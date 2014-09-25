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
package com.jporm.query.namesolver;

import com.jporm.exception.OrmException;

/**
 * 
 * @author Francesco Cina
 *
 * 19/giu/2011
 */
public interface NameSolver  {

    /**
     * Resolve a property in a query to his name in the database using the table alias as prefix.
     * @param property
     * @return
     */
    String solvePropertyName(String property) throws OrmException;

    /**
     * Solved all the property names found in a string and append to the outputBuilder the original string with all
     * the properties resolved
     * @param input
     * @param outputBuilder
     */
    void solveAllPropertyNames(final String input, final StringBuilder outputBuilder);


    /**
     * Register a class and use the passed alias parameter as alias to resolve the property name.
     * @param clazz
     * @param alias
     * @return the registered class id
     */
    <P> Integer register(Class<P> clazz, String alias) throws OrmException;

    /**
     * Return the alias of a registered class
     * @param clazz
     * @return
     * @throws OrmException
     */
    String normalizedAlias(Integer clazzId) throws OrmException;

}
