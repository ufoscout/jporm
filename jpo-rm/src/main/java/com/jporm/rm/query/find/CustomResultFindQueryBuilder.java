/*
 * Copyright 2015 ufo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jporm.rm.query.find;

/**
 *
 * @author ufo
 */
public interface CustomResultFindQueryBuilder {

    /**
     * Returns the query
     *
     * @param clazz
     *            The class of the type to find
     * @param alias
     *            the alias for this class in the Find query
     * @return
     */
    <BEAN> CustomResultFindQuery<Class<?>> from(Class<BEAN> clazz, String alias);

}
