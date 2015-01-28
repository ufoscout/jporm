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
package com.jporm.annotation.introspector.table;

import com.jporm.annotation.Table;

/**
 * Info related to {@link Table} annotation
 * @author cinafr
 *
 */
public class TableInfo {

	private final String tableName;
	private final String schemaName;
	private final String tableNameWithSchema;

	public TableInfo(final String tableName , final String schemaName) {
		this.tableName = tableName;
		this.schemaName = schemaName;

		if (schemaName.length()==0) {
			this.tableNameWithSchema = tableName;
		} else {
			this.tableNameWithSchema =  schemaName + "." + getTableName(); //$NON-NLS-1$
		}
	}

	public String getTableName() {
		return this.tableName;
	}

	public String getSchemaName() {
		return this.schemaName;
	}

	public String getTableNameWithSchema() {
		return this.tableNameWithSchema;
	}

}
