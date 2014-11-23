/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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
package com.jporm.deprecated.core.mapper;


public class NullClassToolMap implements ClassToolMap {

	@Override
	public boolean containsTool(final Class<?> clazz) {
		return false;
	}

	@Override
	public <T> OrmClassTool<T> getOrmClassTool(final Class<T> clazz) {
		return new NullOrmClassTool<T>();
	}


	@Override
	public <T> void put(final Class<T> clazz, final OrmClassTool<T> ormClassTool) {
	}

}
