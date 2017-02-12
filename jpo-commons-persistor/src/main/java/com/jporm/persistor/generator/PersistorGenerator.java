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
package com.jporm.persistor.generator;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.types.TypeConverterFactory;

/**
 *
 * @author Francesco Cina
 *
 *         25/mag/2011
 */
public interface PersistorGenerator {

	/**
	 * Return whether the current {@link PersistorGenerator} is applicable for the specific Class
	 *
	 * @param beanClass
	 * @return
	 */
	<BEAN> boolean applicableFor(final Class<BEAN> beanClass);

	/**
	 * Generates the BEAN {@link Persistor}
	 *
	 * @param classMap
	 * @param typeFactory
	 * @return
	 * @throws Exception
	 */
	<BEAN> Persistor<BEAN> generate(final ClassDescriptor<BEAN> classMap, final TypeConverterFactory typeFactory) throws Exception;

}
