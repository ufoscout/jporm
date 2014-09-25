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
package com.jporm.core.annotation.table;

import com.jporm.annotation.Table;
import com.jporm.core.util.FieldDefaultNaming;

/**
 * 
 * @author cinafr
 *
 */
public class TableInfoFactory {

	private TableInfoFactory() {}

	public static TableInfo getTableInfo(final Class<?> clazz) {
		String tableName =  FieldDefaultNaming.getJavanameToDBnameDefaultMapping(clazz.getSimpleName());
		String schemaName = ""; //$NON-NLS-1$
		final Table annotation = clazz.getAnnotation(Table.class);
		if ( annotation != null) {
			tableName = ((annotation.tableName()==null) || annotation.tableName().isEmpty()) ? tableName : annotation.tableName();
			schemaName = annotation.schemaName();
		}
		return new TableInfo(tableName, schemaName);
	}

}
