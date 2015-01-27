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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.jporm.core.domain.AnnotationBean1Extended;
import com.jporm.introspector.BaseTestApi;
import com.jporm.types.TypeFactory;

/**
 *
 * @author Francesco Cina
 *
 * 08/giu/2011
 */
public class ExtendedClassDBMapReflectionTest extends BaseTestApi {

	@Test
	public void testClassDBMapper2() {
		final ClassDescriptor<AnnotationBean1Extended> classDBMap = new ClassDescriptorBuilderImpl<AnnotationBean1Extended>(AnnotationBean1Extended.class, new TypeFactory()).build();

		assertNotNull(classDBMap);

		assertEquals( "" , classDBMap.getTableInfo().getSchemaName() ); //$NON-NLS-1$
		assertEquals( "ANNOTATION_TABLE_NAME" , classDBMap.getTableInfo().getTableNameWithSchema() ); //$NON-NLS-1$

		assertEquals( "INDEX" , classDBMap.getFieldDescriptorByJavaName( "index" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "COLUMN_NOT_ANNOTATED" , classDBMap.getFieldDescriptorByJavaName( "columnNotAnnotated" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "ANNOTATION_COLUMN_NAME" , classDBMap.getFieldDescriptorByJavaName( "columnAnnotated" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$

		assertNotNull(classDBMap.getPrimaryKeyColumnJavaNames());
		assertTrue(classDBMap.getPrimaryKeyColumnJavaNames().length == 2);
		assertTrue( Arrays.asList( classDBMap.getPrimaryKeyColumnJavaNames() ).contains("index")); //$NON-NLS-1$
		assertTrue( Arrays.asList( classDBMap.getPrimaryKeyColumnJavaNames() ).contains("index2")); //$NON-NLS-1$

		String versionField = ""; //$NON-NLS-1$
		for (String javaName : classDBMap.getAllColumnJavaNames()) {
			if (classDBMap.getFieldDescriptorByJavaName(javaName).getVersionInfo().isVersionable()) {
				versionField = javaName;
			}
		}
		assertEquals("myVersion", versionField); //$NON-NLS-1$
	}

}
