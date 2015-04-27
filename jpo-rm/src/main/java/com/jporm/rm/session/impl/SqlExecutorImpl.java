/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.rm.session.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.io.ResultSetRowReaderToResultSetReader;
import com.jporm.commons.core.io.ResultSetRowReaderToResultSetReaderUnique;
import com.jporm.commons.core.session.ASqlExecutor;
import com.jporm.commons.core.util.BigDecimalUtil;
import com.jporm.rm.session.SqlExecutor;
import com.jporm.rm.session.SqlPerformerStrategy;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.TypeConverterJdbcReady;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;
import com.jporm.types.io.StatementSetter;

/**
 * @author Francesco Cina 02/lug/2011
 */
public class SqlExecutorImpl extends ASqlExecutor implements SqlExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SqlExecutorImpl.class);

	private final SqlPerformerStrategy sqlPerformerStrategy;

	/**
	 * @param sqlPerformerStrategy2
	 * @param serviceCatalog
	 */
	public SqlExecutorImpl(final SqlPerformerStrategy sqlPerformerStrategy, final TypeConverterFactory typeFactory) {
		super(typeFactory);
		this.sqlPerformerStrategy = sqlPerformerStrategy;
	}

	@Override
	public int[] batchUpdate(final Stream<String> sqls) throws JpoException {
		return sqlPerformerStrategy.batchUpdate(sqls);
	}

	@Override
	public int[] batchUpdate(final String sql, final BatchPreparedStatementSetter psc) throws JpoException {
		return sqlPerformerStrategy.batchUpdate(sql, psc);
	}

	@Override
	public int[] batchUpdate(final String sql, final Stream<Object[]> args) throws JpoException {
		return sqlPerformerStrategy.batchUpdate(sql, args.map(values -> {
		Object[] unwrappedValues = new Object[values.length];
		for (int i=0; i<values.length; i++) {
			Object object = values[i];
			if (object!=null) {
				TypeConverterJdbcReady<Object, Object> typeWrapper = (TypeConverterJdbcReady<Object, Object>) getTypeFactory().getTypeConverter(object.getClass());
				unwrappedValues[i] = typeWrapper.toJdbcType(object);
			}
		}
		return unwrappedValues;
	}));
	}

	@Override
	public void execute(final String sql) throws JpoException {
		sqlPerformerStrategy.execute(sql);
	}

	@Override
	public <T> T query(final String sql, final ResultSetReader<T> rse, final Collection<?> args) throws JpoException {
		StatementSetter pss = new PrepareStatementSetterCollectionWrapper(args);
		return sqlPerformerStrategy.query(sql, pss, rse);
	}

	@Override
	public <T> T query(final String sql, final ResultSetReader<T> rse, final Object... args) throws JpoException {
		StatementSetter pss = new PrepareStatementSetterArrayWrapper(args);
		return sqlPerformerStrategy.query(sql, pss, rse);
	}

	@Override
	public <T> List<T> query(final String sql, final ResultSetRowReader<T> rsrr, final Collection<?> args)
			throws JpoException {
		return query(sql, new ResultSetRowReaderToResultSetReader<T>(rsrr), args);
	}

	@Override
	public <T> List<T> query(final String sql, final ResultSetRowReader<T> rsrr, final Object... args)
			throws JpoException {
		return query(sql, new ResultSetRowReaderToResultSetReader<T>(rsrr), args);
	}

	@Override
	public BigDecimal queryForBigDecimal(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
	}

	@Override
	public BigDecimal queryForBigDecimal(final String sql, final Object... args) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
	}

	@Override
	public final BigDecimal queryForBigDecimalUnique(final String sql, final Collection<?> values) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
	}

	@Override
	public final BigDecimal queryForBigDecimalUnique(final String sql, final Object... values) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
	}

	@Override
	public Boolean queryForBoolean(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return BigDecimalUtil.toBoolean(result);
	}

	@Override
	public Boolean queryForBoolean(final String sql, final Object... args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return BigDecimalUtil.toBoolean(result);
	}

	@Override
	public final Boolean queryForBooleanUnique(final String sql, final Collection<?> values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return BigDecimalUtil.toBoolean(result);
	}

	@Override
	public final Boolean queryForBooleanUnique(final String sql, final Object... values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return BigDecimalUtil.toBoolean(result);
	}

	@Override
	public Double queryForDouble(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return BigDecimalUtil.toDouble(result);
	}

	@Override
	public Double queryForDouble(final String sql, final Object... args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return BigDecimalUtil.toDouble(result);
	}

	@Override
	public final Double queryForDoubleUnique(final String sql, final Collection<?> values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return BigDecimalUtil.toDouble(result);
	}

	@Override
	public final Double queryForDoubleUnique(final String sql, final Object... values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return BigDecimalUtil.toDouble(result);
	}

	@Override
	public Float queryForFloat(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return BigDecimalUtil.toFloat(result);
	}

	@Override
	public Float queryForFloat(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return BigDecimalUtil.toFloat(result);
	}

	@Override
	public final Float queryForFloatUnique(final String sql, final Collection<?> values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return BigDecimalUtil.toFloat(result);
	}

	@Override
	public final Float queryForFloatUnique(final String sql, final Object... values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return BigDecimalUtil.toFloat(result);
	}

	@Override
	public Integer queryForInt(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return BigDecimalUtil.toInteger(result);
	}

	@Override
	public Integer queryForInt(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return BigDecimalUtil.toInteger(result);
	}

	@Override
	public final Integer queryForIntUnique(final String sql, final Collection<?> values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return BigDecimalUtil.toInteger(result);
	}

	@Override
	public final Integer queryForIntUnique(final String sql, final Object... values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return BigDecimalUtil.toInteger(result);
	}

	@Override
	public Long queryForLong(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return BigDecimalUtil.toLong(result);
	}

	@Override
	public Long queryForLong(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL, args);
		return BigDecimalUtil.toLong(result);
	}

	@Override
	public final Long queryForLongUnique(final String sql, final Collection<?> values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return BigDecimalUtil.toLong(result);
	}

	@Override
	public final Long queryForLongUnique(final String sql, final Object... values) throws JpoException,
	JpoNotUniqueResultException {
		BigDecimal result = this.query(sql, RESULT_SET_READER_BIG_DECIMAL_UNIQUE, values);
		return BigDecimalUtil.toLong(result);
	}

	@Override
	public String queryForString(final String sql, final Collection<?> args) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_STRING, args);
	}

	@Override
	public String queryForString(final String sql, final Object... args) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_STRING, args);
	}

	@Override
	public final String queryForStringUnique(final String sql, final Collection<?> values) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_STRING_UNIQUE, values);
	}

	@Override
	public final String queryForStringUnique(final String sql, final Object... values) throws JpoException,
	JpoNotUniqueResultException {
		return this.query(sql, RESULT_SET_READER_STRING_UNIQUE, values);
	}

	@Override
	public <T> T queryForUnique(final String sql, final ResultSetRowReader<T> rsrr, final Collection<?> args)
			throws JpoException {
		return query(sql, new ResultSetRowReaderToResultSetReaderUnique<T>(rsrr), args);
	}

	@Override
	public <T> T queryForUnique(final String sql, final ResultSetRowReader<T> rsrr, final Object... args)
			throws JpoException, JpoNotUniqueResultException {
		return query(sql, new ResultSetRowReaderToResultSetReaderUnique<T>(rsrr), args);
	}

	@Override
	public int update(final String sql, final Collection<?> args) throws JpoException {
		StatementSetter pss = new PrepareStatementSetterCollectionWrapper(args);
		return sqlPerformerStrategy.update(sql, pss);
	}

	@Override
	public int update(final String sql, final GeneratedKeyReader generatedKeyReader, final Collection<?> args)
			throws JpoException {
		StatementSetter pss = new PrepareStatementSetterCollectionWrapper(args);
		return sqlPerformerStrategy.update(sql, generatedKeyReader, pss);
	}

	@Override
	public int update(final String sql, final GeneratedKeyReader generatedKeyReader, final Object... args)
			throws JpoException {
		StatementSetter pss = new PrepareStatementSetterArrayWrapper(args);
		return sqlPerformerStrategy.update(sql, generatedKeyReader, pss);
	}

	@Override
	public int update(final String sql, final GeneratedKeyReader generatedKeyReader, final StatementSetter psc)
			throws JpoException {
		return sqlPerformerStrategy.update(sql, generatedKeyReader, psc);
	}

	@Override
	public int update(final String sql, final Object... args) throws JpoException {
		StatementSetter pss = new PrepareStatementSetterArrayWrapper(args);
		return sqlPerformerStrategy.update(sql, pss);
	}

	@Override
	public int update(final String sql, final StatementSetter psc) throws JpoException {
		return sqlPerformerStrategy.update(sql, psc);
	}

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

}
