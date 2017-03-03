/*******************************************************************************
 * Copyright 2017 Francesco Cina'
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
package com.jporm.types.builder;

import com.jporm.commons.json.JsonService;
import com.jporm.types.TypeConverter;

public class TypeConverterBuilderJson<T>  implements TypeConverterBuilder<T, String> {

	private final JsonService jsonService;

	public TypeConverterBuilderJson(JsonService jsonService) {
		this.jsonService = jsonService;
	}

	@Override
	public TypeConverter<T, String> build(Class<T> pClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<String> jdbcType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean acceptType(Class<?> type) {
		// TODO Auto-generated method stub
		return false;
	}

}
