/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.core.session;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.jporm.core.dialect.querytemplate.QueryTemplate;
import com.jporm.core.inject.ServiceCatalog;
import com.jporm.core.session.reader.ArrayResultSetReader;
import com.jporm.core.session.reader.ArrayResultSetReaderUnique;
import com.jporm.core.session.reader.BigDecimalResultSetReader;
import com.jporm.core.session.reader.BigDecimalResultSetReaderUnique;
import com.jporm.core.session.reader.ListResultSetReader;
import com.jporm.core.session.reader.ResultSetRowReaderToResultSetReader;
import com.jporm.core.session.reader.ResultSetRowReaderToResultSetReaderUnique;
import com.jporm.core.session.reader.StringResultSetReader;
import com.jporm.core.session.reader.StringResultSetReaderUnique;
import com.jporm.exception.OrmException;
import com.jporm.exception.OrmNotUniqueResultException;
import com.jporm.session.BatchPreparedStatementSetter;
import com.jporm.session.GeneratedKeyReader;
import com.jporm.session.PreparedStatementSetter;
import com.jporm.session.ResultSetReader;
import com.jporm.session.ResultSetRowReader;
import com.jporm.session.SqlExecutor;
import com.jporm.types.TypeFactory;

/**
 * @author Francesco Cina 02/lug/2011
 */
public class SqlExecutorImpl implements SqlExecutor {

	public static final ResultSetReader<String> RESULT_SET_READER_STRING_UNIQUE = new StringResultSetReaderUnique();
	public static final ResultSetReader<String> RESULT_SET_READER_STRING = new StringResultSetReader();
	public static final ResultSetReader<BigDecimal> RESULT_SET_READER_BIG_DECIMAL_UNIQUE = new BigDecimalResultSetReaderUnique();
	public static final ResultSetReader<BigDecimal> RESULT_SET_READER_BIG_DECIMAL = new BigDecimalResultSetReader();
	public static final ResultSetReader<Object[]> RESULT_SET_READER_ARRAY_UNIQUE = new ArrayResultSetReaderUnique();
	public static final ResultSetReader<Object[]> RESULT_SET_READER_ARRAY = new ArrayResultSetReader();
	public static final ResultSetReader<List<Object[]>> RESULT_SET_READER_LIST = new ListResultSetReader();

	private final SqlPerformerStrategy sqlPerformerStrategy;
	private final TypeFactory typeFactory;
	private final QueryTemplate queryTemplate;
	private int queryTimeout = 0;
	private int maxRows = 0;

	/**
	 * @param sqlPerformerStrategy2
	 * @param serviceCatalog
	 */
	public SqlExecutorImpl(final SqlPerformerStrategy sqlPerformerStrategy, final ServiceCatalog serviceCatalog) {
		this.sqlPerformerStrategy = sqlPerformerStrategy;
		typeFactory = serviceCatalog.getTypeFactory();
		queryTemplate = serviceCatalog.getDbProfile().getQueryTemplate();
	}

	@Override
	public int[] batchUpdate(final Stream<String> sqls) throws OrmException {
		return sqlPerformerStrategy.batchUpdate(sqls, getTimeout());
	}

	@Override
	public int[] batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws OrmException {
		return sqlPerformerStrategy.batchUpdate(sql, psc, getTimeout());
	}

	@Override
	public int[] batchUpdate(final String sql, final Stream<Object[]> args) throws OrmException {
		return sqlPerformerStrategy.batchUpdate(sql, args, getTimeout());
	}

	@Override
	public void execute(final String sql) throws OrmException {
		sqlPerformerStrategy.execute(sql, getTimeout());
	}

	@Override
	public final int getMaxRows() {
		return maxRows;
	}

	@Override
	public final int getTimeout() {
		return queryTimeout;
	}

	@Override
	public <T> T query(final String sql, final ResultSetReader<T> rse, final Collection<?> args) throws OrmException {
		return sqlPerformerStrategy.query(sql, rse, getTimeout(), getMaxRows(), args, typeFactory);
	}

	@Override
	public <T> T query(final String sql, final ResultSetReader<T> rse, final Object... args) throws OrmException {
		return sqlPerformerStrategy.query(sql, rse, getTimeout(), getMaxRows(), args, typeFactory);
	}

	@Override
	public <T> List<T> query(final String sql, final ResultSetRowReader<T> rsrr, final Collection<?> args)
			throws OrmException {
		return query(sql, new ResultSetRowReaderToResultSetReader<T>(rsrr), args);
	}

	@Override
	public <T> List<T> query(final String sql, final ResultSetRowReader<T> rsrr, final Object... args)
			throws OrmException {
		return query(sql, new ResultSetRowReaderToResultSetReader<T>(rsrr), args);
	}

	@Override
	public Object[] queryForArray(String sql, Collection<?> args) throws OrmException, OrmNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_ARRAY, args);
	}

	@Override
	public Object[] queryForArray(String sql, Object... args) throws OrmException, OrmNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_ARRAY, args);
	}

	@Override
	public Optional<Object[]> queryForArrayOptional(final String sql, final Collection<?> args) {
		return Optional.ofNullable(queryForArray(sql, args));
	}

	@Override
	public Optional<Object[]> queryForArrayOptional(final String sql, final Object... args) {
		return Optional.ofNullable(queryForArray(sql, args));
	}

	@Override
	public final Object[] queryForArrayUnique(final String sql, final Collection<?> values) throws OrmException,
	OrmNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_ARRAY_UNIQUE, values);
	}

	@Override
	public final Object[] queryForArrayUnique(final String sql, final Object... values) throws OrmException,
	OrmNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_ARRAY_UNIQUE, values);
	}

	@Override
	public BigDecimal queryForBigDecimal(final String sql, final Collection<?> args) throws OrmException,
	OrmNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
	}

	@Override
	public BigDecimal queryForBigDecimal(final String sql, final Object... args) throws OrmException,
	OrmNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
	}

	@Override
	public final BigDecimal queryForBigDecimalUnique(final String sql, final Collection<?> values) throws OrmException,
	OrmNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
	}

	@Override
	public final BigDecimal queryForBigDecimalUnique(final String sql, final Object... values) throws OrmException,
	OrmNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
	}

	@Override
	public Boolean queryForBoolean(final String sql, final Collection<?> args) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : BigDecimal.ONE.equals(result);
	}

	@Override
	public Boolean queryForBoolean(final String sql, final Object... args) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : BigDecimal.ONE.equals(result);
	}

	@Override
	public final Boolean queryForBooleanUnique(final String sql, final Collection<?> values) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : BigDecimal.ONE.equals(result);
	}

	@Override
	public final Boolean queryForBooleanUnique(final String sql, final Object... values) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : BigDecimal.ONE.equals(result);
	}

	@Override
	public Double queryForDouble(final String sql, final Collection<?> args) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : result.doubleValue();
	}

	@Override
	public Double queryForDouble(final String sql, final Object... args) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : result.doubleValue();
	}

	@Override
	public final Double queryForDoubleUnique(final String sql, final Collection<?> values) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : result.doubleValue();
	}

	@Override
	public final Double queryForDoubleUnique(final String sql, final Object... values) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : result.doubleValue();
	}

	@Override
	public Float queryForFloat(final String sql, final Collection<?> args) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : result.floatValue();
	}

	@Override
	public Float queryForFloat(final String sql, final Object... args) throws OrmException, OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : result.floatValue();
	}

	@Override
	public final Float queryForFloatUnique(final String sql, final Collection<?> values) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : result.floatValue();
	}

	@Override
	public final Float queryForFloatUnique(final String sql, final Object... values) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : result.floatValue();
	}

	@Override
	public Integer queryForInt(final String sql, final Collection<?> args) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : result.intValue();
	}

	@Override
	public Integer queryForInt(final String sql, final Object... args) throws OrmException, OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : result.intValue();
	}

	@Override
	public final Integer queryForIntUnique(final String sql, final Collection<?> values) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : result.intValue();
	}

	@Override
	public final Integer queryForIntUnique(final String sql, final Object... values) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : result.intValue();
	}

	@Override
	public final List<Object[]> queryForList(final String sql, final Collection<?> values) throws OrmException {
		return this.query(sql, RESULT_SET_READER_LIST, values);
	}

	@Override
	public final List<Object[]> queryForList(final String sql, final Object... values) throws OrmException {
		return this.query(sql, RESULT_SET_READER_LIST, values);
	}

	@Override
	public Long queryForLong(final String sql, final Collection<?> args) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : result.longValue();
	}

	@Override
	public Long queryForLong(final String sql, final Object... args) throws OrmException, OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return (result == null) ? null : result.longValue();
	}

	@Override
	public final Long queryForLongUnique(final String sql, final Collection<?> values) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : result.longValue();
	}

	@Override
	public final Long queryForLongUnique(final String sql, final Object... values) throws OrmException,
	OrmNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return (result == null) ? null : result.longValue();
	}

	@Override
	public String queryForString(final String sql, final Collection<?> args) throws OrmException,
	OrmNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_STRING, args);
	}

	@Override
	public String queryForString(final String sql, final Object... args) throws OrmException,
	OrmNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_STRING, args);
	}

	@Override
	public final String queryForStringUnique(final String sql, final Collection<?> values) throws OrmException,
	OrmNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_STRING_UNIQUE, values);
	}

	@Override
	public final String queryForStringUnique(final String sql, final Object... values) throws OrmException,
	OrmNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_STRING_UNIQUE, values);
	}

	@Override
	public <T> T queryForUnique(final String sql, final ResultSetRowReader<T> rsrr, final Collection<?> args)
			throws OrmException {
		return query(sql, new ResultSetRowReaderToResultSetReaderUnique<T>(rsrr), args);
	}

	@Override
	public <T> T queryForUnique(final String sql, final ResultSetRowReader<T> rsrr, final Object... args)
			throws OrmException, OrmNotUniqueResultException {
		return query(sql, new ResultSetRowReaderToResultSetReaderUnique<T>(rsrr), args);
	}

	@Override
	public final void setMaxRows(final int maxRows) {
		this.maxRows = maxRows;
	}

	@Override
	public final void setTimeout(final int queryTimeout) {
		this.queryTimeout = queryTimeout;
	}

	@Override
	public int update(final String sql, final Collection<?> args) throws OrmException {
		return sqlPerformerStrategy.update(sql, getTimeout(), args, typeFactory);
	}

	@Override
	public int update(final String sql, final GeneratedKeyReader generatedKeyReader, final Collection<?> args)
			throws OrmException {
		return sqlPerformerStrategy.update(sql, getTimeout(), generatedKeyReader, queryTemplate, args, typeFactory);
	}

	@Override
	public int update(final String sql, final GeneratedKeyReader generatedKeyReader, final Object... args)
			throws OrmException {
		return sqlPerformerStrategy.update(sql, getTimeout(), generatedKeyReader, queryTemplate, args, typeFactory);
	}

	@Override
	public int update(final String sql, final GeneratedKeyReader generatedKeyReader, final PreparedStatementSetter psc)
			throws OrmException {
		return sqlPerformerStrategy.update(sql, getTimeout(), generatedKeyReader, queryTemplate, psc);
	}

	@Override
	public int update(final String sql, final Object... args) throws OrmException {
		return sqlPerformerStrategy.update(sql, getTimeout(), args, typeFactory);
	}

	@Override
	public int update(final String sql, final PreparedStatementSetter psc) throws OrmException {
		return sqlPerformerStrategy.update(sql, getTimeout(), psc);
	}

}
