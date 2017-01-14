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
package com.jporm.annotation.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jporm.annotation.BaseTestApi;

/**
 *
 * @author Francesco Cina
 *
 *         21/mag/2011
 */
public class FieldDefaultNamingTest extends BaseTestApi {

	@Test
	public void testNaming() {
		assertEquals("EMPLOYEE", FieldDefaultNaming.getDefaultMappingNameForMethod("getEmployee"));
		assertEquals("", FieldDefaultNaming.getDefaultMappingNameForMethod("Employee"));
		assertEquals("", FieldDefaultNaming.getDefaultMappingNameForMethod("GetEmployee"));
		assertEquals("", FieldDefaultNaming.getDefaultMappingNameForMethod(""));
		assertEquals("EMPLOYEE_MALE", FieldDefaultNaming.getDefaultMappingNameForMethod("isEmployeeMale"));
		assertEquals("EMPLOYEE_AGE", FieldDefaultNaming.getDefaultMappingNameForMethod("setEmployeeAge"));

		assertEquals("EMPLOYEE", FieldDefaultNaming.getJavanameToDBnameDefaultMapping("Employee"));
		assertEquals("EMPLOYEE_AGE", FieldDefaultNaming.getJavanameToDBnameDefaultMapping("EmployeeAge"));
		assertEquals("GET_EMPLOYEE", FieldDefaultNaming.getJavanameToDBnameDefaultMapping("getEmployee"));

		assertEquals("Employee", FieldDefaultNaming.getDBnameToJavanameDefaultMapping("EMPLOYEE", true));
		assertEquals("employee", FieldDefaultNaming.getDBnameToJavanameDefaultMapping("EMPLOYEE", false));
		assertEquals("EmployeeAge", FieldDefaultNaming.getDBnameToJavanameDefaultMapping("EMPLOYEE_AGE", true));
		assertEquals("employeeAge", FieldDefaultNaming.getDBnameToJavanameDefaultMapping("EMPLOYEE_AGE", false));
		assertEquals("GetEmployee", FieldDefaultNaming.getDBnameToJavanameDefaultMapping("GET_EMPLOYEE", true));
		assertEquals("getEmployee", FieldDefaultNaming.getDBnameToJavanameDefaultMapping("GET_EMPLOYEE", false));

	}

	@Test
	public void testRemove() {
		assertEquals("Employee", FieldDefaultNaming.removePrefix("get", "getEmployee"));
	}

	@Test
	public void testSetterGetterName() {
		assertEquals("getEmployee", FieldDefaultNaming.getDefaultGetterName("employee"));
		assertEquals("setEmployee", FieldDefaultNaming.getDefaultSetterName("employee"));
		assertEquals("withEmployee", FieldDefaultNaming.getDefaultWitherName("employee"));
		assertEquals("isEmployee", FieldDefaultNaming.getDefaultBooleanGetterName("employee"));
	}

}
