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
package com.jporm.sql.query.clause.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.sql.dialect.DBProfile;
import com.jporm.sql.query.ASqlRoot;
import com.jporm.sql.query.clause.From;
import com.jporm.sql.query.clause.GroupBy;
import com.jporm.sql.query.clause.OrderBy;
import com.jporm.sql.query.clause.Select;
import com.jporm.sql.query.clause.Where;
import com.jporm.sql.query.clause.WhereExpressionElement;
import com.jporm.sql.query.namesolver.NameSolver;
import com.jporm.sql.query.namesolver.impl.NameSolverImpl;
import com.jporm.sql.query.namesolver.impl.PropertiesFactory;
import com.jporm.sql.query.tool.DescriptorToolMap;
import com.jporm.sql.util.StringUtil;

/**
 *
 * @author Francesco Cina
 *
 * 07/lug/2011
 */
public class SelectImpl<BEAN> extends ASqlRoot implements Select {

	public static String[] NO_FIELDS = new String[0];
	public static String SQL_SELECT_SPLIT_PATTERN = "[^,]*[\\(][^\\)]*[\\)][^,]*|[^,]+"; //$NON-NLS-1$
	private static Pattern patternSelectClause = Pattern.compile(SQL_SELECT_SPLIT_PATTERN);

	private final NameSolver nameSolver;
	private final FromImpl<BEAN> from;
	private final WhereImpl where = new WhereImpl();
	private final OrderByImpl orderBy= new OrderByImpl();
	private final GroupByImpl groupBy = new GroupByImpl();

	private int versionStatus = 0;
	private boolean distinct = false;
	private LockMode lockMode = LockMode.NO_LOCK;
	private int maxRows = 0;
	private int firstRow = -1;
	private String[] selectFields = NO_FIELDS;
	private ClassDescriptor<BEAN> classDescriptor;

	public SelectImpl(final DescriptorToolMap classDescriptorMap, final PropertiesFactory propertiesFactory, Class<BEAN> clazz) {
		this(classDescriptorMap, propertiesFactory, clazz, clazz.getSimpleName());
	}

	public SelectImpl(final DescriptorToolMap classDescriptorMap, final PropertiesFactory propertiesFactory, Class<BEAN> clazz, String alias) {
		super(classDescriptorMap);
		this.classDescriptor = classDescriptorMap.get(clazz).getDescriptor();
		nameSolver = new NameSolverImpl(propertiesFactory, false);
		from = new FromImpl<BEAN>(classDescriptorMap, clazz, nameSolver.register(clazz, alias, classDescriptor), nameSolver);
	}

	@Override
	public String renderRowCountSql(DBProfile dbProfile) {
		final StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(*) FROM ( "); //$NON-NLS-1$
		renderSQLWithoutPagination(dbProfile, queryBuilder);
		queryBuilder.append( ") a " ); //$NON-NLS-1$
		return queryBuilder.toString();
	}

	@Override
	public void renderSql(DBProfile dbProfile, StringBuilder queryBuilder) {
		dbProfile.getSqlStrategy().paginateSQL(queryBuilder, firstRow, maxRows, builder -> renderSQLWithoutPagination(dbProfile, builder));
	}

	private void renderSQLWithoutPagination(DBProfile dbProfile, StringBuilder builder) {
		builder.append("SELECT "); //$NON-NLS-1$
		if (distinct) {
			builder.append("DISTINCT "); //$NON-NLS-1$
		}

		int size = selectFields.length;
		boolean first = true;
		for (int i=0; i<size; i++) {
			String field = selectFields[i];
				if (!first) {
					builder.append(", "); //$NON-NLS-1$
				} else {
					first = false;
				}

				final Matcher m = patternSelectClause.matcher(field);
				boolean loop = m.find();
				while (loop) {
					solveField(m.group().trim(), builder, nameSolver);
					loop = m.find();
					if (loop) {
						builder.append(", "); //$NON-NLS-1$
					}
				}
			}

		builder.append(" "); //$NON-NLS-1$
		from.renderSqlElement(dbProfile, builder, nameSolver);
		where.renderSqlElement(dbProfile, builder, nameSolver);
		groupBy.renderSqlElement(dbProfile, builder, nameSolver);
		orderBy.renderSqlElement(dbProfile, builder, nameSolver);
		builder.append(lockMode.getMode());
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
	public Select distinct(final boolean distinct) {
		this.distinct = distinct;
		versionStatus++;
		return this;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public String[] getSelectFields() {
		return selectFields;
	}

	@Override
	public Select selectFields(String... selectFields) {
		this.selectFields = selectFields;
		versionStatus++;
		return this;
	}

	@Override
	public void appendValues(List<Object> values) {
		where.appendElementValues(values);
		groupBy.appendElementValues(values);
	}

	@Override
	public int getVersion() {
		return versionStatus;
	}

	@Override
	public Where where() {
		return where;
	}

	@Override
	public Where where(final List<WhereExpressionElement> expressionElements) {
		return where.and(expressionElements);
	}

	@Override
	public Where where(final String customClause, final Object... args) {
		return where.and(customClause, args);
	}

	@Override
	public Where where(final WhereExpressionElement... expressionElements) {
		return where.and(expressionElements);
	}

	@Override
	public OrderBy orderBy() {
		return orderBy;
	}

	@Override
	public GroupBy groupBy() {
		return groupBy;
	}

	@Override
	public From from() {
		return from;
	}

	public LockMode getLockMode() {
		return lockMode;
	}

	@Override
	public void lockMode(LockMode lockMode) {
		versionStatus++;
		this.lockMode = lockMode;
	}

	@Override
	public Select maxRows(int maxRows) {
		versionStatus++;
		this.maxRows = maxRows;
		return this;
	}

	@Override
	public Select firstRow(int firstRow) {
		versionStatus++;
		this.firstRow = firstRow;
		return this;
	}

}
