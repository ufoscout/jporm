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
package com.jporm.persistor.generator;

import java.lang.reflect.InvocationTargetException;

/**
 * 
 * @author Francesco Cina'
 *
 *         Apr 1, 2012
 */
public abstract class GeneratorManipulator<BEAN> {

    /**
     * Return true if a bean has an active generator associated
     * 
     * @return
     */
    public abstract boolean hasGenerator();

    /**
     * 
     * Check if a generator has to be activated for a bean.
     * 
     * @param bean
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public abstract boolean useGenerator(BEAN bean) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;
}
