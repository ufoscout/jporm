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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.jporm.BaseTestApi;
import com.jporm.domain.AnnotationBean1;
import com.jporm.domain.AnnotationBean2;
import com.jporm.domain.AnnotationBean3;
import com.jporm.domain.AnnotationBean4;
import com.jporm.domain.AnnotationBean7;
import com.jporm.domain.section01.Employee;
import com.jporm.exception.OrmConfigurationException;
import com.jporm.mapper.NullServiceCatalog;
import com.jporm.mapper.clazz.ClassMap;
import com.jporm.mapper.clazz.ClassMapBuilderImpl;

/**
 * 
 * @author Francesco Cina
 *
 * 08/giu/2011
 */
public class ClassDBMapReflectionTest extends BaseTestApi {

    @Test
    public void testClassDBMapper1() {

        final ClassMap<Employee> classDBMap = new ClassMapBuilderImpl<Employee>(Employee.class, new NullServiceCatalog()).generate();
        assertNotNull(classDBMap);

        assertEquals( "" , classDBMap.getTableInfo().getSchemaName() ); //$NON-NLS-1$
        assertEquals( "EMPLOYEE" , classDBMap.getTableInfo().getTableNameWithSchema() ); //$NON-NLS-1$

        assertEquals( "ID" , classDBMap.getClassFieldByJavaName( "id" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals( "AGE" , classDBMap.getClassFieldByJavaName( "age" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals( "NAME" , classDBMap.getClassFieldByJavaName( "name" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals( "SURNAME" , classDBMap.getClassFieldByJavaName( "surname" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals( "EMPLOYEE_NUMBER" , classDBMap.getClassFieldByJavaName( "employeeNumber" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(classDBMap.getPrimaryKeyColumnJavaNames());
        assertTrue(classDBMap.getPrimaryKeyColumnJavaNames().length==0);

        String versionField = ""; //$NON-NLS-1$
        for (String javaName : classDBMap.getAllColumnJavaNames()) {
            if (classDBMap.getClassFieldByJavaName(javaName).getVersionInfo().isVersionable()) {
                versionField = javaName;
            }
        }
        assertEquals("", versionField); //$NON-NLS-1$
    }

    @Test
    public void testClassDBMapper2() {
        final ClassMap<AnnotationBean1> classDBMap = new ClassMapBuilderImpl<AnnotationBean1>(AnnotationBean1.class, new NullServiceCatalog()).generate();
        assertNotNull(classDBMap);

        assertEquals( "" , classDBMap.getTableInfo().getSchemaName() ); //$NON-NLS-1$
        assertEquals( "ANNOTATION_TABLE_NAME" , classDBMap.getTableInfo().getTableNameWithSchema() ); //$NON-NLS-1$

        assertEquals( "INDEX" , classDBMap.getClassFieldByJavaName( "index" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals( "COLUMN_NOT_ANNOTATED" , classDBMap.getClassFieldByJavaName( "columnNotAnnotated" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals( "ANNOTATION_COLUMN_NAME" , classDBMap.getClassFieldByJavaName( "columnAnnotated" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(classDBMap.getPrimaryKeyColumnJavaNames());
        assertTrue(classDBMap.getPrimaryKeyColumnJavaNames().length==1);
        assertEquals( "index" , classDBMap.getPrimaryKeyColumnJavaNames()[0]); //$NON-NLS-1$

        String versionField = ""; //$NON-NLS-1$
        for (String javaName : classDBMap.getAllColumnJavaNames()) {
            if (classDBMap.getClassFieldByJavaName(javaName).getVersionInfo().isVersionable()) {
                versionField = javaName;
            }
        }
        assertEquals("", versionField); //$NON-NLS-1$
    }

    @Test
    public void testClassDBMapper3() {
        final ClassMap<AnnotationBean3> classDBMap = new ClassMapBuilderImpl<AnnotationBean3>(AnnotationBean3.class, new NullServiceCatalog()).generate();
        assertNotNull(classDBMap);

        assertEquals( "SCHEMA_NAME" , classDBMap.getTableInfo().getSchemaName() ); //$NON-NLS-1$
        assertEquals( "ANNOTATION_TABLE_NAME" , classDBMap.getTableInfo().getTableName() ); //$NON-NLS-1$
        assertEquals( "SCHEMA_NAME.ANNOTATION_TABLE_NAME" , classDBMap.getTableInfo().getTableNameWithSchema() ); //$NON-NLS-1$

        assertEquals( "INDEX" , classDBMap.getClassFieldByJavaName( "index" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals( "COLUMN_NOT_ANNOTATED" , classDBMap.getClassFieldByJavaName( "columnNotAnnotated" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals( "ANNOTATION_COLUMN_NAME" , classDBMap.getClassFieldByJavaName( "columnAnnotated" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(classDBMap.getPrimaryKeyColumnJavaNames());
        assertTrue(classDBMap.getPrimaryKeyColumnJavaNames().length == 2);
        assertTrue( Arrays.asList(classDBMap.getPrimaryKeyColumnJavaNames()).contains("index")); //$NON-NLS-1$
        assertTrue( Arrays.asList(classDBMap.getPrimaryKeyColumnJavaNames()).contains("columnAnnotated")); //$NON-NLS-1$

        String versionField = ""; //$NON-NLS-1$
        for (String javaName : classDBMap.getAllColumnJavaNames()) {
            if (classDBMap.getClassFieldByJavaName(javaName).getVersionInfo().isVersionable()) {
                versionField = javaName;
            }
        }
        assertEquals("version", versionField); //$NON-NLS-1$
    }

    @Test
    public void testClassDBMapper4() {
        final ClassMap<AnnotationBean2> classDBMap = new ClassMapBuilderImpl<AnnotationBean2>(AnnotationBean2.class, new NullServiceCatalog()).generate();
        assertNotNull(classDBMap);

        assertEquals( "SCHEMA_NAME" , classDBMap.getTableInfo().getSchemaName() ); //$NON-NLS-1$
        assertEquals( "ANNOTATION_BEAN2" , classDBMap.getTableInfo().getTableName() ); //$NON-NLS-1$
        assertEquals( "SCHEMA_NAME.ANNOTATION_BEAN2" , classDBMap.getTableInfo().getTableNameWithSchema() ); //$NON-NLS-1$

        assertEquals( "INDEX" , classDBMap.getClassFieldByJavaName( "index" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals( "COLUMN_NOT_ANNOTATED" , classDBMap.getClassFieldByJavaName( "columnNotAnnotated" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals( "COLUMN_NOT_ANNOTATED2" , classDBMap.getClassFieldByJavaName( "columnNotAnnotated2" ).getColumnInfo().getDBColumnName() ); //$NON-NLS-1$ //$NON-NLS-2$

        assertNotNull(classDBMap.getPrimaryKeyColumnJavaNames());
        assertTrue(classDBMap.getPrimaryKeyColumnJavaNames().length == 2);

        assertTrue( Arrays.asList(classDBMap.getPrimaryKeyColumnJavaNames()).contains("index")); //$NON-NLS-1$
        assertTrue( Arrays.asList(classDBMap.getPrimaryKeyColumnJavaNames()).contains("columnNotAnnotated2")); //$NON-NLS-1$
        
        String versionField = ""; //$NON-NLS-1$
        for (String javaName : classDBMap.getAllColumnJavaNames()) {
            if (classDBMap.getClassFieldByJavaName(javaName).getVersionInfo().isVersionable()) {
                versionField = javaName;
            }
        }
        assertEquals("", versionField); //$NON-NLS-1$
    }

    @Test
    public void testClassDBMapperShouldThrownExceptionForDuplicatedGenerator() {
        boolean onlyOneVersionAnnotationException = false;
        try {
            new ClassMapBuilderImpl<AnnotationBean7>(AnnotationBean7.class, new NullServiceCatalog()).generate();

        } catch (final OrmConfigurationException e) {
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
            new ClassMapBuilderImpl<AnnotationBean4>(AnnotationBean4.class, new NullServiceCatalog()).generate();
        } catch (final OrmConfigurationException e) {
            if (e.getMessage().contains("@Version")) { //$NON-NLS-1$
                onlyOneVersionAnnotationException = true;
            }
        }
        assertTrue(onlyOneVersionAnnotationException);
    }

}
