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

public abstract class AstractAccessorFactory implements AccessorFactory {

	@Override
	public final <BEAN, P> GetterSetter<BEAN, P> build(final Field field) {
		return new GetterSetter<BEAN, P>(buildGetter(field), buildSetter(field));
	}

	@Override
	public final <BEAN, P> GetterSetter<BEAN, P> build(final Field getField, final Method setMethod) {
		return new GetterSetter<BEAN, P>(buildGetter(getField), buildSetter(setMethod));
	}

	@Override
	public final <BEAN, P> GetterSetter<BEAN, P> build(final Method getMethod, final Field setField) {
		return new GetterSetter<BEAN, P>(buildGetter(getMethod), buildSetter(setField));
	}

	@Override
	public final <BEAN, P> GetterSetter<BEAN, P> build(final Method getMethod, final Method setMethod) {
		return new GetterSetter<BEAN, P>(buildGetter(getMethod), buildSetter(setMethod));
	}

}
