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
package com.jporm.core.domain;

import com.jporm.annotation.Ignore;

/**
 * 
 * @author Francesco Cina
 *
 *         20/mag/2011
 */
public class Employee {

    private long id;
    private int age;
    private String name;
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

    public long getId() {
        return id;
    }

    public String getIgnoreMe() {
        return ignoreMe;
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

    public void setEmployeeNumber(final String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setIgnoreMe(final String ignoreMe) {
        this.ignoreMe = ignoreMe;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

}
