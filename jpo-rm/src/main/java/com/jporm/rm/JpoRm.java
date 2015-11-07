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
package com.jporm.rm;

import com.jporm.rm.session.Session;
import com.jporm.rm.transaction.Transaction;
import com.jporm.types.TypeConverter;
import com.jporm.types.TypeConverterBuilder;

/**
 *
 * @author Francesco Cina
 *
 *         21/mag/2011
 */
public interface JpoRm {

    /**
     * Register a new {@link TypeConverter}. If a {@link TypeConverter} wraps a
     * Class that is already mapped, the last registered {@link TypeConverter}
     * will be used.
     *
     * @param typeConverter
     */
    void register(final TypeConverter<?, ?> typeWrapper);

    /**
     * Register a new {@link TypeConverterBuilder}. If a {@link TypeConverter}
     * wraps a Class that is already mapped, the last registered
     * {@link TypeConverter} will be used.
     *
     * @param typeConverterBuilder
     */
    void register(final TypeConverterBuilder<?, ?> typeWrapperBuilder);

    /**
     * Return a {@link Session} from the current {@link JpoRm} implementation
     * 
     * @return
     */
    Session session();

    /**
     * Returns a new {@link Transaction} instance.
     * 
     * @return
     */
    Transaction transaction();

}
