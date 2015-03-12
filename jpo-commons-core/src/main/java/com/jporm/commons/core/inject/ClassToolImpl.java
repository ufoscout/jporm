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
package com.jporm.commons.core.inject;

import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.persistor.Persistor;


/**
 *
 * @author Francesco Cina
 *
 * 22/mag/2011
 */
public class ClassToolImpl<BEAN> implements ClassTool<BEAN> {

	private final ClassDescriptor<BEAN> descriptor;
	private final Persistor<BEAN> persistor;

	public ClassToolImpl(final ClassDescriptor<BEAN> descriptor, final Persistor<BEAN> ormPersistor) {
		this.descriptor = descriptor;
		this.persistor = ormPersistor;
	}

	@Override
	public ClassDescriptor<BEAN> getDescriptor() {
		return this.descriptor;
	}

	@Override
	public Persistor<BEAN> getPersistor() {
		return this.persistor;
	}

}
