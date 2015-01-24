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
package com.jporm.core.query.clause.where;

import java.util.List;

import com.jporm.core.query.AQuerySubElement;
import com.jporm.exception.OrmQueryFormatException;
import com.jporm.query.clause.WhereExpressionElement;
import com.jporm.query.namesolver.NameSolver;

/**
 * 
 * @author Francesco Cina
 *
 * 19/giu/2011
 */
public abstract class APropertiesExpressionElement extends AQuerySubElement implements WhereExpressionElement {

    private final String firstProperty;
    private final String secondProperty;

    public APropertiesExpressionElement(final String firstProperty, final String secondProperty) {
        this.firstProperty = firstProperty;
        this.secondProperty = secondProperty;
    }

    private PropertyDecorator propertyDecorator = new NullPropertyDecorator();
    private PropertyDecorator valueDecorator = new NullPropertyDecorator();
    private int elementStatusVersion;

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
    public final void renderSqlElement(final StringBuilder query, final NameSolver nameSolver) throws OrmQueryFormatException {
        getPropertyDecorator().decore( nameSolver.solvePropertyName(firstProperty) , query );
        query.append( " " ); //$NON-NLS-1$
        query.append( getExpressionElementKey() );
        query.append( " " ); //$NON-NLS-1$
        getPropertyDecorator().decore( nameSolver.solvePropertyName(secondProperty), query );
        query.append( " " ); //$NON-NLS-1$
    }

    public abstract String getExpressionElementKey();


    @Override
    public final void appendElementValues(final List<Object> values) {
        // do nothing
    }

    @Override
    public final int getElementStatusVersion() {
        return elementStatusVersion;
    }
}
