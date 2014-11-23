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
package com.jporm.introspector.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jporm.introspector.BaseTestApi;
import com.jporm.introspector.util.FieldDefaultNaming;

/**
 *
 * @author Francesco Cina
 *
 * 21/mag/2011
 */
public class FieldDefaultNamingTest extends BaseTestApi {

	@Test
	public void testNaming() {
		assertEquals( "EMPLOYEE" , FieldDefaultNaming.getDefaultMappingNameForMethod("getEmployee")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "" , FieldDefaultNaming.getDefaultMappingNameForMethod("Employee")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "" , FieldDefaultNaming.getDefaultMappingNameForMethod("GetEmployee")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "" , FieldDefaultNaming.getDefaultMappingNameForMethod("")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "EMPLOYEE_MALE" , FieldDefaultNaming.getDefaultMappingNameForMethod("isEmployeeMale")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "EMPLOYEE_AGE" , FieldDefaultNaming.getDefaultMappingNameForMethod("setEmployeeAge")); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals( "EMPLOYEE" , FieldDefaultNaming.getJavanameToDBnameDefaultMapping("Employee")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "EMPLOYEE_AGE" , FieldDefaultNaming.getJavanameToDBnameDefaultMapping("EmployeeAge")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "GET_EMPLOYEE" , FieldDefaultNaming.getJavanameToDBnameDefaultMapping("getEmployee")); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals( "Employee" , FieldDefaultNaming.getDBnameToJavanameDefaultMapping("EMPLOYEE", true)); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "employee" , FieldDefaultNaming.getDBnameToJavanameDefaultMapping("EMPLOYEE", false)); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "EmployeeAge" , FieldDefaultNaming.getDBnameToJavanameDefaultMapping("EMPLOYEE_AGE", true)); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "employeeAge" , FieldDefaultNaming.getDBnameToJavanameDefaultMapping("EMPLOYEE_AGE", false)); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "GetEmployee" , FieldDefaultNaming.getDBnameToJavanameDefaultMapping("GET_EMPLOYEE", true)); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "getEmployee" , FieldDefaultNaming.getDBnameToJavanameDefaultMapping("GET_EMPLOYEE", false)); //$NON-NLS-1$ //$NON-NLS-2$

	}

	@Test
	public void testRemove() {
		assertEquals( "Employee" , FieldDefaultNaming.removePrefix("get", "getEmployee") ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Test
	public void testSetterGetterName() {
		assertEquals( "getEmployee" , FieldDefaultNaming.getDefaultGetterName("employee") ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "setEmployee" , FieldDefaultNaming.getDefaultSetterName("employee") ); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals( "isEmployee" , FieldDefaultNaming.getDefaultBooleanGetterName("employee") ); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
