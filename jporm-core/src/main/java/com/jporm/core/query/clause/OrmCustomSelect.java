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
package com.jporm.core.query.clause;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jporm.core.query.SmartRenderableSqlSubElement;
import com.jporm.core.util.StringUtil;
import com.jporm.exception.OrmException;
import com.jporm.query.clause.Select;
import com.jporm.query.namesolver.NameSolver;

/**
 *
 * @author Francesco Cina
 *
 * 07/lug/2011
 */
public abstract class OrmCustomSelect<T extends Select<?>> extends SmartRenderableSqlSubElement implements Select<T> {

	public static String SQL_SELECT_SPLIT_PATTERN = "[^,]*[\\(][^\\)]*[\\)][^,]*|[^,]+"; //$NON-NLS-1$

	private static Pattern patternSelectClause = Pattern.compile(SQL_SELECT_SPLIT_PATTERN);

	private int versionStatus = 0;
	private boolean distinct = false;
	private final String[] selectFields;

	private List<String> _ignoredFields = Collections.EMPTY_LIST;

	public OrmCustomSelect(final String[] selectFields) {
		this.selectFields = selectFields;
	}

	@Override
	public final void renderSqlElement(final StringBuilder queryBuilder, final NameSolver nameSolver) {
		queryBuilder.append("SELECT "); //$NON-NLS-1$
		if (this.distinct) {
			queryBuilder.append("DISTINCT "); //$NON-NLS-1$
		}

		int size = selectFields.length;
		boolean first = true;
		for (int i=0; i<size; i++) {
			String field = selectFields[i];
			if (!_ignoredFields.contains(field)) {

				if (!first) {
					queryBuilder.append(", "); //$NON-NLS-1$
				} else {
					first = false;
				}

				final Matcher m = patternSelectClause.matcher(field);
				boolean loop = m.find();
				while (loop) {
					solveField(m.group().trim(), queryBuilder, nameSolver);
					loop = m.find();
					if (loop) {
						queryBuilder.append(", "); //$NON-NLS-1$
					}
				}
			}
		}

		queryBuilder.append(" "); //$NON-NLS-1$
	}

	/**
	 * @param string
	 * @return
	 */
	private void solveField(final String field, final StringBuilder queryBuilder, final NameSolver nameSolver) {
		if ( field.contains("(") || StringUtil.containsIgnoreCase(field, " as ") ) { //$NON-NLS-1$ //$NON-NLS-2$
			nameSolver.solveAllPropertyNames(field, queryBuilder ) ;
		} else {
			queryBuilder.append( nameSolver.solvePropertyName( field ) );
			queryBuilder.append( " AS \"" ); //$NON-NLS-1$
			queryBuilder.append( field );
			queryBuilder.append( "\"" ); //$NON-NLS-1$
		}
	}

	@Override
	public final int getElementStatusVersion() {
		return this.versionStatus;
	}

	@Override
	public final void appendElementValues(final List<Object> values) {
		// do nothing
	}

	public T setDistinct(final boolean distinct) {
		this.distinct = distinct;
		this.versionStatus++;
		return select();
	}

	public boolean isDistinct() throws OrmException {
		return this.distinct;
	}

	/**
	 * @param _ignoredFields
	 */
	@Override
	public void ignore(final List<String> ignoredFields) {
		this._ignoredFields = ignoredFields;
	}

	protected abstract T select();

}
