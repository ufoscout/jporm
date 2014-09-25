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
package com.jporm.core.persistor.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.jporm.exception.OrmConfigurationException;
import com.jporm.exception.OrmException;

/**
 * 
 * Get the value of a field directly accessing his value
 * 
 * @author Francesco Cina'
 *
 * Mar 31, 2012
 */
public class FieldSetManipulator<BEAN, P> extends SetManipulator<BEAN, P> {

    private final Field field;

    public FieldSetManipulator(final Field field) {
        this.field = field;
        field.setAccessible(true);

        if (Modifier.isFinal(field.getModifiers()) ) {
            throw new OrmConfigurationException("Field [" + field.getName() + "] of class [" + field.getDeclaringClass() + "] is marked FINAL. His value cannot be managed by JPOrm. Please remove the 'final' modifier."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

    }

    @Override
    public void setValue(final BEAN bean, final P value) {
        try {
            this.field.set(bean, value);
        } catch (Exception e) {
            throw new OrmException(e);
        }
    }

}
