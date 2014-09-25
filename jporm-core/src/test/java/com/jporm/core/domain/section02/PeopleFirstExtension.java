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
package com.jporm.core.domain.section02;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Date;

import com.jporm.annotation.Column;
import com.jporm.annotation.Ignore;

public class PeopleFirstExtension extends PeopleBase {

	private Date birthdate;
	private Date deathdate;
	@Column(name="FIRSTBLOB")
	private InputStream blob;

	private Reader firstclob;

	@Ignore
	protected String toIgnore;

	/**
	 * @return the birthdate
	 */
	public Date getBirthdate() {
		return birthdate;
	}

	/**
	 * @param birthdate the birthdate to set
	 */
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	/**
	 * @return the deathdate
	 */
	public Date getDeathdate() {
		return deathdate;
	}

	/**
	 * @param deathdate the deathdate to set
	 */
	public void setDeathdate(Date deathdate) {
		this.deathdate = deathdate;
	}

	/**
	 * @return the blob
	 */
	public InputStream getBlob() {
		return blob;
	}

	/**
	 * @param blob the blob to set
	 */
	public void setBlob(InputStream blob) {
		this.blob = blob;
	}

	/**
	 * @return the firstclob
	 */
	public Reader getFirstclob() {
		return firstclob;
	}

	/**
	 * @param firstclob the firstclob to set
	 */
	public void setFirstclob(Reader firstclob) {
		this.firstclob = firstclob;
	}

}
