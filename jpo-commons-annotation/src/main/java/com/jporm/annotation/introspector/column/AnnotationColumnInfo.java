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
package com.jporm.annotation.introspector.column;

import com.jporm.annotation.Column;

/**
 * It returns the column name based on the name set in the {@link Column} annotation
 * @author cinafr
 *
 */
public class AnnotationColumnInfo implements ColumnInfo {

	private String javaColumnName;

	public AnnotationColumnInfo(final String javaColumnName) {
		this.javaColumnName = javaColumnName;
	}

	@Override
	public String getDBColumnName() {
		return this.javaColumnName;
	}

}
