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
package com.jporm.persistor.type.jdbc;

import java.sql.NClob;

import com.jporm.persistor.type.TypeWrapper;


public class NClobNullWrapper implements TypeWrapper<NClob, NClob> {

	@Override
	public Class<NClob> jdbcType() {
		return NClob.class;
	}

	@Override
	public Class<NClob> propertyType() {
		return NClob.class;
	}

	@Override
	public NClob wrap(final NClob value) {
		return value;
	}

	@Override
	public NClob unWrap(final NClob value) {
		return value;
	}

	@Override
	public NClob clone(final NClob source) {
		return source;
	}

}
