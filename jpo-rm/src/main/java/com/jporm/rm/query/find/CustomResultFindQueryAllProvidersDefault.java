package com.jporm.rm.query.find;

import java.util.List;

import com.jporm.sql.query.select.LockMode;
import com.jporm.sql.query.select.SelectCommon;
import com.jporm.sql.query.select.groupby.GroupByProvider;
import com.jporm.sql.query.select.orderby.OrderByProvider;
import com.jporm.sql.query.where.WhereProvider;

public interface CustomResultFindQueryAllProvidersDefault extends WhereProvider<CustomResultFindQueryWhere>,
																GroupByProvider<CustomResultFindQueryGroupBy>,
																OrderByProvider<CustomResultFindQueryOrderBy>,
																CustomResultFindQueryExecutorProvider,
																CustomResultFindQueryUnionsProvider,
																CustomResultFindQueryPaginationProvider,
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
	default CustomResultFindQueryPaginationProvider limit(int limit) {
		return getFindQuery().limit(limit);
	}

	@Override
	default CustomResultFindQueryPaginationProvider lockMode(LockMode lockMode) {
		return getFindQuery().lockMode(lockMode);
	}

	@Override
	default CustomResultFindQueryPaginationProvider forUpdate() {
		return getFindQuery().forUpdate();
	}

	@Override
	default CustomResultFindQueryPaginationProvider forUpdateNoWait() {
		return getFindQuery().forUpdateNoWait();
	}

	@Override
	default CustomResultFindQueryPaginationProvider offset(int offset) {
		return getFindQuery().offset(offset);
	}

	@Override
	default CustomResultFindQueryUnionsProvider union(SelectCommon select) {
		return getFindQuery().union(select);
	}

	@Override
	default CustomResultFindQueryUnionsProvider unionAll(SelectCommon select) {
		return getFindQuery().unionAll(select);
	}

	@Override
	default CustomResultFindQueryUnionsProvider except(SelectCommon select) {
		return getFindQuery().except(select);
	}

	@Override
	default CustomResultFindQueryUnionsProvider intersect(SelectCommon select) {
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
	default CustomResultFindQueryGroupBy groupBy(String... fields) {
		return getFindQuery().groupBy(fields);
	}

	@Override
	default CustomResultFindQueryWhere where() {
		return getFindQuery().where();
	}

	CustomResultFindQuery getFindQuery();

}
