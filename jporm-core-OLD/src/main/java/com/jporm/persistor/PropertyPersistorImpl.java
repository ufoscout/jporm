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
package com.jporm.persistor;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jporm.mapper.clazz.ClassField;
import com.jporm.persistor.type.TypeWrapperJdbcReady;
import com.jporm.persistor.version.VersionMath;

/**
 * 
 * @author ufo
 *
 * @param <BEAN> the type of the bean to manipulate
 * @param <P> the type of the bean's property to manipulate
 * @param <DB> the type of the field in the {@link PreparedStatement} and {@link ResultSet}
 */
public class PropertyPersistorImpl<BEAN, P, DB> implements PropertyPersistor<BEAN, P, DB> {

    private final TypeWrapperJdbcReady<P, DB> typeWrapper;
    private final VersionMath<P> math;
    private final ClassField<BEAN, P> classField;

    public PropertyPersistorImpl (final TypeWrapperJdbcReady<P, DB> typeWrapper,
            final ClassField<BEAN, P> classField, final VersionMath<P> math) {
        this.typeWrapper = typeWrapper;
        this.classField = classField;
        this.math = math;

    }

    /**
     * Extract the value from a {@link ResultSet} and set it to a bean's property
     * @param bean
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws SQLException
     */
    @Override
    public void getFromResultSet(final BEAN bean, final ResultSet rs) throws IllegalArgumentException, SQLException {
        this.setPropertyValueToBean( bean, getValueFromResultSet(rs, this.classField.getFieldName()) );
    }

    /**
     * Extract the value from a {@link ResultSet} and set it to a bean's property
     * @param bean
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws SQLException
     */
    @Override
    public void getFromResultSet(final BEAN bean, final ResultSet rs, final int rsColumnIndex) throws IllegalArgumentException, SQLException {
        this.setPropertyValueToBean( bean, this.typeWrapper.wrap(this.typeWrapper.getJdbcIO().getValueFromResultSet(rs, rsColumnIndex) ) );
    }

    @Override
    public P getValueFromResultSet(final ResultSet rs, final String fieldName)
            throws IllegalArgumentException, SQLException {
        return this.typeWrapper.wrap(this.typeWrapper.getJdbcIO().getValueFromResultSet(rs, fieldName ));
    }

    /**
     * Set copy the property value from source to destination
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    @Override
    public void clonePropertyValue(final BEAN source, final BEAN destination) throws IllegalArgumentException {
        this.setPropertyValueToBean(destination, this.typeWrapper.clone(this.getPropertyValueFromBean(source)));
    }

    @Override
    public void increaseVersion(final BEAN bean, final boolean firstVersionNumber) throws IllegalArgumentException {
        this.setPropertyValueToBean(bean, this.math.increase(firstVersionNumber, this.getPropertyValueFromBean(bean)));
    }

    //    private void setDBValueToBean(final BEAN bean, final DB value) throws IllegalArgumentException {
    //        this.setPropertyValueToBean(bean, this.typeWrapper.wrap(value));
    //    }

    @Override
    public P getPropertyValueFromBean(final BEAN bean) throws IllegalArgumentException {
        return this.classField.getGetManipulator().getValue(bean);
    }

    private void setPropertyValueToBean(final BEAN bean, final P value) throws IllegalArgumentException {
        this.classField.getSetManipulator().setValue(bean, value);
    }

    @Override
    public Class<P> propertyType(){
        return this.typeWrapper.propertyType();
    }

    @Override
    public boolean isInnerRelation() {
        return false;
    }

}
