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
 *          ON : Mar 14, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.commons.core.query.cache;

import java.util.Map;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Mar 14, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public interface SqlCache {

    String delete(final Class<?> clazz);

    Map<Class<?>, String> saveWithGenerators();

    Map<Class<?>, String> saveWithoutGenerators();

    Map<String, String> sqlByUniqueId();

    String update(final Class<?> clazz);

    Map<Class<?>, String> find();

    Map<Class<?>, String> findRowCount();
}
