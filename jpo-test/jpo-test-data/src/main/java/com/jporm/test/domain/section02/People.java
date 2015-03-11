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
package com.jporm.test.domain.section02;

import java.sql.Blob;
import java.sql.Date;

import com.jporm.annotation.Column;

/**
 * 
 * @author Francesco Cina
 *
 * 05/giu/2011
 */
public class People extends PeopleBase {

	private Date deathdate;
	@Column(name="SECONDBLOB")
	private Blob blob;

	public Date getDeathdate() {
		return deathdate;
	}
	public void setDeathdate(Date deathdate) {
		this.deathdate = deathdate;
	}
	public Blob getSecondblob() {
		return blob;
	}
	public void setSecondblob(Blob secondblob) {
		blob = secondblob;
	}

}
