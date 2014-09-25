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
package com.jporm.core.persistor.type.jdbc;

import com.jporm.persistor.type.TypeWrapper;


public class StringNullWrapper implements TypeWrapper<String, String> {

	@Override
	public Class<String> jdbcType() {
		return String.class;
	}

	@Override
	public Class<String> propertyType() {
		return String.class;
	}

	@Override
	public String wrap(final String value) {
		return value;
	}

	@Override
	public String unWrap(final String value) {
		return value;
	}

	@Override
	public String clone(final String source) {
		return source;
	}

}
