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
package com.jporm.annotation.mapper.clazz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.jporm.annotation.BaseTestApi;
import com.jporm.annotation.exception.JpoWrongAnnotationException;

/**
 *
 * @author Francesco Cina
 *
 *         08/giu/2011
 */
public class ClassDBMapReflectionTest extends BaseTestApi {

	@Test
	public void testClassDBMapper1() {

		final ClassDescriptor<Employee> classDBMap = new ClassDescriptorBuilderImpl<>(Employee.class).build();
		assertNotNull(classDBMap);

		assertEquals("", classDBMap.getTableInfo().getSchemaName()); //$NON-NLS-1$
		assertEquals("EMPLOYEE", classDBMap.getTableInfo().getTableNameWithSchema()); //$NON-NLS-1$

		assertEquals("ID", classDBMap.getFieldDescriptorByJavaName("id").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("AGE", classDBMap.getFieldDescriptorByJavaName("age").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("NAME", classDBMap.getFieldDescriptorByJavaName("name").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("SURNAME", classDBMap.getFieldDescriptorByJavaName("surname").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("EMPLOYEE_NUMBER", classDBMap.getFieldDescriptorByJavaName("employeeNumber").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$

		assertNotNull(classDBMap.getPrimaryKeyColumnJavaNames());
		assertTrue(classDBMap.getPrimaryKeyColumnJavaNames().length == 0);

		String versionField = ""; //$NON-NLS-1$
		for (final String javaName : classDBMap.getAllColumnJavaNames()) {
			if (classDBMap.getFieldDescriptorByJavaName(javaName).getVersionInfo().isVersionable()) {
				versionField = javaName;
			}
		}
		assertEquals("", versionField); //$NON-NLS-1$
	}

	@Test
	public void testClassDBMapper2() {
		final ClassDescriptor<AnnotationBean1> classDBMap = new ClassDescriptorBuilderImpl<>(AnnotationBean1.class)
				.build();
		assertNotNull(classDBMap);

		assertEquals("", classDBMap.getTableInfo().getSchemaName()); //$NON-NLS-1$
		assertEquals("ANNOTATION_TABLE_NAME", classDBMap.getTableInfo().getTableNameWithSchema()); //$NON-NLS-1$

		assertEquals("INDEX", classDBMap.getFieldDescriptorByJavaName("index").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("COLUMN_NOT_ANNOTATED", classDBMap.getFieldDescriptorByJavaName("columnNotAnnotated").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("ANNOTATION_COLUMN_NAME", classDBMap.getFieldDescriptorByJavaName("columnAnnotated").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$

		assertNotNull(classDBMap.getPrimaryKeyColumnJavaNames());
		assertTrue(classDBMap.getPrimaryKeyColumnJavaNames().length == 1);
		assertEquals("index", classDBMap.getPrimaryKeyColumnJavaNames()[0]); //$NON-NLS-1$

		String versionField = ""; //$NON-NLS-1$
		for (final String javaName : classDBMap.getAllColumnJavaNames()) {
			if (classDBMap.getFieldDescriptorByJavaName(javaName).getVersionInfo().isVersionable()) {
				versionField = javaName;
			}
		}
		assertEquals("", versionField); //$NON-NLS-1$
	}

	@Test
	public void testClassDBMapper3() {
		final ClassDescriptor<AnnotationBean3> classDBMap = new ClassDescriptorBuilderImpl<>(AnnotationBean3.class)
				.build();
		assertNotNull(classDBMap);

		assertEquals("SCHEMA_NAME", classDBMap.getTableInfo().getSchemaName()); //$NON-NLS-1$
		assertEquals("ANNOTATION_TABLE_NAME", classDBMap.getTableInfo().getTableName()); //$NON-NLS-1$
		assertEquals("SCHEMA_NAME.ANNOTATION_TABLE_NAME", classDBMap.getTableInfo().getTableNameWithSchema()); //$NON-NLS-1$

		assertEquals("INDEX", classDBMap.getFieldDescriptorByJavaName("index").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("COLUMN_NOT_ANNOTATED", classDBMap.getFieldDescriptorByJavaName("columnNotAnnotated").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("ANNOTATION_COLUMN_NAME", classDBMap.getFieldDescriptorByJavaName("columnAnnotated").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$

		assertNotNull(classDBMap.getPrimaryKeyColumnJavaNames());
		assertTrue(classDBMap.getPrimaryKeyColumnJavaNames().length == 2);
		assertTrue(Arrays.asList(classDBMap.getPrimaryKeyColumnJavaNames()).contains("index")); //$NON-NLS-1$
		assertTrue(Arrays.asList(classDBMap.getPrimaryKeyColumnJavaNames()).contains("columnAnnotated")); //$NON-NLS-1$

		String versionField = ""; //$NON-NLS-1$
		for (final String javaName : classDBMap.getAllColumnJavaNames()) {
			if (classDBMap.getFieldDescriptorByJavaName(javaName).getVersionInfo().isVersionable()) {
				versionField = javaName;
			}
		}
		assertEquals("version", versionField); //$NON-NLS-1$
	}

	@Test
	public void testClassDBMapper4() {
		final ClassDescriptor<AnnotationBean2> classDBMap = new ClassDescriptorBuilderImpl<>(AnnotationBean2.class)
				.build();
		assertNotNull(classDBMap);

		assertEquals("SCHEMA_NAME", classDBMap.getTableInfo().getSchemaName()); //$NON-NLS-1$
		assertEquals("ANNOTATION_BEAN2", classDBMap.getTableInfo().getTableName()); //$NON-NLS-1$
		assertEquals("SCHEMA_NAME.ANNOTATION_BEAN2", classDBMap.getTableInfo().getTableNameWithSchema()); //$NON-NLS-1$

		assertEquals("INDEX", classDBMap.getFieldDescriptorByJavaName("index").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("COLUMN_NOT_ANNOTATED", classDBMap.getFieldDescriptorByJavaName("columnNotAnnotated").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("COLUMN_NOT_ANNOTATED2", classDBMap.getFieldDescriptorByJavaName("columnNotAnnotated2").getColumnInfo().getDBColumnName()); //$NON-NLS-1$ //$NON-NLS-2$

		assertNotNull(classDBMap.getPrimaryKeyColumnJavaNames());
		assertTrue(classDBMap.getPrimaryKeyColumnJavaNames().length == 2);

		assertTrue(Arrays.asList(classDBMap.getPrimaryKeyColumnJavaNames()).contains("index")); //$NON-NLS-1$
		assertTrue(Arrays.asList(classDBMap.getPrimaryKeyColumnJavaNames()).contains("columnNotAnnotated2")); //$NON-NLS-1$

		String versionField = ""; //$NON-NLS-1$
		for (final String javaName : classDBMap.getAllColumnJavaNames()) {
			if (classDBMap.getFieldDescriptorByJavaName(javaName).getVersionInfo().isVersionable()) {
				versionField = javaName;
			}
		}
		assertEquals("", versionField); //$NON-NLS-1$
	}

	@Test
	public void testClassDBMapperShouldThrownExceptionForDuplicatedGenerator() {
		boolean onlyOneVersionAnnotationException = false;
		try {
			new ClassDescriptorBuilderImpl<>(AnnotationBean7.class).build();

		} catch (final JpoWrongAnnotationException e) {
			if (e.getMessage().contains("@Generator")) { //$NON-NLS-1$
				onlyOneVersionAnnotationException = true;
			}
		}
		assertTrue(onlyOneVersionAnnotationException);
	}

	@Test
	public void testClassDBMapperShouldThrownExceptionForDuplicatedVersion() {
		boolean onlyOneVersionAnnotationException = false;
		try {
			new ClassDescriptorBuilderImpl<>(AnnotationBean4.class).build();
		} catch (final JpoWrongAnnotationException e) {
			if (e.getMessage().contains("@Version")) { //$NON-NLS-1$
				onlyOneVersionAnnotationException = true;
			}
		}
		assertTrue(onlyOneVersionAnnotationException);
	}

}
