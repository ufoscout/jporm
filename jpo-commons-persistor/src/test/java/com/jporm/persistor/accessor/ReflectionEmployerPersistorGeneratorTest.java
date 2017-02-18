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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.annotation.mapper.clazz.ClassDescriptorBuilderImpl;
import com.jporm.core.domain.Employee;
import com.jporm.persistor.BaseTestApi;
import com.jporm.persistor.generator.Persistor;
import com.jporm.persistor.generator.PersistorGeneratorBean;
import com.jporm.types.TypeConverterFactory;
import com.jporm.types.io.ResultEntry;

/**
 *
 * @author Francesco Cina'
 *
 *         Mar 24, 2012
 */
public class ReflectionEmployerPersistorGeneratorTest extends BaseTestApi {

	private ClassDescriptor<Employee> classMapper;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Persistor<Employee> persistor;
	private Employee employee;

	private Map<String, Object> getValueMap(final String[] names, final Object[] values) {
		final Map<String, Object> valueMap = new HashMap<>();
		for (int i = 0; i < names.length; i++) {
			valueMap.put(names[i], values[i]);
		}
		return valueMap;
	}

	@Before
	public void setUp() throws Exception {
		classMapper = new ClassDescriptorBuilderImpl<>(Employee.class).build();
		assertNotNull(classMapper);
		persistor = new PersistorGeneratorBean().generate(classMapper, new TypeConverterFactory());
		assertNotNull(persistor);

		employee = new Employee();
		employee.setId(17);
		employee.setName("name"); //$NON-NLS-1$
		employee.setAge(100);
		employee.setSurname("surname"); //$NON-NLS-1$
		employee.setEmployeeNumber("employeeNumber"); //$NON-NLS-1$
	}

	@Test
	public void testAllNotGeneratedValues() {

		final String[] expectedFields = classMapper.getAllNotGeneratedColumnJavaNames();
		final Object[] allNotGeneratedValues = persistor.getPropertyValues(expectedFields, employee);

		logger.info("Expected file order:"); //$NON-NLS-1$
		// The order of the readed field must match this
		logger.info(Arrays.toString(expectedFields));

		assertEquals(expectedFields.length, allNotGeneratedValues.length);

		final Map<String, Object> valueMap = getValueMap(expectedFields, allNotGeneratedValues);

		assertEquals(employee.getId(), valueMap.get("id"));
		assertEquals(employee.getName(), valueMap.get("name"));
		assertEquals(employee.getAge(), valueMap.get("age"));
		assertEquals(employee.getSurname(), valueMap.get("surname"));
		assertEquals(employee.getEmployeeNumber(), valueMap.get("employeeNumber"));

	}

	@Test
	public void testAllValues() {

		final String[] expectedFields = classMapper.getAllColumnJavaNames();
		final Object[] allValuesValues = persistor.getPropertyValues(expectedFields, employee);

		logger.info("Expected file order:"); //$NON-NLS-1$
		// The order of the readed field must match this
		logger.info(Arrays.toString(expectedFields));

		assertEquals(expectedFields.length, allValuesValues.length);

		final Map<String, Object> valueMap = getValueMap(expectedFields, allValuesValues);

		assertEquals(employee.getId(), valueMap.get("id"));
		assertEquals(employee.getName(), valueMap.get("name"));
		assertEquals(employee.getAge(), valueMap.get("age"));
		assertEquals(employee.getSurname(), valueMap.get("surname"));
		assertEquals(employee.getEmployeeNumber(), valueMap.get("employeeNumber"));
	}

	@Test
	public void testGenerators() {

		assertFalse(persistor.hasGenerator());

	}

	@Test
	public void testMapRow() throws Exception {
		final ResultEntry rs = mock(ResultEntry.class);

		final long empId = new Random().nextLong();
		final int empAge = new Random().nextInt();
		final String empNumber = "empNumber"; //$NON-NLS-1$
		final String empName = "empName"; //$NON-NLS-1$
		final String empSurname = "empSurname"; //$NON-NLS-1$

		doReturn(empId).when(rs).getLong("id"); //$NON-NLS-1$
		doReturn(empAge).when(rs).getInt("age"); //$NON-NLS-1$
		when(rs.getString("employeeNumber")).thenReturn(empNumber); //$NON-NLS-1$
		when(rs.getString("name")).thenReturn(empName); //$NON-NLS-1$
		when(rs.getString("surname")).thenReturn(empSurname); //$NON-NLS-1$

		final Employee createdEntity = persistor.beanFromResultSet(rs, new ArrayList<String>());

		assertEquals(empId, createdEntity.getId());
		assertEquals(empAge, createdEntity.getAge());
		assertEquals(empNumber, createdEntity.getEmployeeNumber());
		assertEquals(empName, createdEntity.getName());
		assertEquals(empSurname, createdEntity.getSurname());
	}

	@Test
	public void testNotPrimaryKeyColumnJavaNames() {

		final String[] expectedFields = classMapper.getNotPrimaryKeyColumnJavaNames();
		final Object[] notPrimaryKeyValues = persistor.getPropertyValues(expectedFields, employee);

		logger.info("Expected file order:"); //$NON-NLS-1$
		// The order of the readed field must match this
		logger.info(Arrays.toString(expectedFields));

		assertEquals(expectedFields.length, notPrimaryKeyValues.length);
		final Map<String, Object> valueMap = getValueMap(expectedFields, notPrimaryKeyValues);

		assertEquals(employee.getId(), valueMap.get("id"));
		assertEquals(employee.getName(), valueMap.get("name"));
		assertEquals(employee.getAge(), valueMap.get("age"));
		assertEquals(employee.getSurname(), valueMap.get("surname"));
		assertEquals(employee.getEmployeeNumber(), valueMap.get("employeeNumber"));
	}

	@Test
	public void testPrimaryKeyColumnJavaNames() {

		final String[] expectedFields = classMapper.getPrimaryKeyColumnJavaNames();
		final Object[] primaryKeyValues = persistor.getPropertyValues(expectedFields, employee);

		logger.info("Expected file order:"); //$NON-NLS-1$
		// The order of the readed field must match this
		logger.info(Arrays.toString(expectedFields));

		assertEquals(expectedFields.length, primaryKeyValues.length);

		assertTrue(primaryKeyValues.length == 0);
	}

	@Test
	public void testUpdatePrimaryKey() {
		final ResultEntry rs = mock(ResultEntry.class);

		employee = persistor.updateGeneratedValues(rs, employee);

		assertEquals(17, employee.getId());
		assertEquals(100, employee.getAge());
		assertEquals("employeeNumber", employee.getEmployeeNumber()); //$NON-NLS-1$
		assertEquals("name", employee.getName()); //$NON-NLS-1$
		assertEquals("surname", employee.getSurname()); //$NON-NLS-1$
	}

	@Test
	public void testVersion() {
		// Check no exceptions are thrown
		persistor.increaseVersion(employee, true);
		persistor.increaseVersion(employee, false);
	}

}
