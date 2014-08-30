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
package com.jporm.session;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.dialect.querytemplate.QueryTemplate;
import com.jporm.exception.OrmException;
import com.jporm.persistor.type.TypeFactory;
import com.jporm.persistor.type.TypeWrapperJdbcReady;

/**
 * 
 * @author Francesco Cina'
 *
 * Dec 20, 2011
 * 
 * The implementations of this class MUST be state less and Thread safe.
 * 
 */
public abstract class SqlPerformerStrategy {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract int[] batchUpdate(List<String> sqls, int timeout) throws OrmException;

    protected abstract int[] batchUpdate(String sql, BatchPreparedStatementSetter psc, int timeout) throws OrmException;

    protected abstract int[] batchUpdate(String sql, List<Object[]> args, int timeout) throws OrmException;

    protected abstract void execute(String sql, int timeout) throws OrmException;

    protected final Logger getLogger() {
        return logger;
    }

    protected abstract <T> T query(String sql, int timeout, int maxRows, final PreparedStatementSetter pss, ResultSetReader<T> rse) throws OrmException ;

    public final <T> T query(final String sql, final ResultSetReader<T> rse, final int queryTimeout, final int maxRows, final Collection<?> args, final TypeFactory typeFactory) throws OrmException {
        PreparedStatementSetter pss = new PrepareStatementSetterCollectionWrapper(args, typeFactory);
        return query(sql, queryTimeout, maxRows, pss, rse);
    }

    public final <T> T query(final String sql, final ResultSetReader<T> rse, final int queryTimeout, final int maxRows, final Object[] args, final TypeFactory typeFactory) throws OrmException {
        PreparedStatementSetter pss = new PrepareStatementSetterArrayWrapper(args, typeFactory);
        return query(sql, queryTimeout, maxRows, pss, rse);
    }

    /**
     * @param sql
     * @param queryTimeout
     * @param args
     * @return
     */
    public final int update(final String sql, final int queryTimeout, final Collection<?> args, final TypeFactory typeFactory) {
        PreparedStatementSetter pss = new PrepareStatementSetterCollectionWrapper(args, typeFactory);
        return this.update(sql, queryTimeout, pss);
    }

    /**
     * @param sql
     * @param queryTimeout
     * @param generatedKeyReader
     * @param args
     * @return
     */
    public final int update(final String sql, final int queryTimeout, final GeneratedKeyReader generatedKeyReader, final QueryTemplate queryTemplate, final Collection<?> args, final TypeFactory typeFactory) {
        PreparedStatementSetter pss = new PrepareStatementSetterCollectionWrapper(args, typeFactory);
        return this.update(sql, queryTimeout, generatedKeyReader, queryTemplate, pss);
    }

    /**
     * @param sql
     * @param queryTimeout
     * @param generatedKeyReader
     * @param args
     * @return
     */
    public final int update(final String sql, final int queryTimeout, final GeneratedKeyReader generatedKeyReader, final QueryTemplate queryTemplate, final Object[] args, final TypeFactory typeFactory) {
        PreparedStatementSetter pss = new PrepareStatementSetterArrayWrapper(args, typeFactory);
        return this.update(sql, queryTimeout, generatedKeyReader, queryTemplate, pss);
    }

    protected abstract int update(String sql, int timeout, GeneratedKeyReader generatedKeyReader, QueryTemplate queryTemplate, final PreparedStatementSetter pss) throws OrmException;

    /**
     * @param sql
     * @param queryTimeout
     * @param args
     * @return
     */
    public final int update(final String sql, final int queryTimeout, final Object[] args, final TypeFactory typeFactory) {
        PreparedStatementSetter pss = new PrepareStatementSetterArrayWrapper(args, typeFactory);
        return this.update(sql, queryTimeout, pss);
    }


    protected abstract int update(String sql, int timeout, final PreparedStatementSetter pss) throws OrmException;


    class PrepareStatementSetterArrayWrapper implements PreparedStatementSetter {
        private Object[] args;
        private TypeFactory typeFactory;

        public PrepareStatementSetterArrayWrapper(final Object[] args, final TypeFactory typeFactory) {
            this.args = args;
            this.typeFactory = typeFactory;
        }

        @Override
        public void set(final PreparedStatement ps) throws SQLException {
            if (logger.isDebugEnabled()) {
                logger.debug("Query params: " + Arrays.asList(args)); //$NON-NLS-1$
            }
            int index = 0;
            for (Object object : args) {
                if (object!=null) {
                    TypeWrapperJdbcReady<Object, Object> typeWrapper = (TypeWrapperJdbcReady<Object, Object>) typeFactory.getTypeWrapper(object.getClass());
                    typeWrapper.getJdbcIO().setValueToPreparedStatement( typeWrapper.unWrap(object) , ps, ++index);
                } else {
                    ps.setObject(++index, object);
                }
            }
        }
    }

    class PrepareStatementSetterCollectionWrapper implements PreparedStatementSetter {

        private Collection<?> args;
        private TypeFactory typeFactory;

        public PrepareStatementSetterCollectionWrapper(final Collection<?> args, final TypeFactory typeFactory) {
            this.args = args;
            this.typeFactory = typeFactory;
        }

        @Override
        public void set(final PreparedStatement ps) throws SQLException {
            if (logger.isDebugEnabled()) {
                logger.debug("Query params: " + args); //$NON-NLS-1$
            }
            int index = 0;
            for (Object object : args) {
                if (object!=null) {
                    TypeWrapperJdbcReady<Object, Object> typeWrapper = (TypeWrapperJdbcReady<Object, Object>) typeFactory.getTypeWrapper(object.getClass());
                    typeWrapper.getJdbcIO().setValueToPreparedStatement( typeWrapper.unWrap(object) , ps, ++index);
                } else {
                    ps.setObject(++index, object);
                }
            }
        }

    }
}
