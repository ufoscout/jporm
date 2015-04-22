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
package com.jporm.commons.core.query.cache.impl;

import com.jporm.cache.Cache;
import com.jporm.cache.simple.SimpleCache;
import com.jporm.commons.core.query.cache.SqlCache;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 14, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class SqlCacheImpl implements SqlCache {

	private final Cache<Class<?>, String> delete = new SimpleCache<>();
	private final Cache<String, String> sqlByUniqueId = new SimpleCache<>();
	private final Cache<Class<?>, String> update = new SimpleCache<>();
	private final Cache<Class<?>, String> saveWithGenerators = new SimpleCache<>();
	private final Cache<Class<?>, String> saveWithoutGenerators = new SimpleCache<>();

	@Override
	public Cache<Class<?>, String> delete() {
		return delete;
	}

	@Override
	public Cache<String, String> sqlByUniqueId() {
		return sqlByUniqueId;
	}

	@Override
	public Cache<Class<?>, String> update() {
		return update;
	}

	@Override
	public Cache<Class<?>, String> saveWithGenerators() {
		return saveWithGenerators;
	}

	@Override
	public Cache<Class<?>, String> saveWithoutGenerators() {
		return saveWithoutGenerators;
	}

}
