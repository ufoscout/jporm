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

import java.util.Optional;

import com.jporm.annotation.Ignore;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class EmployeeWithWitherAndOptional {

	private long id;
	private int age;
	private Optional<String> name;
	private String surname;
	private String employeeNumber;

	@Ignore
	private String ignoreMe;

	public int getAge() {
		return age;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public String getIgnoreMe() {
		return ignoreMe;
	}

	public String getSurname() {
		return surname;
	}

	public long id() {
		return id;
	}

	public EmployeeWithWitherAndOptional age(final int age) {
		this.age = age;
		return this;
	}

	public void setEmployeeNumber(final String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public void setIgnoreMe(final String ignoreMe) {
		this.ignoreMe = ignoreMe;
	}

	public EmployeeWithWitherAndOptional withSurname(final String surname) {
		this.surname = surname;
		return this;
	}

	/**
	 * @return the name
	 */
	public Optional<String> getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public EmployeeWithWitherAndOptional withName(Optional<String> name) {
		this.name = name;
		return this;
	}

}
