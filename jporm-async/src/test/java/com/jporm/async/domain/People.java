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
package com.jporm.async.domain;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Date;

import com.jporm.annotation.Generator;
import com.jporm.annotation.Id;
import com.jporm.annotation.generator.GeneratorType;

/**
 *
 * @author Francesco Cina
 *
 * 05/giu/2011
 */
public class People  {

	@Id
	@Generator(generatorType = GeneratorType.SEQUENCE, name = "SEQ_PEOPLE")
	private Long id;
	private String firstname;
	private String lastname;
	private Date birthdate;
	private Date deathdate;
	private InputStream firstblob;
	private Blob secondblob;
	private Reader firstclob;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public Date getDeathdate() {
		return deathdate;
	}
	public void setDeathdate(Date deathdate) {
		this.deathdate = deathdate;
	}
	public InputStream getFirstblob() {
		return firstblob;
	}
	public void setFirstblob(InputStream firstblob) {
		this.firstblob = firstblob;
	}
	public Blob getSecondblob() {
		return secondblob;
	}
	public void setSecondblob(Blob secondblob) {
		this.secondblob = secondblob;
	}
	public void setFirstclob(Reader firstclob) {
		this.firstclob = firstclob;
	}
	public Reader getFirstclob() {
		return firstclob;
	}

}
