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
package com.jporm.test.domain.section01;

import java.util.Optional;

import com.jporm.annotation.Id;

/**
 *
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class Employee {

	@Id
	private Integer id;
	private int age;
	private String name;
	private String surname;
	private Optional<String> employeeNumber;

	public int getAge() {
		return age;
	}

	public Optional<String> getEmployeeNumber() {
		return employeeNumber;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public void setAge(final int age) {
		this.age = age;
	}

	public void setEmployeeNumber(final Optional<String> employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

}
