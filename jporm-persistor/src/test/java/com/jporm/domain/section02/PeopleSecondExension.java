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
package com.jporm.domain.section02;

import java.sql.Blob;

import com.jporm.annotation.Column;
import com.jporm.annotation.Table;

/**
 * 
 * @author Francesco Cina'
 *
 * Apr 21, 2012
 */
@Table(tableName="PEOPLE")
public class PeopleSecondExension extends PeopleFirstExtension {

	@Column(name="SECONDBLOD")
	private Blob anotherblob;

	public Blob getAnotherblob() {
		return anotherblob;
	}

	public void setAnotherblob(Blob anotherblob) {
		this.anotherblob = anotherblob;
	}

}
