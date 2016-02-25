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

import java.util.List;

import com.jporm.sql.dsl.dialect.DBProfile;
import com.jporm.sql.dsl.query.ASqlSubElement;
import com.jporm.sql.dsl.query.processor.PropertiesProcessor;
import com.jporm.sql.dsl.query.where.WhereExpressionElement;

/**
 *
 * @author Francesco Cina
 *
 *         19/giu/2011
 */
public abstract class APropertiesExpressionElement extends ASqlSubElement implements WhereExpressionElement {

    private final String firstProperty;
    private final String secondProperty;

    private PropertyDecorator propertyDecorator = new NullPropertyDecorator();

    private PropertyDecorator valueDecorator = new NullPropertyDecorator();

    public APropertiesExpressionElement(final String firstProperty, final String secondProperty) {
        this.firstProperty = firstProperty;
        this.secondProperty = secondProperty;
    }

    @Override
    public final void sqlElementValues(final List<Object> values) {
        // do nothing
    }

    public abstract String getExpressionElementKey();

    protected PropertyDecorator getPropertyDecorator() {
        return propertyDecorator;
    }

    protected PropertyDecorator getValueDecorator() {
        return valueDecorator;
    }

    @Override
    public final void sqlElementQuery(final StringBuilder query, final DBProfile dbProfile, final PropertiesProcessor nameSolver) {
        getPropertyDecorator().decore(nameSolver.solvePropertyName(firstProperty), query);
        query.append(" "); //$NON-NLS-1$
        query.append(getExpressionElementKey());
        query.append(" "); //$NON-NLS-1$
        getPropertyDecorator().decore(nameSolver.solvePropertyName(secondProperty), query);
        query.append(" "); //$NON-NLS-1$
    }

    protected void setPropertyDecorator(final PropertyDecorator propertyDecorator) {
        this.propertyDecorator = propertyDecorator;
    }

    protected void setValueDecorator(final PropertyDecorator valueDecorator) {
        this.valueDecorator = valueDecorator;
    }

}
