package com.jporm.rx.reactor.query.find;

import com.jporm.rx.reactor.session.SqlExecutor;
import com.jporm.sql.SqlDsl;
import com.jporm.sql.query.select.Select;

public class SelectQueryBuilderImpl implements SelectQueryBuilder {
    private final String[] selectFields;
    private final SqlExecutor sqlExecutor;
	private final SqlDsl<String> sqlDsl;

    /**
     *
     * @param selectFields
     * @param serviceCatalog
     * @param sqlExecutor
     * @param sqlFactory
     * @param dbType
     */
    public SelectQueryBuilderImpl(final String[] selectFields, final SqlExecutor sqlExecutor,  final SqlDsl<String> sqlDsl) {
        this.selectFields = selectFields;
        this.sqlExecutor = sqlExecutor;
		this.sqlDsl = sqlDsl;
    }

    @Override
    public CustomResultFindQuery<String> from(String table, String alias) {
        Select<String> select = sqlDsl.select(selectFields).from(table, alias);
        return new CustomResultFindQueryImpl<>(select, sqlExecutor);
    }

}
