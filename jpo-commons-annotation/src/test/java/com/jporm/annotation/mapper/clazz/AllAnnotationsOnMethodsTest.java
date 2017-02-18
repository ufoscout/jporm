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
package com.jporm.annotation.mapper.clazz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.jporm.annotation.BaseTestApi;
import com.jporm.annotation.exception.JpoWrongPropertyNameException;

public class AllAnnotationsOnMethodsTest extends BaseTestApi {

	@Test
	public void testBeanWithAutogeneratedFallbackSequenceValuesGenerator() {

		final ClassDescriptor<AllAnnotationsOnMethods> classMapper = new ClassDescriptorBuilderImpl<>(AllAnnotationsOnMethods.class, Collections.emptyList()).build();
		assertNotNull(classMapper);

		final FieldDescriptor<AllAnnotationsOnMethods, String> index1 = classMapper.getFieldDescriptorByJavaName("index1");
		assertEquals(String.class, index1.getProcessedClass());
		assertTrue(index1.isIdentifier());

		final FieldDescriptor<AllAnnotationsOnMethods, String> index2 = classMapper.getFieldDescriptorByJavaName("index2");
		assertEquals(String.class, index1.getProcessedClass());
		assertTrue(index2.isIdentifier());

	}

	@Test
	public void testIgnoreFieldsByName() {

		final ClassDescriptor<AllAnnotationsOnMethods> classMapper = new ClassDescriptorBuilderImpl<>(AllAnnotationsOnMethods.class, Arrays.asList("index1")).build();
		assertNotNull(classMapper);

		assertNotNull(classMapper.getFieldDescriptorByJavaName("index2"));

		try {
			classMapper.getFieldDescriptorByJavaName("index1");
			fail("exception should be thrown as the field should have been ignored by the ClassDescriptorBuilder");
		} catch (final JpoWrongPropertyNameException e) {
			// OK
		}
	}

}
