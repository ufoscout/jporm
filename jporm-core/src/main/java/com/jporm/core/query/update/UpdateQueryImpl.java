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

import java.util.List;

import com.jporm.core.inject.ClassTool;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.query.AQueryRoot;
import com.jporm.exception.OrmOptimisticLockException;
import com.jporm.persistor.Persistor;
import com.jporm.query.find.FindQueryWhere;
import com.jporm.query.update.CustomUpdateQuery;
import com.jporm.query.update.CustomUpdateQuerySet;
import com.jporm.query.update.CustomUpdateQueryWhere;
import com.jporm.query.update.UpdateQuery;

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
public class UpdateQueryImpl<BEAN> extends AQueryRoot implements UpdateQuery<BEAN> {

	//private final BEAN bean;
	private final BEAN updatedBean;
	private final Class<BEAN> clazz;
	private final ServiceCatalog serviceCatalog;
	private final ClassTool<BEAN> ormClassTool;
	private final CustomUpdateQuery updateQuery;
	private final String[] pkAndVersionFieldNames;
	private boolean executed;
	private final Persistor<BEAN> persistor;

	/**
	 * @param newBean
	 * @param serviceCatalog
	 * @param ormSession
	 */
	public UpdateQueryImpl(final BEAN bean, final ServiceCatalog serviceCatalog) {
		super(serviceCatalog);
		//this.bean = bean;
		this.serviceCatalog = serviceCatalog;
		this.clazz = (Class<BEAN>) bean.getClass();
		ormClassTool = serviceCatalog.getClassToolMap().get(clazz);
		persistor = ormClassTool.getPersistor();
		this.updatedBean = persistor.clone(bean);
		pkAndVersionFieldNames = ormClassTool.getDescriptor().getPrimaryKeyAndVersionColumnJavaNames();
		updateQuery = getQuery();
	}

	@Override
	public BEAN now() {
		executed = true;
		serviceCatalog.getValidatorService().validator(updatedBean).validateThrowException();
		// CHECK IF OBJECT HAS A 'VERSION' FIELD AND THE DATA MUST BE LOCKED BEFORE UPDATE
		if (persistor.isVersionableWithLock()) {
			FindQueryWhere<BEAN> query = (FindQueryWhere<BEAN>) serviceCatalog.getSession().findQuery(updatedBean.getClass())
					.lockMode(persistor.getVersionableLockMode()).where();
			Object[] values = persistor.getPropertyValues(pkAndVersionFieldNames, updatedBean);
			for (int i = 0; i < pkAndVersionFieldNames.length; i++) {
				query.eq(pkAndVersionFieldNames[i], values[i]);
			}
			if (query.getRowCount() == 0) {
				throw new OrmOptimisticLockException(
						"The bean of class [" + clazz + "] cannot be updated. Version in the DB is not the expected one."); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		if (updateQuery.now() == 0) {
			throw new OrmOptimisticLockException(
					"The bean of class [" + clazz + "] cannot be updated. Version in the DB is not the expected one or the ID of the bean is associated with and existing bean."); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return updatedBean;
	}

	@Override
	public void execute() {
		now();
	}

	@Override
	public boolean isExecuted() {
		return executed ;
	}

	@Override
	public void renderSql(StringBuilder queryBuilder) {
		updateQuery.renderSql(queryBuilder);
	}

	@Override
	public void appendValues(List<Object> values) {
		updateQuery.appendValues(values);
	}

	@Override
	public int getStatusVersion() {
		return 0;
	}

	private CustomUpdateQuery getQuery() {
		CustomUpdateQuery updateQuery = serviceCatalog.getSession().updateQuery(updatedBean.getClass());

		CustomUpdateQueryWhere updateQueryWhere = updateQuery.where();
		Object[] pkAndVersionValues = persistor.getPropertyValues(pkAndVersionFieldNames, updatedBean);
		for (int i = 0; i < pkAndVersionFieldNames.length; i++) {
			updateQueryWhere.eq(pkAndVersionFieldNames[i], pkAndVersionValues[i]);
		}

		persistor.increaseVersion(updatedBean, false);

		CustomUpdateQuerySet updateQuerySet = updateQuery.set();
		String[] notPks = ormClassTool.getDescriptor().getNotPrimaryKeyColumnJavaNames();
		Object[] notPkValues = persistor.getPropertyValues(notPks, updatedBean);
		for (int i = 0; i < notPks.length; i++) {
			updateQuerySet.eq(notPks[i], notPkValues[i]);
		}

		return updateQuery;

	}

}
