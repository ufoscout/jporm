/*******************************************************************************
 * Copyright 2016 Francesco Cina'
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
package com.jporm.commons.core.inject;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.annotation.mapper.clazz.FieldDescriptor;
import com.jporm.commons.core.inject.valuegenerator.ValueGenerator;
import com.jporm.sql.dialect.DBProfile;

public interface ExtendedFieldDescriptor<BEAN, R, P> extends FieldDescriptor<BEAN, R, P> {

	static <BEAN, R, P> ExtendedFieldDescriptor<BEAN, R, P> get(ClassDescriptor<BEAN> descriptor, FieldDescriptor<BEAN, R, P> fieldDescriptor) {
		return new ExtendedFieldDescriptorImpl<>(descriptor, fieldDescriptor);
	}

	public ValueGenerator getGenerator(DBProfile dbProfile);

}
