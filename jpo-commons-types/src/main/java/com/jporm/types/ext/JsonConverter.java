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
package com.jporm.types.ext;

import com.jporm.commons.json.JsonService;
import com.jporm.types.TypeConverter;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Nov 22, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class JsonConverter<T> implements TypeConverter<T, String> {

	private final Class<T> clazz;
	private final JsonService jsonService;

	public JsonConverter(Class<T> clazz, JsonService jsonService) {
		this.clazz = clazz;
		this.jsonService = jsonService;
	}

	@Override
	public T clone(T source) {
		final int todo;
		//Should the source be returned or a deep clone?
		return jsonService.fromJson(clazz, jsonService.toJson(source));
	}

	@Override
	public T fromJdbcType(String value) {
		if (value!=null) {
			return jsonService.fromJson(clazz, value);
		}
		return null;
	}

	@Override
	public Class<String> jdbcType() {
		return String.class;
	}

	@Override
	public Class<T> propertyType() {
		return clazz;
	}

	@Override
	public String toJdbcType(T value) {
		if (value!=null) {
			return jsonService.toJson(value);
		}
		return null;
	}


}
