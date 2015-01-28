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
package com.jporm.types;

import com.jporm.types.ext.EnumWrapper;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Nov 21, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
@SuppressWarnings("rawtypes")
public class TypeWrapperBuilderEnum implements TypeWrapperBuilder<Enum, String> {

	@Override
	public Class<String> jdbcType() {
		return String.class;
	}

	@Override
	public Class<Enum> propertyType() {
		return Enum.class;
	}

	@Override
	public TypeWrapper<Enum, String> build(final Class<Enum> pClass) {
		return new EnumWrapper(pClass);
	}

}
