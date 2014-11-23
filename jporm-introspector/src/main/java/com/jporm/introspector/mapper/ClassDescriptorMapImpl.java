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
package com.jporm.introspector.mapper;

import java.util.HashMap;
import java.util.Map;

import com.jporm.JPO;
import com.jporm.exception.OrmException;
import com.jporm.introspector.mapper.clazz.ClassDescriptor;

public class ClassDescriptorMapImpl implements ClassDescriptorMap {

	private final Map<Class<?>, ClassDescriptor<?>> classDescriptorMap = new HashMap<Class<?>, ClassDescriptor<?>>();
	private final JPO jpOrm;

	public ClassDescriptorMapImpl(final JPO jpOrm) {
		this.jpOrm = jpOrm;
	}


	@Override
	public <T> void put(final Class<T> clazz, final ClassDescriptor<T> ormClassTool) {
		classDescriptorMap.put(clazz, ormClassTool);
	}

	@Override
	public boolean containsTool(final Class<?> clazz) {
		return classDescriptorMap.containsKey(clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ClassDescriptor<T> getClassDescriptor(final Class<T> clazz) throws OrmException {
		ClassDescriptor<?> classDescriptor = classDescriptorMap.get(clazz);
		if (classDescriptor==null) {
			jpOrm.register(clazz);
			classDescriptor = classDescriptorMap.get(clazz);
		}
		return (ClassDescriptor<T>) classDescriptor;
	}

}
