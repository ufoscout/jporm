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
package com.jporm.persistor.accessor.methodhandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.jporm.annotation.mapper.clazz.ValueProcessor;
import com.jporm.persistor.accessor.AccessorFactory;
import com.jporm.persistor.accessor.Getter;
import com.jporm.persistor.accessor.Setter;

public class MethodHandlerAccessorFactory implements AccessorFactory {

	@Override
	public <BEAN, R, P> Getter<BEAN, R, P> buildGetter(final Field field, ValueProcessor<R, P> valueProcessor) {
		return new MethodHandlerGetter<>(field, valueProcessor);
	}

	@Override
	public <BEAN, R, P> Getter<BEAN, R, P> buildGetter(final Method method, ValueProcessor<R, P> valueProcessor) {
		return new MethodHandlerGetter<>(method, valueProcessor);
	}

	@Override
	public <BEAN, R, P> Setter<BEAN, R, P> buildSetter(final Field field, ValueProcessor<R, P> valueProcessor) {
		return new MethodHandlerSetter<>(field, valueProcessor);
	}

	@Override
	public <BEAN, R, P> Setter<BEAN, R, P> buildSetter(final Method method, ValueProcessor<R, P> valueProcessor) {
		return new MethodHandlerSetter<>(method, valueProcessor);
	}

	@Override
	public <BEAN, R, P> Setter<BEAN, R, P> buildWither(Method method, ValueProcessor<R, P> valueProcessor) {
		return new MethodHandlerWither<>(method, valueProcessor);
	}

}
