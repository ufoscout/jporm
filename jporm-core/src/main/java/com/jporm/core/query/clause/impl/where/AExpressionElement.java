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
package com.jporm.core.query.clause.impl.where;

import java.util.Collection;
import java.util.List;

import com.jporm.core.query.AQuerySubElement;
import com.jporm.core.query.clause.WhereExpressionElement;
import com.jporm.core.query.namesolver.NameSolver;

/**
 * 
 * @author Francesco Cina
 *
 * 19/giu/2011
 */
//TODO to refactor!!
public abstract class AExpressionElement extends AQuerySubElement implements WhereExpressionElement {

    private String property;
    private boolean singleValue;
    private Object value;
    private boolean multipleValues;
    private Collection<?> expressionValues;


    private PropertyDecorator propertyDecorator = new NullPropertyDecorator();
    private PropertyDecorator valueDecorator = new NullPropertyDecorator();
    private int elementStatusVersion;

    public final boolean hasValue() {
        return (singleValue);
    }

    public final boolean hasValues() {
        return (multipleValues && (expressionValues!=null) && (expressionValues.size()>0));
    }

    public Object getValue() {
        return value;
    }

    public void setValue(final Object value) {
        elementStatusVersion++;
        singleValue = true;
        multipleValues = false;
        this.value = value;
    }

    public Collection<?> getValues() {
        return expressionValues;
    }

    public void setValues(final Collection<?> values) {
        elementStatusVersion++;
        singleValue = false;
        multipleValues = true;
        expressionValues = values;
    }

    public final String getProperty() {
        return property;
    }

    public void setProperty(final String property) {
        elementStatusVersion++;
        this.property = property;
    }

    protected PropertyDecorator getPropertyDecorator() {
        return propertyDecorator;
    }

    protected void setPropertyDecorator(final PropertyDecorator propertyDecorator) {
        elementStatusVersion++;
        this.propertyDecorator = propertyDecorator;
    }

    protected PropertyDecorator getValueDecorator() {
        return valueDecorator;
    }

    protected void setValueDecorator(final PropertyDecorator valueDecorator) {
        elementStatusVersion++;
        this.valueDecorator = valueDecorator;
    }

    @Override
    public final void renderSqlElement(final StringBuilder query, final NameSolver nameSolver) {
        getPropertyDecorator().decore( nameSolver.solvePropertyName(getProperty()) , query);
        query.append( " " ); //$NON-NLS-1$
        query.append( getExpressionElementKey() );
        query.append( " " ); //$NON-NLS-1$
        appendQuestionMarks(query);
    }

    private void appendQuestionMarks(final StringBuilder query) {
        if (hasValue()) {
            getValueDecorator().decore("?", query); //$NON-NLS-1$
            query.append(" "); //$NON-NLS-1$
            return;
        }
        if (hasValues()) {
            query.append( "( "); //$NON-NLS-1$
            for (int i=0; i<(getValues().size()-1); i++) {
                getValueDecorator().decore("?", query); //$NON-NLS-1$ 
                query.append(", "); //$NON-NLS-1$
            }
            getValueDecorator().decore("?", query) ; //$NON-NLS-1$ 
            query.append(" ) "); //$NON-NLS-1$
        }
    }

    public abstract String getExpressionElementKey();

    @Override
    public final void appendElementValues(final List<Object> values) {
        if (hasValue()) {
            values.add(value);
        }
        if (hasValues()) {
            values.addAll( expressionValues );
        }
    }

    @Override
    public final int getElementStatusVersion() {
        return elementStatusVersion;
    }
}
