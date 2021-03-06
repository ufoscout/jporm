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
package com.jporm.test.domain.section08;

import com.jporm.annotation.Column;
import com.jporm.annotation.Id;
import com.jporm.annotation.Table;
import com.jporm.annotation.Version;

@Table(tableName = "USERS")
public class UserWithTwoIDs {

    @Id
    private Long id;

    @Column(name = "AGE")
    private Long userAge;

    @Id
    private String firstname;
    private String lastname;

    @Version
    private Long version;

    public String getFirstname() {
        return firstname;
    }

    public Long getId() {
        return id;
    }

    public String getLastname() {
        return lastname;
    }

    /**
     * @return the age
     */
    public Long getUserAge() {
        return userAge;
    }

    public Long getVersion() {
        return version;
    }

    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    /**
     * @param age
     *            the age to set
     */
    public void setUserAge(final Long age) {
        userAge = age;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }

}
