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
package com.jporm.commons.core.inject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jporm.commons.core.JPOConfig;
import com.jporm.commons.core.exception.JpoException;

public class ClassToolMapImpl implements ClassToolMap {

	private final Map<Class<?>, ClassTool<?>> classToolMap = new ConcurrentHashMap<Class<?>, ClassTool<?>>();
	private final JPOConfig jpOrm;

	public ClassToolMapImpl(final JPOConfig jpOrm) {
		this.jpOrm = jpOrm;
	}


	@Override
	public <T> void put(final Class<T> clazz, final ClassTool<T> ormClassTool) {
		classToolMap.put(clazz, ormClassTool);
	}

	@Override
	public boolean containsTool(final Class<?> clazz) {
		return classToolMap.containsKey(clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ClassTool<T> get(final Class<T> clazz) throws JpoException {
		ClassTool<?> ormClazzTool = classToolMap.get(clazz);
		if (ormClazzTool==null) {
			jpOrm.register(clazz);
			ormClazzTool = classToolMap.get(clazz);
		}
		return (ClassTool<T>) ormClazzTool;
	}

}
