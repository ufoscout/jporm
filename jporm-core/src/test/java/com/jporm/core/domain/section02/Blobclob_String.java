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

import com.jporm.annotation.Generator;
import com.jporm.annotation.Table;
import com.jporm.annotation.generator.GeneratorType;

/**
 * 
 * @author Francesco Cina
 *
 * 08/giu/2011
 */
@Table(tableName="BLOBCLOB")
public class Blobclob_String {

	private byte[] blob;
	private String clob;
	
	@Generator(generatorType = GeneratorType.SEQUENCE, name = "SEQ_BLOBCLOB")
	private long id;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public byte[] getBlob() {
		return blob;
	}
	public void setBlob(byte[] blob) {
		this.blob = blob;
	}
	public String getClob() {
		return clob;
	}
	public void setClob(String clob) {
		this.clob = clob;
	}
	
	
	
}
