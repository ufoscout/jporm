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
 *          ON : Mar 3, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.core.persistor;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.jporm.core.mapper.clazz.ClassField;
import com.jporm.exception.OrmConfigurationException;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 3, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class PropertyPeristorDecorator<DECORATED_BEAN, BEAN, P, DB> implements PropertyPersistor<BEAN, P, DB> {

    private PropertyPersistor<DECORATED_BEAN, P, DB> decoratedPropertyPersistor;
    private ClassField<BEAN, DECORATED_BEAN> classField;

    public PropertyPeristorDecorator(final PropertyPersistor<DECORATED_BEAN, P, DB> decoratedPropertyPersistor,
            final ClassField<BEAN, DECORATED_BEAN> classField) {
        this.decoratedPropertyPersistor = decoratedPropertyPersistor;
        this.classField = classField;

    }

    @Override
    public Class<P> propertyType() {
        return decoratedPropertyPersistor.propertyType();
    }

    @Override
    public P getPropertyValueFromBean(final BEAN bean) throws IllegalArgumentException {
        DECORATED_BEAN value = classField.getGetManipulator().getValue(bean);
        if (value!=null) {
            return decoratedPropertyPersistor.getPropertyValueFromBean(value);
        }
        return null;
    }

    @Override
    public void increaseVersion(final BEAN bean, final boolean firstVersionNumber)
            throws IllegalArgumentException {
        throw new OrmConfigurationException("Operation not permitted!"); //$NON-NLS-1$

    }

    @Override
    public void clonePropertyValue(final BEAN source, final BEAN destination)
            throws IllegalArgumentException {
        this.classField.getSetManipulator().setValue(destination, this.classField.getGetManipulator().getValue(source));
    }

    @Override
    public void getFromResultSet(final BEAN bean, final ResultSet rs, final int rsColumnIndex)
            throws IllegalArgumentException, SQLException {
        throw new OrmConfigurationException("Operation not permitted!"); //$NON-NLS-1$
    }

    @Override
    public void getFromResultSet(final BEAN bean, final ResultSet rs)
            throws IllegalArgumentException, SQLException {
        throw new OrmConfigurationException("Operation not permitted!"); //$NON-NLS-1$
    }

    @Override
    public boolean isInnerRelation() {
        return true;
    }

    @Override
    public P getValueFromResultSet(final ResultSet rs, final String fieldName) throws IllegalArgumentException, SQLException {
        return decoratedPropertyPersistor.getValueFromResultSet(rs, fieldName);
    }

}
