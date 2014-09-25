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
package com.jporm.mapper.clazz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import com.jporm.BaseTestApi;
import com.jporm.annotation.generator.GeneratorType;
import com.jporm.domain.section01.Employee;
import com.jporm.domain.section02.Blobclob_ByteArray;
import com.jporm.domain.section02.Blobclob_Stream;
import com.jporm.exception.OrmException;
import com.jporm.mapper.NullServiceCatalog;

/**
 *
 * @author Francesco Cina
 *
 *         01/giu/2011
 */
public class ClassMapperGeneratorTest extends BaseTestApi {

	@Test
	public <P> void testClassMapperGenerator1() {
		final ClassMap<Employee> classMapper = new ClassMapBuilderImpl<Employee>(
				Employee.class, new NullServiceCatalog()).generate();
		assertNotNull(classMapper);

		assertEquals("", classMapper.getTableInfo().getSchemaName()); //$NON-NLS-1$
		assertEquals(
				"EMPLOYEE", classMapper.getTableInfo().getTableNameWithSchema()); //$NON-NLS-1$
		assertEquals(5, classMapper.getAllColumnJavaNames().length);
		assertEquals(0, classMapper.getPrimaryKeyColumnJavaNames().length);
		assertEquals(5, classMapper.getNotPrimaryKeyColumnJavaNames().length);

		try {
			classMapper.getClassFieldByJavaName("" + new Date().getTime()); //$NON-NLS-1$
			fail("An exception should be thrwn here!"); //$NON-NLS-1$
		} catch (OrmException e) {
			// do nothing
		}

		for (final String col : classMapper.getAllColumnJavaNames()) {
			System.out.println("Search column " + col); //$NON-NLS-1$
		}

		for (final String col : classMapper.getPrimaryKeyColumnJavaNames()) {
			System.out.println("Search column " + col); //$NON-NLS-1$
			final ClassField<Employee, P> column = classMapper
					.getClassFieldByJavaName(col);
			assertTrue(column.isIdentifier());
		}

		for (final String col : classMapper.getNotPrimaryKeyColumnJavaNames()) {
			System.out.println("Search column " + col); //$NON-NLS-1$
			final ClassField<Employee, P> column = classMapper
					.getClassFieldByJavaName(col);
			assertFalse(column.isIdentifier());
		}

		assertTrue(Arrays.asList(classMapper.getAllColumnJavaNames()).contains(
				"id")); //$NON-NLS-1$
		assertTrue(Arrays.asList(classMapper.getAllColumnJavaNames()).contains(
				"age")); //$NON-NLS-1$
		assertTrue(Arrays.asList(classMapper.getAllColumnJavaNames()).contains(
				"name")); //$NON-NLS-1$
		assertTrue(Arrays.asList(classMapper.getAllColumnJavaNames()).contains(
				"surname")); //$NON-NLS-1$
		assertTrue(Arrays.asList(classMapper.getAllColumnJavaNames()).contains(
				"employeeNumber")); //$NON-NLS-1$

		assertTrue(Arrays.asList(classMapper.getNotPrimaryKeyColumnJavaNames())
				.contains("id")); //$NON-NLS-1$
		assertTrue(Arrays.asList(classMapper.getNotPrimaryKeyColumnJavaNames())
				.contains("age")); //$NON-NLS-1$
		assertTrue(Arrays.asList(classMapper.getNotPrimaryKeyColumnJavaNames())
				.contains("name")); //$NON-NLS-1$
		assertTrue(Arrays.asList(classMapper.getNotPrimaryKeyColumnJavaNames())
				.contains("surname")); //$NON-NLS-1$
		assertTrue(Arrays.asList(classMapper.getNotPrimaryKeyColumnJavaNames())
				.contains("employeeNumber")); //$NON-NLS-1$

		assertEquals(
				"ID", classMapper.getClassFieldByJavaName("id").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(
				"AGE", classMapper.getClassFieldByJavaName("age").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(
				"NAME", classMapper.getClassFieldByJavaName("name").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(
				"SURNAME", classMapper.getClassFieldByJavaName("surname").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(
				"EMPLOYEE_NUMBER", classMapper.getClassFieldByJavaName("employeeNumber").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$

	}

	@Test
	public <P> void testClassMapperGenerator2() {

		final ClassMap<Blobclob_Stream> classMapper = new ClassMapBuilderImpl<Blobclob_Stream>(
				Blobclob_Stream.class, new NullServiceCatalog()).generate();
		assertNotNull(classMapper);

		assertEquals("", classMapper.getTableInfo().getSchemaName()); //$NON-NLS-1$
		assertEquals(
				"BLOBCLOB", classMapper.getTableInfo().getTableNameWithSchema()); //$NON-NLS-1$
		assertEquals(3, classMapper.getAllColumnJavaNames().length);
		assertEquals(1, classMapper.getPrimaryKeyColumnJavaNames().length);
		assertEquals(2, classMapper.getNotPrimaryKeyColumnJavaNames().length);

		for (final String col : classMapper.getAllColumnJavaNames()) {
			System.out.println("Search column " + col); //$NON-NLS-1$
		}

		for (final String col : classMapper.getPrimaryKeyColumnJavaNames()) {
			System.out.println("Search column " + col); //$NON-NLS-1$
			final ClassField<Blobclob_Stream, P> column = classMapper
					.getClassFieldByJavaName(col);
			assertTrue(column.isIdentifier());
		}

		for (final String col : classMapper.getNotPrimaryKeyColumnJavaNames()) {
			System.out.println("Search column " + col); //$NON-NLS-1$
			final ClassField<Blobclob_Stream, P> column = classMapper
					.getClassFieldByJavaName(col);
			assertFalse(column.isIdentifier());
		}

		assertTrue(Arrays.asList(classMapper.getAllColumnJavaNames()).contains(
				"id")); //$NON-NLS-1$
		assertTrue(Arrays.asList(classMapper.getAllColumnJavaNames()).contains(
				"blobInputStream")); //$NON-NLS-1$
		assertTrue(Arrays.asList(classMapper.getAllColumnJavaNames()).contains(
				"clob")); //$NON-NLS-1$

		assertTrue(Arrays.asList(classMapper.getPrimaryKeyColumnJavaNames())
				.contains("id")); //$NON-NLS-1$

		assertTrue(Arrays.asList(classMapper.getNotPrimaryKeyColumnJavaNames())
				.contains("blobInputStream")); //$NON-NLS-1$
		assertTrue(Arrays.asList(classMapper.getNotPrimaryKeyColumnJavaNames())
				.contains("clob")); //$NON-NLS-1$

		assertEquals(
				"ID", classMapper.getClassFieldByJavaName("id").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(
				"BLOB", classMapper.getClassFieldByJavaName("blobInputStream").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(
				"CLOB", classMapper.getClassFieldByJavaName("clob").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$

	}

	@Test
	public <P> void testClassMapperGenerator3() {

		final ClassMap<Blobclob_ByteArray> classMapper = new ClassMapBuilderImpl<Blobclob_ByteArray>(
				Blobclob_ByteArray.class, new NullServiceCatalog()).generate();
		assertNotNull(classMapper);

		assertEquals("", classMapper.getTableInfo().getSchemaName()); //$NON-NLS-1$
		assertEquals(
				"BLOBCLOB", classMapper.getTableInfo().getTableNameWithSchema()); //$NON-NLS-1$
		assertEquals(3, classMapper.getAllColumnJavaNames().length);
		assertEquals(1, classMapper.getPrimaryKeyColumnJavaNames().length);
		assertEquals(2, classMapper.getNotPrimaryKeyColumnJavaNames().length);
		assertEquals(1, classMapper.getAllGeneratedColumnJavaNames().length);

		for (final String col : classMapper.getAllColumnJavaNames()) {
			System.out.println("Search column " + col); //$NON-NLS-1$
		}

		for (final String col : classMapper.getPrimaryKeyColumnJavaNames()) {
			System.out.println("Search column " + col); //$NON-NLS-1$
			final ClassField<Blobclob_ByteArray, P> column = classMapper
					.getClassFieldByJavaName(col);
			assertTrue(column.isIdentifier());
		}

		for (final String col : classMapper.getNotPrimaryKeyColumnJavaNames()) {
			System.out.println("Search column " + col); //$NON-NLS-1$
			final ClassField<Blobclob_ByteArray, P> column = classMapper
					.getClassFieldByJavaName(col);
			assertFalse(column.isIdentifier());
		}

		assertTrue(Arrays.asList(classMapper.getAllColumnJavaNames()).contains(
				"index")); //$NON-NLS-1$
		assertTrue(Arrays.asList(classMapper.getAllColumnJavaNames()).contains(
				"blob")); //$NON-NLS-1$
		assertTrue(Arrays.asList(classMapper.getAllColumnJavaNames()).contains(
				"clob")); //$NON-NLS-1$

		assertTrue(Arrays.asList(classMapper.getAllGeneratedColumnJavaNames())
				.contains("index")); //$NON-NLS-1$

		assertTrue(Arrays.asList(classMapper.getPrimaryKeyColumnJavaNames())
				.contains("index")); //$NON-NLS-1$

		assertTrue(Arrays.asList(classMapper.getNotPrimaryKeyColumnJavaNames())
				.contains("blob")); //$NON-NLS-1$
		assertTrue(Arrays.asList(classMapper.getNotPrimaryKeyColumnJavaNames())
				.contains("clob")); //$NON-NLS-1$

		assertEquals(
				"ID", classMapper.getClassFieldByJavaName("index").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(
				"BLOB", classMapper.getClassFieldByJavaName("blob").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(
				"CLOB", classMapper.getClassFieldByJavaName("clob").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$

	}

	@Test
	public void testBeanWithSequenceValuesGenerator() {
		final ClassMap<BeanWithSequence> classMapper = new ClassMapBuilderImpl<BeanWithSequence>(
				BeanWithSequence.class, new NullServiceCatalog()).generate();
		assertNotNull(classMapper);
		ClassFieldImpl<BeanWithSequence, Object> field = classMapper.getClassFieldByJavaName("sequenceField");
		assertEquals(GeneratorType.SEQUENCE, field.getGeneratorInfo().getGeneratorType());
	}

	@Test
	public void testBeanWithSequenceFallbackAutogeneratedValuesGenerator() {
		final ClassMap<BeanWithSequenceFallback> classMapper = new ClassMapBuilderImpl<BeanWithSequenceFallback>(
				BeanWithSequenceFallback.class, new NullServiceCatalog()).generate();
		assertNotNull(classMapper);
		ClassFieldImpl<BeanWithSequenceFallback, Object> field = classMapper.getClassFieldByJavaName("sequenceFallbackField");
		assertEquals(GeneratorType.SEQUENCE_FALLBACK_AUTOGENERATED, field.getGeneratorInfo().getGeneratorType());
	}

	@Test
	public void testBeanWithAutogeneratedFallbackSequenceValuesGenerator() {
		final ClassMap<BeanWithAutogeneratedFallback> classMapper = new ClassMapBuilderImpl<BeanWithAutogeneratedFallback>(
				BeanWithAutogeneratedFallback.class, new NullServiceCatalog()).generate();
		assertNotNull(classMapper);
		ClassFieldImpl<BeanWithAutogeneratedFallback, Object> field = classMapper.getClassFieldByJavaName("autogeneratedFallbackField");
		assertEquals(GeneratorType.AUTOGENERATED_FALLBACK_SEQUENCE, field.getGeneratorInfo().getGeneratorType());
	}

	@Test
	public void testBeanWithAutogeneratedValuesGenerator() {
		final ClassMap<BeanWithAutogenerated> classMapper = new ClassMapBuilderImpl<BeanWithAutogenerated>(
				BeanWithAutogenerated.class, new NullServiceCatalog()).generate();
		assertNotNull(classMapper);
		ClassFieldImpl<BeanWithAutogenerated, Object> field = classMapper.getClassFieldByJavaName("autogeneratedField");
		assertEquals(GeneratorType.AUTOGENERATED, field.getGeneratorInfo().getGeneratorType());
	}

}
