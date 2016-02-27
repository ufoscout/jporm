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
package com.jporm.sql.dsl.query.where.expression;

import java.util.Collection;
import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.SqlSubElement;
import com.jporm.sql.dsl.query.processor.PropertiesProcessor;
import com.jporm.sql.dsl.query.where.WhereExpressionElement;

/**
 *
 * @author Francesco Cina
 *
 *         19/giu/2011
 */
// TODO to refactor!!
public abstract class AExpressionElement implements WhereExpressionElement, SqlSubElement {

    private String property;
    private boolean singleValue;
    private Object value;
    private boolean multipleValues;
    private Collection<?> expressionValues;

    private PropertyDecorator propertyDecorator = new NullPropertyDecorator();
    private PropertyDecorator valueDecorator = new NullPropertyDecorator();

    @Override
    public final void sqlElementValues(final List<Object> values) {
        if (hasValue()) {
            values.add(value);
        }
        if (hasValues()) {
            values.addAll(expressionValues);
        }
    }

    private void appendQuestionMarks(final StringBuilder query) {
        if (hasValue()) {
            getValueDecorator().decore("?", query); //$NON-NLS-1$
            query.append(" "); //$NON-NLS-1$
            return;
        }
        if (hasValues()) {
            query.append("( "); //$NON-NLS-1$
            for (int i = 0; i < (getValues().size() - 1); i++) {
                getValueDecorator().decore("?", query); //$NON-NLS-1$
                query.append(", "); //$NON-NLS-1$
            }
            getValueDecorator().decore("?", query); //$NON-NLS-1$
            query.append(" ) "); //$NON-NLS-1$
        }
    }

    public abstract String getExpressionElementKey();

    public final String getProperty() {
        return property;
    }

    protected PropertyDecorator getPropertyDecorator() {
        return propertyDecorator;
    }

    public Object getValue() {
        return value;
    }

    protected PropertyDecorator getValueDecorator() {
        return valueDecorator;
    }

    public Collection<?> getValues() {
        return expressionValues;
    }

    public final boolean hasValue() {
        return (singleValue);
    }

    public final boolean hasValues() {
        return (multipleValues && (expressionValues != null) && (expressionValues.size() > 0));
    }

    @Override
    public final void sqlElementQuery(final StringBuilder query, final DBProfile dbProfile, final PropertiesProcessor nameSolver) {
        getPropertyDecorator().decore(nameSolver.solvePropertyName(getProperty()), query);
        query.append(" "); //$NON-NLS-1$
        query.append(getExpressionElementKey());
        query.append(" "); //$NON-NLS-1$
        appendQuestionMarks(query);
    }

    public void setProperty(final String property) {
        this.property = property;
    }

    protected void setPropertyDecorator(final PropertyDecorator propertyDecorator) {
        this.propertyDecorator = propertyDecorator;
    }

    public void setValue(final Object value) {
        singleValue = true;
        multipleValues = false;
        this.value = value;
    }

    protected void setValueDecorator(final PropertyDecorator valueDecorator) {
        this.valueDecorator = valueDecorator;
    }

    public void setValues(final Collection<?> values) {
        singleValue = false;
        multipleValues = true;
        expressionValues = values;
    }

}
