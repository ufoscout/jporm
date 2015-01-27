/*******************************************************************************
 * Copyright 2013 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/*
 * ---------------------------------------------------------------------------- PROJECT : JPOrm CREATED BY : Francesco
 * Cina' ON : Feb 23, 2013 ----------------------------------------------------------------------------
 */
package com.jporm.core.query.update;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.jporm.cache.Cache;
import com.jporm.core.inject.ClassTool;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.util.ArrayUtil;
import com.jporm.exception.OrmOptimisticLockException;
import com.jporm.persistor.Persistor;
import com.jporm.query.find.FindQueryWhere;
import com.jporm.query.update.CustomUpdateQuery;
import com.jporm.query.update.CustomUpdateQuerySet;
import com.jporm.query.update.CustomUpdateQueryWhere;
import com.jporm.session.SqlExecutor;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Feb 23, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class UpdateQueryImpl<BEAN> implements UpdateQuery<BEAN> {

	// private final BEAN bean;
	private final Stream<BEAN> beans;
	private final Class<BEAN> clazz;
	private final ServiceCatalog serviceCatalog;
	private final ClassTool<BEAN> ormClassTool;
	private boolean executed;
	private final Persistor<BEAN> persistor;
	private final String[] pkAndVersionFieldNames;
	private final String[] notPksFieldNames;

	/**
	 * @param newBean
	 * @param serviceCatalog
	 * @param ormSession
	 */
	public UpdateQueryImpl(final Stream<BEAN> beans, Class<BEAN> clazz, final ServiceCatalog serviceCatalog) {
		this.beans = beans;
		this.serviceCatalog = serviceCatalog;
		this.clazz = clazz;
		ormClassTool = serviceCatalog.getClassToolMap().get(clazz);
		persistor = ormClassTool.getPersistor();
		pkAndVersionFieldNames = ormClassTool.getDescriptor().getPrimaryKeyAndVersionColumnJavaNames();
		notPksFieldNames = ormClassTool.getDescriptor().getNotPrimaryKeyColumnJavaNames();
	}

	@Override
	public Stream<BEAN> now() {
		return nowWithoutBatchUpdate();
	}

	@Override
	public void execute() {
		now();
	}

	@Override
	public boolean isExecuted() {
		return executed;
	}


	private String getQuery() {
		Cache cache = serviceCatalog.getCrudQueryCache().update();

		return cache.get(clazz, String.class, key -> {

			CustomUpdateQuery updateQuery = serviceCatalog.getSession().updateQuery(clazz);

			CustomUpdateQueryWhere updateQueryWhere = updateQuery.where();
			for (int i = 0; i < pkAndVersionFieldNames.length; i++) {
				updateQueryWhere.eq(pkAndVersionFieldNames[i], "");
			}

			CustomUpdateQuerySet updateQuerySet = updateQuery.set();

			for (int i = 0; i < notPksFieldNames.length; i++) {
				updateQuerySet.eq(notPksFieldNames[i], "");
			}

			return updateQuery.renderSql();
		});

	}


	private String getLockQuery() {

		Cache cache = serviceCatalog.getCrudQueryCache().updateLock();

		return cache.get(clazz, String.class, key -> {
			FindQueryWhere<BEAN> query = serviceCatalog.getSession().findQuery(clazz).lockMode(persistor.getVersionableLockMode()).where();
			for (int i = 0; i < pkAndVersionFieldNames.length; i++) {
				query.eq(pkAndVersionFieldNames[i], "");
			}
			return query.renderRowCountSql();
		});

	}


	private Stream<BEAN> nowWithoutBatchUpdate() {
		executed = true;

		String updateQuery = getQuery();
		String lockQuery = getLockQuery();
		final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();

		// VERSION WITHOUT BATCH UPDATE
		return beans.map(bean -> {
			BEAN updatedBean = persistor.clone(bean);

			Object[] pkAndOriginalVersionValues = ormClassTool.getPersistor().getPropertyValues(pkAndVersionFieldNames, updatedBean);
			persistor.increaseVersion(updatedBean, false);
			Object[] notPksValues = ormClassTool.getPersistor().getPropertyValues(notPksFieldNames, updatedBean);

			if (persistor.isVersionableWithLock()) {

				if (sqlExec.queryForIntUnique(lockQuery, pkAndOriginalVersionValues) == 0) {
					throw new OrmOptimisticLockException(
							"The bean of class [" + clazz + "] cannot be updated. Version in the DB is not the expected one."); //$NON-NLS-1$
				}
			}


			if (sqlExec.update(updateQuery, ArrayUtil.concat(notPksValues, pkAndOriginalVersionValues)) == 0) {
				throw new OrmOptimisticLockException(
						"The bean of class [" + clazz + "] cannot be updated. Version in the DB is not the expected one or the ID of the bean is associated with and existing bean."); //$NON-NLS-1$
			}
			return updatedBean;
		});

	}


	private Stream<BEAN> nowWithBatchUpdate() {
		executed = true;

		String updateQuery = getQuery();
		String lockQuery = getLockQuery();
		List<BEAN> updatedBeans = new ArrayList<>();
		final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();

		Stream<Object[]> values = beans.map(bean -> {
			BEAN updatedBean = persistor.clone(bean);
			updatedBeans.add(updatedBean);
			Object[] pkAndOriginalVersionValues = ormClassTool.getPersistor().getPropertyValues(pkAndVersionFieldNames, updatedBean);
			persistor.increaseVersion(updatedBean, false);
			Object[] notPksValues = ormClassTool.getPersistor().getPropertyValues(notPksFieldNames, updatedBean);

			//TODO this could be done in a single query
			if (persistor.isVersionableWithLock()) {

				if (sqlExec.queryForIntUnique(lockQuery, pkAndOriginalVersionValues) == 0) {
					throw new OrmOptimisticLockException(
							"The bean of class [" + clazz + "] cannot be updated. Version in the DB is not the expected one."); //$NON-NLS-1$
				}
			}

			return ArrayUtil.concat(notPksValues, pkAndOriginalVersionValues);
		});

		int[] result = sqlExec.batchUpdate(updateQuery, values);

		if (IntStream.of(result).sum() < updatedBeans.size()) {
			throw new OrmOptimisticLockException(
					"The bean of class [" + clazz + "] cannot be updated. Version in the DB is not the expected one or the ID of the bean is not associated with and existing bean."); //$NON-NLS-1$
		}

		return updatedBeans.stream();
	}

}
