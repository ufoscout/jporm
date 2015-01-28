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
package com.jporm.annotation.mapper.clazz;

import com.jporm.annotation.Column;
import com.jporm.annotation.Id;
import com.jporm.annotation.Table;
import com.jporm.annotation.Version;

/**
 * 
 * @author Francesco Cina
 *
 * 08/giu/2011
 */

@Table(tableName = "ANNOTATION_TABLE_NAME", schemaName = "SCHEMA_NAME")
public class AnnotationBean4 {

	@Id
	private String index;

	public long columnNotAnnotated;

	@Version
	private long version1;

	@Version
	private long version2;

	@Id
	@Column(name = "ANNOTATION_COLUMN_NAME")
	Object columnAnnotated;

	public String getIndex() {
		return this.index;
	}

	public void setIndex(final String index) {
		this.index = index;
	}

	public long getVersion1() {
		return this.version1;
	}

	public void setVersion1(final long version1) {
		this.version1 = version1;
	}

	public long getVersion2() {
		return this.version2;
	}

	public void setVersion2(final long version2) {
		this.version2 = version2;
	}

}
