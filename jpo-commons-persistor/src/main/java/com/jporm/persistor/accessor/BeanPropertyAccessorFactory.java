/*******************************************************************************
 * Copyright 2014 Francesco Cina'
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
package com.jporm.persistor.accessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.jporm.persistor.accessor.methodhandler.MethodHandlerAccessorFactory;

public class BeanPropertyAccessorFactory implements AccessorFactory {

	private final AccessorFactory mhAccessorFactory = new MethodHandlerAccessorFactory();

	@Override
	public <BEAN, P> Getter<BEAN, P> buildGetter(final Field field) {
		return mhAccessorFactory.buildGetter(field);
	}

	@Override
	public <BEAN, P> Getter<BEAN, P> buildGetter(final Method method) {
		return mhAccessorFactory.buildGetter(method);
	}

	@Override
	public <BEAN, P> Setter<BEAN, P> buildSetter(final Field field) {
		return mhAccessorFactory.buildSetter(field);
	}

	@Override
	public <BEAN, P> Setter<BEAN, P> buildSetter(final Method method) {
		return mhAccessorFactory.buildSetter(method);
	}

}
