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
package com.jporm.types.converter;

import com.jporm.commons.json.JsonService;
import com.jporm.types.TypeConverter;
import com.jporm.types.jdbc.JdbcIO;
import com.jporm.types.jdbc.JdbcIOFactory;

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
	private boolean deepCopy = true;
	private final JdbcIO<String> jdbcIO = JdbcIOFactory.getString();

	public JsonConverter(Class<T> clazz, JsonService jsonService) {
		this.clazz = clazz;
		this.jsonService = jsonService;
	}

	@Override
	public T clone(T source) {
		if (deepCopy && source!=null) {
			return jsonService.fromJson(clazz, jsonService.toJson(source));
		}
		return source;
	}

	@Override
	public T fromJdbcType(String value) {
		if (value!=null) {
			return jsonService.fromJson(clazz, value);
		}
		return null;
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

	/**
	 * @return the deepCopy
	 */
	public boolean isDeepCopy() {
		return deepCopy;
	}

	/**
	 * @param deepCopy the deepCopy to set
	 */
	public void setDeepCopy(boolean deepCopy) {
		this.deepCopy = deepCopy;
	}

	@Override
	public JdbcIO<String> getJdbcIO() {
		return jdbcIO;
	}

}
