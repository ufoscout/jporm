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
package com.jporm.commons.core.inject;

import java.util.HashMap;
import java.util.Map;

import com.jporm.annotation.exception.JpoWrongPropertyNameException;
import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.persistor.generator.Persistor;

/**
 *
 * @author Francesco Cina
 *
 *         22/mag/2011
 */
public class ClassToolImpl<BEAN> implements ClassTool<BEAN> {
	private final Map<String, ExtendedFieldDescriptor<BEAN, ?, ?>> fieldClassMapByJavaName = new HashMap<>();

	private final ClassDescriptor<BEAN> descriptor;
	private final Persistor<BEAN> persistor;

	public ClassToolImpl(final ClassDescriptor<BEAN> descriptor, final Persistor<BEAN> ormPersistor) {
		this.descriptor = descriptor;
		this.persistor = ormPersistor;
		for (final String javaFieldName : descriptor.getAllColumnJavaNames()) {
			fieldClassMapByJavaName.put(javaFieldName, ExtendedFieldDescriptor.get(descriptor, descriptor.getFieldDescriptorByJavaName(javaFieldName)));
		}
	}

	@Override
	public ClassDescriptor<BEAN> getDescriptor() {
		return this.descriptor;
	}

	@Override
	public Persistor<BEAN> getPersistor() {
		return this.persistor;
	}

	@Override
	public <R, P> ExtendedFieldDescriptor<BEAN, R, P> getFieldDescriptorByJavaName(String javaName) {
		if (this.fieldClassMapByJavaName.containsKey(javaName)) {
			return (ExtendedFieldDescriptor<BEAN, R, P>) this.fieldClassMapByJavaName.get(javaName);
		}
		throw new JpoWrongPropertyNameException("Property with name [" + javaName + "] not found in class " + descriptor.getMappedClass()); //$NON-NLS-1$ //$NON-NLS-2$

	}

}
