/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.commons.core.session;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;

import com.jporm.commons.core.io.BigDecimalResultSetReader;
import com.jporm.commons.core.io.BigDecimalResultSetReaderUnique;
import com.jporm.commons.core.io.StringResultSetReader;
import com.jporm.commons.core.io.StringResultSetReaderUnique;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.TypeConverterJdbcReady;
import com.jporm.types.io.ResultSet;
import com.jporm.types.io.Statement;

/**
 * @author Francesco Cina 02/lug/2011
 */
public abstract class ASqlExecutor {

    protected class PrepareStatementSetterArrayWrapper implements Consumer<Statement> {
        private final Object[] args;

        public PrepareStatementSetterArrayWrapper(final Object[] args) {
            this.args = args;
        }

        @Override
        public void accept(final Statement ps) {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug("Query params: " + Arrays.asList(args)); //$NON-NLS-1$
            }
            int index = 0;
            for (Object object : args) {
                setToStatement(index++, object, ps);
            }
        }
    }

    protected class PrepareStatementSetterCollectionWrapper implements Consumer<Statement> {

        private final Collection<?> args;

        public PrepareStatementSetterCollectionWrapper(final Collection<?> args) {
            this.args = args;
        }

        @Override
        public void accept(final Statement ps) {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug("Query params: " + args); //$NON-NLS-1$
            }
            int index = 0;
            for (Object object : args) {
                setToStatement(index++, object, ps);
            }
        }

    }

    protected static final Function<ResultSet, String> RESULT_SET_READER_STRING_UNIQUE = new StringResultSetReaderUnique();
    protected static final Function<ResultSet, String> RESULT_SET_READER_STRING = new StringResultSetReader();

    protected static final Function<ResultSet, BigDecimal> RESULT_SET_READER_BIG_DECIMAL_UNIQUE = new BigDecimalResultSetReaderUnique();

    protected static final Function<ResultSet, BigDecimal> RESULT_SET_READER_BIG_DECIMAL = new BigDecimalResultSetReader();

    private final TypeConverterFactory typeFactory;

    /**
     * @param sqlPerformerStrategy2
     * @param serviceCatalog
     */
    public ASqlExecutor(final TypeConverterFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    protected abstract Logger getLogger();

    /**
     * @return the typeFactory
     */
    public TypeConverterFactory getTypeFactory() {
        return typeFactory;
    }

    @SuppressWarnings("unchecked")
    protected void setToStatement(final int index, final Object value, final Statement statement) {
        if (value != null) {
			TypeConverterJdbcReady<Object, Object> typeWrapper = (TypeConverterJdbcReady<Object, Object>) getTypeFactory().getTypeConverter(value.getClass());
            typeWrapper.getJdbcIO().setValueToPreparedStatement(typeWrapper.toJdbcType(value), statement, index);
        } else {
            statement.setObject(index, value);
        }
    }
}
