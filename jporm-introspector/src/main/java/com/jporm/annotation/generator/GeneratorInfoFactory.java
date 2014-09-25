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
package com.jporm.annotation.generator;

import java.lang.reflect.Field;

import com.jporm.annotation.Generator;
import com.jporm.factory.ObjectBuilder;

/**
 *
 * @author cinafr
 *
 */
public class GeneratorInfoFactory {

	private GeneratorInfoFactory() {}

	public static GeneratorInfo getGeneratorInfo(final Field field) {
		Generator generator = field.getAnnotation(Generator.class);
		if (generator!=null) {
			return new GeneratorInfoImpl(generator.generatorType(), generator.name(), true);
		}
		return new GeneratorInfoImpl(GeneratorType.NONE, ObjectBuilder.EMPTY_STRING, false);
	}

}
