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
package com.jporm.core.domain;

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
public class AnnotationBean3 {

	@Id
	private String index;

	public long columnNotAnnotated;

	@Version
	private long version;

	@Id
	@Column(name = "ANNOTATION_COLUMN_NAME")
	Object columnAnnotated;

	public String getIndex() {
		return this.index;
	}

	public void setIndex(final String index) {
		this.index = index;
	}

	public void setVersion(final long version) {
		this.version = version;
	}

	public long getVersion() {
		return this.version;
	}


}
