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

import java.util.Optional;

import com.jporm.annotation.Table;
import com.jporm.annotation.mapper.FieldDefaultNaming;
import com.jporm.annotation.mapper.ReflectionUtils;

/**
 *
 * @author cinafr
 *
 */
public class TableInfoFactory {

	public static TableInfo getTableInfo(final Class<?> clazz) {
		String tableName = FieldDefaultNaming.getJavanameToDBnameDefaultMapping(clazz.getSimpleName());
		String schemaName = "";
		final Optional<Table> optionalAnnotation = ReflectionUtils.findAnnotation(clazz, Table.class);

		if (optionalAnnotation.isPresent()) {
			final Table annotation = optionalAnnotation.get();
			tableName = annotation.tableName() == null || annotation.tableName().isEmpty() ? tableName : annotation.tableName();
			schemaName = annotation.schemaName();
		}
		return new TableInfo(tableName, schemaName);
	}

	private TableInfoFactory() {
	}

}
