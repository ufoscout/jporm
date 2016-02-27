package com.jporm.rm.query.find;

import java.util.List;

import com.jporm.sql.dsl.query.groupby.GroupByProvider;
import com.jporm.sql.dsl.query.orderby.OrderByProvider;
import com.jporm.sql.dsl.query.select.LockMode;
import com.jporm.sql.dsl.query.select.SelectCommon;
import com.jporm.sql.dsl.query.select.SelectCommonProvider;
import com.jporm.sql.dsl.query.select.SelectUnionsProvider;
import com.jporm.sql.dsl.query.where.WhereProvider;

public interface CustomResultFindQueryAllProvidersDefault extends WhereProvider<CustomResultFindQueryWhere>,
																GroupByProvider<CustomResultFindQueryGroupBy>,
																OrderByProvider<CustomResultFindQueryOrderBy>,
																CustomResultFindQueryExecutorProvider,
																SelectUnionsProvider,
																SelectCommonProvider,
																SelectCommon {

	@Override
	default void sqlValues(List<Object> values) {
		getFindQuery().sqlValues(values);
	}

	@Override
	default void sqlQuery(StringBuilder queryBuilder) {
		getFindQuery().sqlQuery(queryBuilder);
	}

	@Override
	default String sqlRowCountQuery() {
		return getFindQuery().sqlRowCountQuery();
	}

	@Override
	default SelectCommonProvider limit(int limit) {
		return getFindQuery().limit(limit);
	}

	@Override
	default SelectCommonProvider lockMode(LockMode lockMode) {
		return getFindQuery().lockMode(lockMode);
	}

	@Override
	default SelectCommonProvider forUpdate() {
		return getFindQuery().forUpdate();
	}

	@Override
	default SelectCommonProvider forUpdateNoWait() {
		return getFindQuery().forUpdateNoWait();
	}

	@Override
	default SelectCommonProvider offset(int offset) {
		return getFindQuery().offset(offset);
	}

	@Override
	default SelectUnionsProvider union(SelectCommon select) {
		return getFindQuery().union(select);
	}

	@Override
	default SelectUnionsProvider unionAll(SelectCommon select) {
		return getFindQuery().unionAll(select);
	}

	@Override
	default SelectUnionsProvider except(SelectCommon select) {
		return getFindQuery().except(select);
	}

	@Override
	default SelectUnionsProvider intersect(SelectCommon select) {
		return getFindQuery().intersect(select);
	}

	@Override
	default ExecutionEnvProvider<?> getExecutionEnvProvider() {
		return getFindQuery().getExecutionEnvProvider();
	}

	@Override
	default CustomResultFindQueryOrderBy orderBy() {
		return getFindQuery().orderBy();
	}

	@Override
	default CustomResultFindQueryGroupBy groupBy() {
		return getFindQuery().groupBy();
	}

	@Override
	default CustomResultFindQueryWhere where() {
		return getFindQuery().where();
	}
	
	CustomResultFindQuery getFindQuery();

}
