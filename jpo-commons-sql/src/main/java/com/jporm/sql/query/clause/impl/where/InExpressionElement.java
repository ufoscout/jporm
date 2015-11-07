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
package com.jporm.sql.query.clause.impl.where;

import java.util.Arrays;
import java.util.Collection;

/**
 * 
 * @author Francesco Cina
 *
 *         19/giu/2011
 */
public class InExpressionElement extends AExpressionElement {

    public InExpressionElement(final String property, final Collection<?> values) {
        setProperty(property);
        setValues(values);
    }

    public InExpressionElement(final String property, final Object... values) {
        this(property, Arrays.asList(values));
    }

    @Override
    public String getExpressionElementKey() {
        return "in"; //$NON-NLS-1$
    }

}
