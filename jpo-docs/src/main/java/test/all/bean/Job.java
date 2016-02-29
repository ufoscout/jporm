/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package test.all.bean;

import com.jporm.annotation.Id;

public class Job {

    // @Id qualifies the current field as unique identifier for this bean
    @Id
    private String id;

    // This field is automatically associated with the database column called
    // USER_ID
    // At database level this column usually has a ForeingKey versus the USERS
    // table. In JPO it is just a plain value.
    private Long userId;

    // This field is automatically associated with the database column called
    // NUMBER
    private Integer number;

    // This field is automatically associated with the database column called
    // CAP
    private String cap;

    // This field is automatically associated with the database column called
    // STREET
    private String street;

    // This field is automatically associated with the database column called
    // CITY
    private String city;

    public String getCap() {
        return cap;
    }

    public String getCity() {
        return city;
    }

    public String getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public String getStreet() {
        return street;
    }

    public Long getUserId() {
        return userId;
    }

    public void setCap(final String cap) {
        this.cap = cap;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setNumber(final Integer number) {
        this.number = number;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

}
