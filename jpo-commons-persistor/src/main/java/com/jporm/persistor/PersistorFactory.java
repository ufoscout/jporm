/*******************************************************************************
 * Copyright 2017 Francesco Cina'
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
package com.jporm.persistor;

import java.util.ArrayList;
import java.util.List;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.persistor.generator.Persistor;
import com.jporm.persistor.generator.PersistorGenerator;
import com.jporm.persistor.generator.PersistorGeneratorBean;
import com.jporm.persistor.generator.immutables.PersistorGeneratorImmutables;
import com.jporm.types.TypeConverterFactory;

public class PersistorFactory {

	private final List<PersistorGenerator> persistors = new ArrayList<>();
	private final TypeConverterFactory typeFactory;

	public PersistorFactory(final TypeConverterFactory typeFactory) {
		this.typeFactory = typeFactory;
		persistors.add(new PersistorGeneratorImmutables());
		persistors.add(new PersistorGeneratorBean());
	}

	/**
	 * Generate a {@link Persistor} for the BEAN
	 * @return
	 * @throws Exception
	 */
	public <BEAN> Persistor<BEAN> generate(final ClassDescriptor<BEAN> classMap) throws Exception {
		for (final PersistorGenerator generator : persistors) {
			if (generator.applicableFor(classMap.getMappedClass())) {
				return generator.generate(classMap, typeFactory);
			}
		}
		throw new RuntimeException("Not possible to find a valid persistor for Class [" + classMap.getClass() + "]. Does it has a public constructor with no parameters?");
	}

}
