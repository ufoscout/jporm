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
 *          ON : Feb 18, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.mapper.relation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jporm.exception.OrmConfigurationException;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 18, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class ClassFKMapImpl<BEAN> implements ClassFKMap<BEAN> {

    private final Map<Class<?>, ForeignKey<?>> foreignKeys = new HashMap<Class<?>, ForeignKey<?>>();
    private final Class<?> mappedClass;

    public ClassFKMapImpl(final Class<?> mappedClass) {
        this.mappedClass = mappedClass;
    }

    @Override
    public <VERSUS_CLASS> boolean hasFKVersus(final Class<VERSUS_CLASS> versusClass) {
        boolean contains = foreignKeys.containsKey(versusClass);
        if (!contains) {
            checkAssignableFor(versusClass);
            contains = foreignKeys.containsKey(versusClass);
        }
        return contains;
    }

    @Override
    public <VERSUS_CLASS> ForeignKey<VERSUS_CLASS> versus(final Class<VERSUS_CLASS> versusClass) {
        if (hasFKVersus(versusClass)) {
            return (ForeignKey<VERSUS_CLASS>) foreignKeys.get(versusClass);
        }
        throw new OrmConfigurationException("Class [" + mappedClass + "] has no FK versus Class [" + versusClass + "]");   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
    }

    public <VERSUS_CLASS> void addFK(final ForeignKey<VERSUS_CLASS> fk) {
        foreignKeys.put(fk.getVersusClass(), fk);
    }

    void checkAssignableFor(final Class<?> versusClass) {
        ForeignKey<?> candidate = null;
        for (Entry<Class<?>, ForeignKey<?>> fkEntry : foreignKeys.entrySet()) {
            if (fkEntry.getKey().isAssignableFrom(versusClass)) {
                candidate = fkEntry.getValue();
                break;
            }
        }
        if (candidate!=null) {
            foreignKeys.put(versusClass, candidate);
        }
    }

}
