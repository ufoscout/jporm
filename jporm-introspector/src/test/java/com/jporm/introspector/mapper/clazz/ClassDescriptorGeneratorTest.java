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
package com.jporm.introspector.mapper.clazz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import com.jporm.annotation.generator.GeneratorType;
import com.jporm.exception.OrmException;
import com.jporm.introspector.BaseTestApi;
import com.jporm.introspector.domain.Blobclob_ByteArray;
import com.jporm.introspector.domain.Blobclob_Stream;
import com.jporm.introspector.domain.Employee;
import com.jporm.types.TypeFactory;

/**
 *
 * @author Francesco Cina
 *
 *         01/giu/2011
 */
public class ClassDescriptorGeneratorTest extends BaseTestApi {

	@Test
	public <P> void testClassDescriptorperGenerator1() {
		final ClassDescriptor<Employee> classMapper = new ClassDescriptorBuilderImpl<Employee>(
				Employee.class, new TypeFactory()).build();
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
			final FieldDescriptor<Employee, P> column = classMapper
					.getClassFieldByJavaName(col);
			assertTrue(column.isIdentifier());
		}

		for (final String col : classMapper.getNotPrimaryKeyColumnJavaNames()) {
			System.out.println("Search column " + col); //$NON-NLS-1$
			final FieldDescriptor<Employee, P> column = classMapper
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
	public <P> void testClassDescriptorperGenerator2() {

		final ClassDescriptor<Blobclob_Stream> classMapper = new ClassDescriptorBuilderImpl<Blobclob_Stream>(
				Blobclob_Stream.class, new TypeFactory()).build();
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
			final FieldDescriptor<Blobclob_Stream, P> column = classMapper
					.getClassFieldByJavaName(col);
			assertTrue(column.isIdentifier());
		}

		for (final String col : classMapper.getNotPrimaryKeyColumnJavaNames()) {
			System.out.println("Search column " + col); //$NON-NLS-1$
			final FieldDescriptor<Blobclob_Stream, P> column = classMapper
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
	public <P> void testClassDescriptorperGenerator3() {

		final ClassDescriptor<Blobclob_ByteArray> classMapper = new ClassDescriptorBuilderImpl<Blobclob_ByteArray>(
				Blobclob_ByteArray.class, new TypeFactory()).build();
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
			final FieldDescriptor<Blobclob_ByteArray, P> column = classMapper
					.getClassFieldByJavaName(col);
			assertTrue(column.isIdentifier());
		}

		for (final String col : classMapper.getNotPrimaryKeyColumnJavaNames()) {
			System.out.println("Search column " + col); //$NON-NLS-1$
			final FieldDescriptor<Blobclob_ByteArray, P> column = classMapper
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
		final ClassDescriptor<BeanWithSequence> classMapper = new ClassDescriptorBuilderImpl<BeanWithSequence>(
				BeanWithSequence.class, new TypeFactory()).build();
		assertNotNull(classMapper);
		FieldDescriptor<BeanWithSequence, Object> field = classMapper.getClassFieldByJavaName("sequenceField");
		assertEquals(GeneratorType.SEQUENCE, field.getGeneratorInfo().getGeneratorType());
	}

	@Test
	public void testBeanWithSequenceFallbackAutogeneratedValuesGenerator() {
		final ClassDescriptor<BeanWithSequenceFallback> classMapper = new ClassDescriptorBuilderImpl<BeanWithSequenceFallback>(
				BeanWithSequenceFallback.class, new TypeFactory()).build();
		assertNotNull(classMapper);
		FieldDescriptor<BeanWithSequenceFallback, Object> field = classMapper.getClassFieldByJavaName("sequenceFallbackField");
		assertEquals(GeneratorType.SEQUENCE_FALLBACK_AUTOGENERATED, field.getGeneratorInfo().getGeneratorType());
	}

	@Test
	public void testBeanWithAutogeneratedFallbackSequenceValuesGenerator() {
		final ClassDescriptor<BeanWithAutogeneratedFallback> classMapper = new ClassDescriptorBuilderImpl<BeanWithAutogeneratedFallback>(
				BeanWithAutogeneratedFallback.class, new TypeFactory()).build();
		assertNotNull(classMapper);
		FieldDescriptor<BeanWithAutogeneratedFallback, Object> field = classMapper.getClassFieldByJavaName("autogeneratedFallbackField");
		assertEquals(GeneratorType.AUTOGENERATED_FALLBACK_SEQUENCE, field.getGeneratorInfo().getGeneratorType());
	}

	@Test
	public void testBeanWithAutogeneratedValuesGenerator() {
		final ClassDescriptor<BeanWithAutogenerated> classMapper = new ClassDescriptorBuilderImpl<BeanWithAutogenerated>(
				BeanWithAutogenerated.class, new TypeFactory()).build();
		assertNotNull(classMapper);
		FieldDescriptor<BeanWithAutogenerated, Object> field = classMapper.getClassFieldByJavaName("autogeneratedField");
		assertEquals(GeneratorType.AUTOGENERATED, field.getGeneratorInfo().getGeneratorType());
	}

}
