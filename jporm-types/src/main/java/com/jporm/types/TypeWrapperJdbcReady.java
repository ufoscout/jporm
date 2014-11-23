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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Mar 2, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.core.persistor.type;

import com.jporm.wrapper.TypeWrapper;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 2, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class TypeWrapperJdbcReady<P, DB> implements TypeWrapper<P, DB> {

    private final TypeWrapper<P, DB> typeWrapper;
    private final JdbcIO<DB> jdbcIO;

    TypeWrapperJdbcReady(final TypeWrapper<P, DB> typeWrapper, final JdbcIO<DB> jdbcIO) {
        this.typeWrapper = typeWrapper;
        this.jdbcIO = jdbcIO;
    }

    /**
     * @return
     * @see com.jporm.wrapper.TypeWrapper#jdbcType()
     */
    @Override
    public Class<DB> jdbcType() {
        return getTypeWrapper().jdbcType();
    }

    /**
     * @return
     * @see com.jporm.wrapper.TypeWrapper#propertyType()
     */
    @Override
    public Class<P> propertyType() {
        return getTypeWrapper().propertyType();
    }

    /**
     * @param value
     * @return
     * @see com.jporm.wrapper.TypeWrapper#wrap(java.lang.Object)
     */
    @Override
    public P wrap(final DB value) {
        return getTypeWrapper().wrap(value);
    }

    /**
     * @param value
     * @return
     * @see com.jporm.wrapper.TypeWrapper#unWrap(java.lang.Object)
     */
    @Override
    public DB unWrap(final P value) {
        return getTypeWrapper().unWrap(value);
    }

    /**
     * @param source
     * @return
     * @see com.jporm.wrapper.TypeWrapper#clone(java.lang.Object)
     */
    @Override
    public P clone(final P source) {
        return getTypeWrapper().clone(source);
    }

    public JdbcIO<DB> getJdbcIO() {
        return jdbcIO;
    }

    public TypeWrapper<P, DB> getTypeWrapper() {
        return typeWrapper;
    }

}
