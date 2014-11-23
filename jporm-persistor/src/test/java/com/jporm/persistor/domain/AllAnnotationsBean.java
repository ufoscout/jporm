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
package com.jporm.persistor.domain;

import com.jporm.annotation.Column;
import com.jporm.annotation.Generator;
import com.jporm.annotation.Id;
import com.jporm.annotation.Table;
import com.jporm.annotation.Version;
import com.jporm.annotation.generator.GeneratorType;

/**
 *
 * @author cinafr
 *
 */
@Table(tableName = "ANNOTATIONS_BEAN", schemaName = "SCHEMA_NAME")
public class AllAnnotationsBean {

	@Id
	private String index1;

	@Id
	private String index2;

	@Generator(generatorType = GeneratorType.SEQUENCE, name = "ZOO_SEQ_PEOPLE")
	private long generatedField;

	private long columnNotAnnotated;

	@Column(name = "ANNOTATION_COLUMN_NAME")
	private Object columnAnnotated;

	@Column(name="BEAN_SIX")
	private Double bean6;

	@Version
	private long myVersion;

	public String getIndex1() {
		return index1;
	}

	public void setIndex1(final String index1) {
		this.index1 = index1;
	}

	public String getIndex2() {
		return index2;
	}

	public void setIndex2(final String index2) {
		this.index2 = index2;
	}

	public long getGeneratedField() {
		return generatedField;
	}

	public void setGeneratedField(final long generatedField) {
		this.generatedField = generatedField;
	}

	public long getColumnNotAnnotated() {
		return columnNotAnnotated;
	}

	public void setColumnNotAnnotated(final long columnNotAnnotated) {
		this.columnNotAnnotated = columnNotAnnotated;
	}

	public Object getColumnAnnotated() {
		return columnAnnotated;
	}

	public void setColumnAnnotated(final Object columnAnnotated) {
		this.columnAnnotated = columnAnnotated;
	}

	public long getMyVersion() {
		return myVersion;
	}

	public void setMyVersion(final long myVersion) {
		this.myVersion = myVersion;
	}

}
