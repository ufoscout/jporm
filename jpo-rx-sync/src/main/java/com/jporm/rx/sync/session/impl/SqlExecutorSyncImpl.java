package com.jporm.rx.sync.session.impl;

import co.paralleluniverse.fibers.Suspendable;
import com.jporm.rx.connection.UpdateResult;
import com.jporm.rx.session.SqlExecutor;
import com.jporm.rx.sync.quasar.JpoCompletableWrapper;
import com.jporm.rx.sync.session.SqlExecutorSync;
import com.jporm.sql.dialect.DBType;
import com.jporm.types.io.GeneratedKeyReader;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;
import com.jporm.types.io.StatementSetter;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * Created by ufo on 26/07/15.
 */
@Suspendable
public class SqlExecutorSyncImpl implements SqlExecutorSync {

    private SqlExecutor sqlExecutor;

    public SqlExecutorSyncImpl(SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    @Override
    public <T> T query(String sql, ResultSetReader<T> rse, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.query(sql, rse, args));
    }

    @Override
    public <T> T query(String sql, ResultSetReader<T> rse, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.query(sql, rse, args));
    }

    @Override
    public <T> List<T> query(String sql, ResultSetRowReader<T> rsrr, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.query(sql, rsrr, args));
    }

    @Override
    public <T> List<T> query(String sql, ResultSetRowReader<T> rsrr, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.query(sql, rsrr, args));
    }

    @Override
    public BigDecimal queryForBigDecimal(String sql, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForBigDecimal(sql, args));
    }

    @Override
    public BigDecimal queryForBigDecimal(String sql, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForBigDecimal(sql, args));
    }

    @Override
    public BigDecimal queryForBigDecimalUnique(String sql, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForBigDecimalUnique(sql, args));
    }

    @Override
    public BigDecimal queryForBigDecimalUnique(String sql, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForBigDecimalUnique(sql, args));
    }

    @Override
    public Boolean queryForBoolean(String sql, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForBoolean(sql, args));
    }

    @Override
    public Boolean queryForBoolean(String sql, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForBoolean(sql, args));
    }

    @Override
    public Boolean queryForBooleanUnique(String sql, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForBooleanUnique(sql, args));
    }

    @Override
    public Boolean queryForBooleanUnique(String sql, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForBooleanUnique(sql, args));
    }

    @Override
    public Double queryForDouble(String sql, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForDouble(sql, args));
    }

    @Override
    public Double queryForDouble(String sql, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForDouble(sql, args));
    }

    @Override
    public Double queryForDoubleUnique(String sql, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForDoubleUnique(sql, args));
    }

    @Override
    public Double queryForDoubleUnique(String sql, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForDoubleUnique(sql, args));
    }

    @Override
    public Float queryForFloat(String sql, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForFloat(sql, args));
    }

    @Override
    public Float queryForFloat(String sql, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForFloat(sql, args));
    }

    @Override
    public Float queryForFloatUnique(String sql, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForFloatUnique(sql, args));
    }

    @Override
    public Float queryForFloatUnique(String sql, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForFloatUnique(sql, args));
    }

    @Override
    public Integer queryForInt(String sql, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForInt(sql, args));
    }

    @Override
    public Integer queryForInt(String sql, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForInt(sql, args));
    }

    @Override
    public Integer queryForIntUnique(String sql, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForIntUnique(sql, args));
    }

    @Override
    public Integer queryForIntUnique(String sql, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForIntUnique(sql, args));
    }

    @Override
    public Long queryForLong(String sql, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForLong(sql, args));
    }

    @Override
    public Long queryForLong(String sql, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForLong(sql, args));
    }

    @Override
    public Long queryForLongUnique(String sql, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForLongUnique(sql, args));
    }

    @Override
    public Long queryForLongUnique(String sql, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForLongUnique(sql, args));
    }

    @Override
    public String queryForString(String sql, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForString(sql, args));
    }

    @Override
    public String queryForString(String sql, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForString(sql, args));
    }

    @Override
    public String queryForStringUnique(String sql, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForStringUnique(sql, args));
    }

    @Override
    public String queryForStringUnique(String sql, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForStringUnique(sql, args));
    }

    @Override
    public <T> T queryForUnique(String sql, ResultSetRowReader<T> rsrr, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForUnique(sql, rsrr, args));
    }

    @Override
    public <T> T queryForUnique(String sql, ResultSetRowReader<T> rsrr, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.queryForUnique(sql, rsrr, args));
    }

    @Override
    public UpdateResult update(String sql, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.update(sql, args));
    }

    @Override
    public UpdateResult update(String sql, GeneratedKeyReader generatedKeyReader, Collection<?> args) {
        return JpoCompletableWrapper.get(sqlExecutor.update(sql, generatedKeyReader, args));
    }

    @Override
    public UpdateResult update(String sql, GeneratedKeyReader generatedKeyReader, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.update(sql, generatedKeyReader, args));
    }

    @Override
    public UpdateResult update(String sql, GeneratedKeyReader generatedKeyReader, StatementSetter psc) {
        return JpoCompletableWrapper.get(sqlExecutor.update(sql, generatedKeyReader, psc));
    }

    @Override
    public UpdateResult update(String sql, Object... args) {
        return JpoCompletableWrapper.get(sqlExecutor.update(sql, args));
    }

    @Override
    public UpdateResult update(String sql, StatementSetter psc) {
        return JpoCompletableWrapper.get(sqlExecutor.update(sql, psc));
    }

    @Override
    public DBType dbType() {
        return JpoCompletableWrapper.get(sqlExecutor.dbType());
    }
}
