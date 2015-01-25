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
package com.jporm.core.query.crud.cache;

import com.jporm.cache.Cache;
import com.jporm.core.cache.SimpleCache;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 14, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class CRUDQueryCacheImpl implements CRUDQueryCache {

	private final Cache delete = new SimpleCache();
	private final Cache find = new SimpleCache();
	private final Cache update = new SimpleCache();
	private final Cache updateLock = new SimpleCache();
	private final Cache saveWithGenerators = new SimpleCache();
	private final Cache saveWithoutGenerators = new SimpleCache();

	@Override
	public Cache delete() {
		return delete;
	}

	@Override
	public Cache find() {
		return find;
	}

	@Override
	public Cache update() {
		return update;
	}

	@Override
	public Cache saveWithGenerators() {
		return saveWithGenerators;
	}

	@Override
	public Cache saveWithoutGenerators() {
		return saveWithoutGenerators;
	}

	@Override
	public Cache updateLock() {
		return updateLock;
	}

}
