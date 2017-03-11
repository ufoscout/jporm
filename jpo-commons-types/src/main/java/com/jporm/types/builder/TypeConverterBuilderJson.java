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

import java.util.function.Supplier;

import com.jporm.commons.json.JsonService;
import com.jporm.types.converter.JsonConverter;

public class TypeConverterBuilderJson<T>  implements TypeConverterBuilder<T, String> {

	private final Supplier<JsonService> jsonService;

	public TypeConverterBuilderJson(Supplier<JsonService> jsonService) {
		this.jsonService = jsonService;
	}

	@Override
	public JsonConverter<T> build(Class<T> pClass) {
		return new JsonConverter<>(pClass, jsonService.get());
	}

	@Override
	public boolean acceptType(Class<?> type) {
		return true;
	}

}
