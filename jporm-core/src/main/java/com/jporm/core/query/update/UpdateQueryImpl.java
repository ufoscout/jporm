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
	// private final Stream<CustomUpdateQuery> queries;
	private boolean executed;
	private final Persistor<BEAN> persistor;

	/**
	 * @param newBean
	 * @param serviceCatalog
	 * @param ormSession
	 */
	public UpdateQueryImpl(final Stream<BEAN> beans, Class<BEAN> clazz, final ServiceCatalog serviceCatalog) {
		this.beans = beans;
		// this.bean = bean;
		this.serviceCatalog = serviceCatalog;
		this.clazz = clazz;
		ormClassTool = serviceCatalog.getClassToolMap().get(clazz);
		persistor = ormClassTool.getPersistor();
		// this.updatedBeans = beans.map(bean ->
		// persistor.clone(bean)).collect(Collectors.toList());
		// queries = getQueries();
	}

	@Override
	public Stream<BEAN> now() {
		executed = true;

		String updateQuery = getQuery();
		String[] pkAndVersionFieldNames = ormClassTool.getDescriptor().getPrimaryKeyAndVersionColumnJavaNames();
		String[] notPksFieldNames = ormClassTool.getDescriptor().getNotPrimaryKeyColumnJavaNames();

		return beans.map(bean -> {
			BEAN updatedBean = persistor.clone(bean);

			final SqlExecutor sqlExec = serviceCatalog.getSession().sqlExecutor();
			Object[] pkAndOriginalVersionValues = ormClassTool.getPersistor().getPropertyValues(pkAndVersionFieldNames, updatedBean);
			persistor.increaseVersion(updatedBean, false);
			Object[] notPksValues = ormClassTool.getPersistor().getPropertyValues(notPksFieldNames, updatedBean);

			if (persistor.isVersionableWithLock()) {
				FindQueryWhere<BEAN> query = serviceCatalog.getSession().findQuery(clazz).lockMode(persistor.getVersionableLockMode()).where();
				for (int i = 0; i < pkAndVersionFieldNames.length; i++) {
					query.eq(pkAndVersionFieldNames[i], pkAndOriginalVersionValues[i]);
				}
				if (query.getRowCount() == 0) {
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

	//	@Override
	//	public Stream<BEAN> nowOld() {
	//		executed = true;
	//
	//		Iterator<BEAN> beanIterator = updatedBeans.iterator();
	//		return queries.map(updateQuery -> {
	//			BEAN updatedBean = beanIterator.next();
	//			// CHECK IF OBJECT HAS A 'VERSION' FIELD AND THE DATA MUST BE LOCKED
	//			//BEFORE UPDATE
	//			if (persistor.isVersionableWithLock()) {
	//				FindQueryWhere<BEAN> query = (FindQueryWhere<BEAN>)
	//						serviceCatalog.getSession().findQuery(updatedBean.getClass())
	//						.lockMode(persistor.getVersionableLockMode()).where();
	//				Object[] values = persistor.getPropertyValues(pkAndVersionFieldNames,
	//						updatedBean);
	//				for (int i = 0; i < pkAndVersionFieldNames.length; i++) {
	//					query.eq(pkAndVersionFieldNames[i], values[i]);
	//				}
	//				if (query.getRowCount() == 0) {
	//					throw new OrmOptimisticLockException(
	//							"The bean of class [" + clazz + "] cannot be updated. Version in the DB is not the expected one."); //$NON-NLS-1$
	//				}
	//			}
	//
	//			if (updateQuery.now() == 0) {
	//				throw new OrmOptimisticLockException(
	//						"The bean of class [" + clazz + "] cannot be updated. Version in the DB is not the expected one or the ID of the bean is associated with and existing bean."); //$NON-NLS-1$
	//			}
	//
	//			return updatedBean;
	//		});
	//
	//	}

	@Override
	public void execute() {
		now();
	}

	@Override
	public boolean isExecuted() {
		return executed;
	}

	// private Stream<CustomUpdateQuery> getQueries() {
	//
	// return updatedBeans.stream().map(bean -> {
	// CustomUpdateQuery updateQuery =
	// serviceCatalog.getSession().updateQuery(clazz);
	//
	// CustomUpdateQueryWhere updateQueryWhere = updateQuery.where();
	// Object[] pkAndVersionValues =
	// persistor.getPropertyValues(pkAndVersionFieldNames, bean);
	// for (int i = 0; i < pkAndVersionFieldNames.length; i++) {
	// updateQueryWhere.eq(pkAndVersionFieldNames[i], pkAndVersionValues[i]);
	// }
	//
	// persistor.increaseVersion(bean, false);
	//
	// CustomUpdateQuerySet updateQuerySet = updateQuery.set();
	// String[] notPks =
	// ormClassTool.getDescriptor().getNotPrimaryKeyColumnJavaNames();
	// Object[] notPkValues = persistor.getPropertyValues(notPks, bean);
	// for (int i = 0; i < notPks.length; i++) {
	// updateQuerySet.eq(notPks[i], notPkValues[i]);
	// }
	//
	// return updateQuery;
	// });
	// }

	private String getQuery() {
		Cache cache = serviceCatalog.getCrudQueryCache().update();

		return cache.get(clazz, String.class, key -> {

			String[] pkAndVersionFieldNames = ormClassTool.getDescriptor().getPrimaryKeyAndVersionColumnJavaNames();
			String[] notPksFieldNames = ormClassTool.getDescriptor().getNotPrimaryKeyColumnJavaNames();

			CustomUpdateQuery updateQuery = serviceCatalog.getSession().updateQuery(clazz);

			CustomUpdateQueryWhere updateQueryWhere = updateQuery.where();
			// Object[] pkAndVersionValues =
			// persistor.getPropertyValues(pkAndVersionFieldNames, bean);
			for (int i = 0; i < pkAndVersionFieldNames.length; i++) {
				updateQueryWhere.eq(pkAndVersionFieldNames[i], "");
			}

			// persistor.increaseVersion(bean, false);

			CustomUpdateQuerySet updateQuerySet = updateQuery.set();

			// Object[] notPkValues = persistor.getPropertyValues(notPks,
			// bean);
			for (int i = 0; i < notPksFieldNames.length; i++) {
				updateQuerySet.eq(notPksFieldNames[i], "");
			}

			return updateQuery.renderSql();
		});

	}

}
