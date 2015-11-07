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

import com.jporm.annotation.Id;
import com.jporm.annotation.Table;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Nov 20, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
@Table(tableName = "EMPLOYEE")
public class EmployeeWithEnum {

    @Id
    private long id;
    private int age;
    private EmployeeName name;
    private EmployeeSurname surname;
    private String employeeNumber;

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @return the employeeNumber
     */
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the name
     */
    public EmployeeName getName() {
        return name;
    }

    /**
     * @return the surname
     */
    public EmployeeSurname getSurname() {
        return surname;
    }

    /**
     * @param age
     *            the age to set
     */
    public void setAge(final int age) {
        this.age = age;
    }

    /**
     * @param employeeNumber
     *            the employeeNumber to set
     */
    public void setEmployeeNumber(final String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(final long id) {
        this.id = id;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final EmployeeName name) {
        this.name = name;
    }

    /**
     * @param surname
     *            the surname to set
     */
    public void setSurname(final EmployeeSurname surname) {
        this.surname = surname;
    }

}
