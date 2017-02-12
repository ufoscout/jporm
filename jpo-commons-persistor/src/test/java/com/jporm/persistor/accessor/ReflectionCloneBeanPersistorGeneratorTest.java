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
package com.jporm.persistor.accessor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.annotation.mapper.clazz.ClassDescriptorBuilderImpl;
import com.jporm.core.domain.AllAnnotationsBean;
import com.jporm.persistor.BaseTestApi;
import com.jporm.persistor.generator.Persistor;
import com.jporm.persistor.generator.PersistorGeneratorBean;
import com.jporm.types.TypeConverterFactory;

/**
 *
 * @author Francesco Cina'
 *
 *         7 May 2012
 */
public class ReflectionCloneBeanPersistorGeneratorTest extends BaseTestApi {

	private Persistor<AllAnnotationsBean> persistor;

	@Before
	public void setUp() throws Exception {
		final ClassDescriptor<AllAnnotationsBean> classMapper = new ClassDescriptorBuilderImpl<>(AllAnnotationsBean.class).build();
		assertNotNull(classMapper);
		persistor = new PersistorGeneratorBean().generate(classMapper, new TypeConverterFactory());
	}

	@Test
	public void testCloneSpeed() {
		final AllAnnotationsBean sourceBean = new AllAnnotationsBean();
		sourceBean.setIndex1("indexOld1"); //$NON-NLS-1$
		sourceBean.setGeneratedField(0);

		final int howMany = 10000;
		final StopWatch watch = new Log4JStopWatch();
		for (int i = 0; i < howMany; i++) {
			persistor.clone(sourceBean);
		}
		watch.stop("Cloning " + howMany + " objects"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	public void testShadowClone() {

		final AllAnnotationsBean sourceBean = new AllAnnotationsBean();
		sourceBean.setIndex1("indexOld1"); //$NON-NLS-1$
		sourceBean.setGeneratedField(0);

		final AllAnnotationsBean clone = persistor.clone(sourceBean);

		assertFalse(clone == sourceBean);
		assertTrue(sourceBean.getIndex1().equals(clone.getIndex1()));
		assertTrue(sourceBean.getGeneratedField() == clone.getGeneratedField());

		clone.setIndex1("newIndex1"); //$NON-NLS-1$
		clone.setGeneratedField(1);

		assertFalse(clone == sourceBean);
		assertFalse(sourceBean.getIndex1().equals(clone.getIndex1()));
		assertFalse(sourceBean.getGeneratedField() == clone.getGeneratedField());

	}

}
