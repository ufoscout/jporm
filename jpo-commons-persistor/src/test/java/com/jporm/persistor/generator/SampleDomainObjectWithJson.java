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
package com.jporm.persistor.generator;

import java.util.Optional;

import com.jporm.annotation.JsonType;
import com.jporm.annotation.Table;
import com.jporm.test.domain.section01.Employee;

//formatter:off
/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : 3-aug.-2012
 *
 * @author - vanroyni
 * @version $Revision
 */
// formatter:on
@Table(tableName = "UM_NOTIFICATION_PREFS")
public class SampleDomainObjectWithJson {

	@JsonType(deepCopy=false)
	private Employee jsonEmployee;

	@JsonType(deepCopy=true)
	private Optional<Employee> optionalJsonEmployee;

	/**
	 * @return the jsonEmployee
	 */
	public Employee getJsonEmployee() {
		return jsonEmployee;
	}

	/**
	 * @param jsonEmployee the jsonEmployee to set
	 */
	public void setJsonEmployee(Employee jsonEmployee) {
		this.jsonEmployee = jsonEmployee;
	}

	/**
	 * @return the optionalJsonEmployee
	 */
	public Optional<Employee> getOptionalJsonEmployee() {
		return optionalJsonEmployee;
	}

	/**
	 * @param optionalJsonEmployee the optionalJsonEmployee to set
	 */
	public void setOptionalJsonEmployee(Optional<Employee> optionalJsonEmployee) {
		this.optionalJsonEmployee = optionalJsonEmployee;
	}

}
