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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.annotation.mapper.clazz.ClassDescriptorBuilderImpl;
import com.jporm.core.domain.AllAnnotationsBean;
import com.jporm.persistor.BaseTestApi;
import com.jporm.persistor.Persistor;
import com.jporm.persistor.PersistorGeneratorImpl;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.ResultEntry;

/**
 *
 * @author Francesco Cina'
 *
 *         Mar 24, 2012
 */
public class ReflectionAllAnnotationsBeanPersistorGeneratorTest extends BaseTestApi {

	private ClassDescriptor<AllAnnotationsBean> classMapper;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Persistor<AllAnnotationsBean> persistor;
	private AllAnnotationsBean annBean;

	@Before
	public void setUp() throws Exception {
		classMapper = new ClassDescriptorBuilderImpl<>(AllAnnotationsBean.class).build();
		assertNotNull(classMapper);
		persistor = new PersistorGeneratorImpl<>(classMapper, new TypeConverterFactory()).generate();

		annBean = new AllAnnotationsBean();
		annBean.setGeneratedField(123l);
		annBean.setIndex1("index1"); //$NON-NLS-1$
		annBean.setMyVersion(999999);
		annBean.setIndex2("index2"); //$NON-NLS-1$
		annBean.setColumnAnnotated("columnAnnotated"); //$NON-NLS-1$
		annBean.setColumnNotAnnotated(11111l);

	}

	@Test
	public void testGenerators() {

		assertTrue(persistor.hasGenerator());

	}

	@Test
	public void testMapRow() throws SQLException {
		final ResultEntry rs = mock(ResultEntry.class);

		final Random random = new Random();
		final long generatedField = random.nextLong();
		final long myVersion = random.nextLong();
		final String index1 = "index1_" + random.nextInt(); //$NON-NLS-1$
		final String index2 = "index2_" + random.nextInt(); //$NON-NLS-1$
		final String annotated = "annotated_" + random.nextInt(); //$NON-NLS-1$
		final long notAnnotated = random.nextLong();

		doReturn(generatedField).when(rs).getLong("generatedField"); //$NON-NLS-1$
		doReturn(myVersion).when(rs).getLong("myVersion"); //$NON-NLS-1$
		when(rs.getString("index1")).thenReturn(index1); //$NON-NLS-1$
		when(rs.getString("index2")).thenReturn(index2); //$NON-NLS-1$
		when(rs.getObject("columnAnnotated")).thenReturn(annotated); //$NON-NLS-1$
		when(rs.getLong("columnNotAnnotated")).thenReturn(notAnnotated); //$NON-NLS-1$
		when(rs.getString("bean6")).thenReturn("bean6Value"); //$NON-NLS-1$//$NON-NLS-2$

		final AllAnnotationsBean createdEntity = persistor.beanFromResultSet(rs, new ArrayList<String>());

		assertEquals(generatedField, createdEntity.getGeneratedField());
		assertEquals(myVersion, createdEntity.getMyVersion());
		assertEquals(index1, createdEntity.getIndex1());
		assertEquals(index2, createdEntity.getIndex2());
		assertEquals(annotated, createdEntity.getColumnAnnotated());
		assertEquals(notAnnotated, createdEntity.getColumnNotAnnotated());

	}

	@Test
	public void testPrimaryKeyColumnJavaNames() {

		final String[] expectedFields = classMapper.getPrimaryKeyColumnJavaNames();
		final Object[] primaryKeyValues = persistor.getPropertyValues(expectedFields, annBean);

		logger.info("Expected file order:"); //$NON-NLS-1$
		// The order of the readed field must match this
		logger.info(Arrays.toString(expectedFields));

		assertEquals(expectedFields.length, primaryKeyValues.length);

		int i = 0;
		assertEquals(annBean.getIndex1(), primaryKeyValues[i++]);
		assertEquals(annBean.getIndex2(), primaryKeyValues[i++]);

		assertEquals(i, primaryKeyValues.length);
	}

	@Test
	public void testUpdatePrimaryKey() throws Exception {

		final ResultEntry rs = mock(ResultEntry.class);

		final Random random = new Random();
		final long generatedField = random.nextLong();

		doReturn(generatedField).when(rs).getLong(0);

		annBean = persistor.updateGeneratedValues(rs, annBean);

		assertEquals(generatedField, annBean.getGeneratedField());
		assertEquals("index1", annBean.getIndex1()); //$NON-NLS-1$
		assertEquals("index2", annBean.getIndex2()); //$NON-NLS-1$
		assertEquals(999999l, annBean.getMyVersion());
		assertEquals("columnAnnotated", annBean.getColumnAnnotated()); //$NON-NLS-1$
		assertEquals(11111l, annBean.getColumnNotAnnotated());

	}

	@Test
	public void testVersion() {

		annBean.setMyVersion(11l);

		persistor.increaseVersion(annBean, true);
		final long version = annBean.getMyVersion();
		assertEquals(0l, version);
		logger.info("Expected version: " + version); //$NON-NLS-1$

		persistor.increaseVersion(annBean, false);
		logger.info("Updated version: " + annBean.getMyVersion()); //$NON-NLS-1$
		assertEquals(version + 1, annBean.getMyVersion());

		persistor.increaseVersion(annBean, false);
		logger.info("Updated version: " + annBean.getMyVersion()); //$NON-NLS-1$
		assertEquals(version + 2, annBean.getMyVersion());

	}

}
