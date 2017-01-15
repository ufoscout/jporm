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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.annotation.mapper.clazz.ClassDescriptorBuilderImpl;
import com.jporm.commons.core.exception.JpoException;
import com.jporm.persistor.Persistor;
import com.jporm.persistor.PersistorGeneratorImpl;
import com.jporm.types.TypeConverterFactory;

public class ClassToolMapImpl implements ClassToolMap {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final TypeConverterFactory typeFactory;
	private final Map<Class<?>, ClassTool<?>> classToolMap = new ConcurrentHashMap<>();

	public ClassToolMapImpl(final TypeConverterFactory typeFactory) {
		this.typeFactory = typeFactory;
	}

	@Override
	public boolean containsTool(final Class<?> clazz) {
		return classToolMap.containsKey(clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ClassTool<T> get(final Class<T> clazz) throws JpoException {
		ClassTool<?> ormClazzTool = classToolMap.get(clazz);
		if (ormClazzTool == null) {
			register(clazz);
			ormClazzTool = classToolMap.get(clazz);
		}
		return (ClassTool<T>) ormClazzTool;
	}

	public synchronized <BEAN> void register(final Class<BEAN> clazz) {
		try {
			if (!containsTool(clazz)) {
				logger.debug("register new class: " + clazz.getName());
				final ClassDescriptor<BEAN> classDescriptor = new ClassDescriptorBuilderImpl<>(clazz).build();
				final Persistor<BEAN> ormPersistor = new PersistorGeneratorImpl<>(classDescriptor, typeFactory).generate();
				final ClassTool<BEAN> classTool = new ClassToolImpl<>(classDescriptor, ormPersistor);
				classToolMap.put(clazz, classTool);
			}
		} catch (final Exception e) {
			throw new JpoException(e);
		}
	}

}
