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

import com.jporm.annotation.Generator;
import com.jporm.annotation.GeneratorType;
import com.jporm.annotation.Id;

public class Job {

    // @Id qualifies the current field as unique identifier for this bean
    @Id
    // @Generator is used to indicate an auto generated field. The id is generated on the fly using a random UUID.
    @Generator(generatorType = GeneratorType.UUID)
    private String id;

    // This field is automatically associated with the database column called USER_ID
    // At database level this column usually has a ForeingKey versus the USERS table. In JPO it is just a plain value.
    private Long userId;

    // This field is automatically associated with the database column called NUMBER
    private Integer number;

    // This field is automatically associated with the database column called CAP
    private String cap;

    // This field is automatically associated with the database column called STREET
    private String street;

    // This field is automatically associated with the database column called CITY
    private String city;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
