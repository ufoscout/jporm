package com.jporm.rx.reactor.query.find;

public interface SelectQueryBuilder {

    /**
     * Returns the query
     *
     * @param table
     *            The the table name
     * @return
     */
    default CustomResultFindQuery<String> from(String table) {
    	return from(table, "");
    }

    /**
     * Returns the query
     *
     * @param table
     *            The the table name
     * @param alias
     *            the alias for the table
     * @return
     */
    CustomResultFindQuery<String> from(String table, String alias);

}
