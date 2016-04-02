/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.rm.session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.commons.core.connection.Connection;
import com.jporm.commons.core.connection.ConnectionProvider;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.commons.core.exception.JpoNotUniqueResultException;
import com.jporm.commons.core.io.ResultSetRowReaderToResultSetReader;
import com.jporm.commons.core.io.ResultSetRowReaderToResultSetReaderUnique;
import com.jporm.commons.core.session.ASqlExecutor;
import com.jporm.commons.core.util.BigDecimalUtil;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.BatchPreparedStatementSetter;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.Statement;

/**
 * @author Francesco Cina 02/lug/2011
 */
public class SqlExecutorImpl extends ASqlExecutor implements SqlExecutor {

    private static final Function<String, String> SQL_PRE_PROCESSOR_DEFAULT = (sql) -> sql;
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlExecutorImpl.class);
    private final Function<String, String> sqlPreProcessor;
    private final ConnectionProvider connectionProvider;
    private final boolean autoCommit;

    public SqlExecutorImpl(final ConnectionProvider connectionProvider, final TypeConverterFactory typeFactory, final boolean autoCommit) {
        this(connectionProvider, typeFactory, autoCommit, SQL_PRE_PROCESSOR_DEFAULT);
    }

    /**
     * @param sqlPerformerStrategy2
     * @param serviceCatalog
     */
    public SqlExecutorImpl(final ConnectionProvider connectionProvider, final TypeConverterFactory typeFactory, final boolean autoCommit, final Function<String, String> sqlPreProcessor) {
        super(typeFactory);
        this.connectionProvider = connectionProvider;
        this.autoCommit = autoCommit;
        this.sqlPreProcessor = sqlPreProcessor;
    }

    @Override
    public int[] batchUpdate(final Collection<String> sqls) throws JpoException {
        Connection connection = null;
        try {
            connection = connectionProvider.getConnection(autoCommit);
            return connection.batchUpdate(sqls, sqlPreProcessor);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public int[] batchUpdate(String sql, final BatchPreparedStatementSetter psc) throws JpoException {
        Connection connection = null;
        try {
            sql = preProcessSql(sql);
            connection = connectionProvider.getConnection(autoCommit);
            return connection.batchUpdate(sql, psc);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public int[] batchUpdate(String sql, final Collection<Object[]> args) throws JpoException {
        Connection connection = null;
        try {
            sql = preProcessSql(sql);
            connection = connectionProvider.getConnection(autoCommit);
            Collection<Consumer<Statement>> statements = new ArrayList<>();
            args.forEach(array -> statements.add(new PrepareStatementSetterArrayWrapper(array)));
            return connection.batchUpdate(sql, statements);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public void execute(String sql) throws JpoException {
        Connection connection = null;
        try {
            sql = preProcessSql(sql);
            connection = connectionProvider.getConnection(autoCommit);
            connection.execute(sql);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    public <T> T query(String sql, final Collection<?> args, final Function<ResultSet, T> rse) throws JpoException {
        Connection connection = null;
        try {
            sql = preProcessSql(sql);
            connection = connectionProvider.getConnection(autoCommit);
            Consumer<Statement> pss = new PrepareStatementSetterCollectionWrapper(args);
            return connection.query(sql, pss, rse);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public void query(String sql, final Collection<?> args, final Consumer<ResultSet> rse) throws JpoException {
        query(sql, args, (resultSet) -> {
            rse.accept(resultSet);
            return null;
        });
    }

    @Override
    public <T> List<T> query(final String sql, final Collection<?> args, final BiFunction<ResultEntry, Integer, T> resultSetRowReader) throws JpoException {
        return query(sql, args, new ResultSetRowReaderToResultSetReader<T>(resultSetRowReader));
    }

    @Override
    public void query(final String sql, final Collection<?> args, final BiConsumer<ResultEntry, Integer> resultSetRowReader) throws JpoException {
        query(sql, args, (final ResultSet resultSet) -> {
            int rowNum = 0;
            while (resultSet.hasNext()) {
                ResultEntry entry = resultSet.next();
                resultSetRowReader.accept(entry, rowNum++);
            }
        });
    }

    @Override
    public <T> T query(String sql, final Object[] args, final Function<ResultSet, T> rse) throws JpoException {
        Connection connection = null;
        try {
            sql = preProcessSql(sql);
            connection = connectionProvider.getConnection(autoCommit);
            Consumer<Statement> pss = new PrepareStatementSetterArrayWrapper(args);
            return connection.query(sql, pss, rse);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public void query(String sql, final Object[] args, final Consumer<ResultSet> rse) throws JpoException {
        query(sql, args, (resultSet) -> {
            rse.accept(resultSet);
            return null;
        });
    }

    @Override
    public <T> List<T> query(final String sql, final Object[] args, final BiFunction<ResultEntry, Integer, T> resultSetRowReader) throws JpoException {
        return query(sql, args, new ResultSetRowReaderToResultSetReader<T>(resultSetRowReader));
    }

    @Override
    public void query(final String sql, final Object[] args, final BiConsumer<ResultEntry, Integer> resultSetRowReader) throws JpoException {
        query(sql, args, (final ResultSet resultSet) -> {
            int rowNum = 0;
            while (resultSet.hasNext()) {
                ResultEntry entry = resultSet.next();
                resultSetRowReader.accept(entry, rowNum++);
            }
        });
    }

    @Override
    public BigDecimal queryForBigDecimal(final String sql, final Collection<?> args) throws JpoException, JpoNotUniqueResultException {
        return this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
    }

    @Override
    public BigDecimal queryForBigDecimal(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
        return this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
    }

    @Override
    public final BigDecimal queryForBigDecimalUnique(final String sql, final Collection<?> args) throws JpoException, JpoNotUniqueResultException {
        return this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
    }

    @Override
    public final BigDecimal queryForBigDecimalUnique(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
        return this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
    }

    @Override
    public Boolean queryForBoolean(final String sql, final Collection<?> args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
        return BigDecimalUtil.toBoolean(result);
    }

    @Override
    public Boolean queryForBoolean(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
        return BigDecimalUtil.toBoolean(result);
    }

    @Override
    public final Boolean queryForBooleanUnique(final String sql, final Collection<?> args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
        return BigDecimalUtil.toBoolean(result);
    }

    @Override
    public final Boolean queryForBooleanUnique(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
        return BigDecimalUtil.toBoolean(result);
    }

    @Override
    public Double queryForDouble(final String sql, final Collection<?> args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
        return BigDecimalUtil.toDouble(result);
    }

    @Override
    public Double queryForDouble(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
        return BigDecimalUtil.toDouble(result);
    }

    @Override
    public final Double queryForDoubleUnique(final String sql, final Collection<?> args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
        return BigDecimalUtil.toDouble(result);
    }

    @Override
    public final Double queryForDoubleUnique(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
        return BigDecimalUtil.toDouble(result);
    }

    @Override
    public Float queryForFloat(final String sql, final Collection<?> args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
        return BigDecimalUtil.toFloat(result);
    }

    @Override
    public Float queryForFloat(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
        return BigDecimalUtil.toFloat(result);
    }

    @Override
    public final Float queryForFloatUnique(final String sql, final Collection<?> args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
        return BigDecimalUtil.toFloat(result);
    }

    @Override
    public final Float queryForFloatUnique(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
        return BigDecimalUtil.toFloat(result);
    }

    @Override
    public Integer queryForInt(final String sql, final Collection<?> args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
        return BigDecimalUtil.toInteger(result);
    }

    @Override
    public Integer queryForInt(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
        return BigDecimalUtil.toInteger(result);
    }

    @Override
    public final Integer queryForIntUnique(final String sql, final Collection<?> args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
        return BigDecimalUtil.toInteger(result);
    }

    @Override
    public final Integer queryForIntUnique(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
        return BigDecimalUtil.toInteger(result);
    }

    @Override
    public Long queryForLong(final String sql, final Collection<?> args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
        return BigDecimalUtil.toLong(result);
    }

    @Override
    public Long queryForLong(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL);
        return BigDecimalUtil.toLong(result);
    }

    @Override
    public final Long queryForLongUnique(final String sql, final Collection<?> args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
        return BigDecimalUtil.toLong(result);
    }

    @Override
    public final Long queryForLongUnique(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
        BigDecimal result = this.query(sql, args, RESULT_SET_READER_BIG_DECIMAL_UNIQUE);
        return BigDecimalUtil.toLong(result);
    }

    @Override
    public String queryForString(final String sql, final Collection<?> args) throws JpoException, JpoNotUniqueResultException {
        return this.query(sql, args, RESULT_SET_READER_STRING);
    }

    @Override
    public String queryForString(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
        return this.query(sql, args, RESULT_SET_READER_STRING);
    }

    @Override
    public final String queryForStringUnique(final String sql, final Collection<?> args) throws JpoException, JpoNotUniqueResultException {
        return this.query(sql, args, RESULT_SET_READER_STRING_UNIQUE);
    }

    @Override
    public final String queryForStringUnique(final String sql, final Object... args) throws JpoException, JpoNotUniqueResultException {
        return this.query(sql, args, RESULT_SET_READER_STRING_UNIQUE);
    }

    @Override
    public <T> T queryForUnique(final String sql, final Collection<?> args, final BiFunction<ResultEntry, Integer, T> rsrr) throws JpoException {
        return query(sql, args, new ResultSetRowReaderToResultSetReaderUnique<T>(rsrr));
    }

    @Override
    public <T> T queryForUnique(final String sql, final Object[] args, final BiFunction<ResultEntry, Integer, T> rsrr) throws JpoException, JpoNotUniqueResultException {
        return query(sql, args, new ResultSetRowReaderToResultSetReaderUnique<T>(rsrr));
    }

    @Override
    public int update(final String sql, final Collection<?> args) throws JpoException {
        Consumer<Statement> pss = new PrepareStatementSetterCollectionWrapper(args);
        return update(sql, pss);
    }

    @Override
    public <R> R update(final String sql, final Collection<?> args, final GeneratedKeyReader<R> generatedKeyReader) throws JpoException {
        Consumer<Statement> pss = new PrepareStatementSetterCollectionWrapper(args);
        return update(sql, pss, generatedKeyReader);
    }

    @Override
    public int update(final String sql, final Object... args) throws JpoException {
        Consumer<Statement> pss = new PrepareStatementSetterArrayWrapper(args);
        return update(sql, pss);
    }

    @Override
    public <R> R update(final String sql, final Object[] args, final GeneratedKeyReader<R> generatedKeyReader) throws JpoException {
        Consumer<Statement> pss = new PrepareStatementSetterArrayWrapper(args);
        return update(sql, pss, generatedKeyReader);
    }

    @Override
    public int update(String sql, final Consumer<Statement> psc) throws JpoException {
        Connection connection = null;
        try {
            sql = preProcessSql(sql);
            connection = connectionProvider.getConnection(autoCommit);
            return connection.update(sql, psc);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public <R> R update(String sql, final Consumer<Statement> psc, final GeneratedKeyReader<R> generatedKeyReader) throws JpoException {
        Connection connection = null;
        try {
            sql = preProcessSql(sql);
            connection = connectionProvider.getConnection(autoCommit);
            return connection.update(sql, generatedKeyReader, psc);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public <T> Optional<T> queryForOptional(String sql, Collection<?> args, BiFunction<ResultEntry, Integer, T> rsrr) throws JpoException {
        T result = query(sql, args, rs -> {
           if (rs.hasNext()) {
               return rsrr.apply(rs.next(), 0);
           }
           return null;
        });
        return Optional.ofNullable(result);
    }

    @Override
    public <T> Optional<T> queryForOptional(String sql, Object[] args, BiFunction<ResultEntry, Integer, T> rsrr) throws JpoException {
        T result = query(sql, args, rs -> {
            if (rs.hasNext()) {
                return rsrr.apply(rs.next(), 0);
            }
            return null;
         });
         return Optional.ofNullable(result);
    }

    private String preProcessSql(String sql) {
        return sqlPreProcessor.apply(sql);
    }

}