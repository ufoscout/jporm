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

/**
 * 
 * @author Francesco Cina
 *
 * 08/giu/2011
 */

@Table(tableName = "ANNOTATION_TABLE_NAME")
public class AnnotationBean1 {

	@Id
	private String index;
	
	public long columnNotAnnotated;
	
	@Column(name = "ANNOTATION_COLUMN_NAME")
	Object columnAnnotated;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
	
}
