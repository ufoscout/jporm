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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jporm.persistor.accessor.lambdametafactory.LambaMetafactoryAccessorFactory;
import com.jporm.persistor.accessor.methodhandler.MethodHandlerAccessorFactory;

public class BeanPropertyAccessorFactory extends AstractAccessorFactory {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private AccessorFactory lambaAccessorFactory = new LambaMetafactoryAccessorFactory();
	private AccessorFactory mhAccessorFactory = new MethodHandlerAccessorFactory();
	//private AccessorFactory reflectionAccessorFactory = new ReflectionAccessorFactory();

	@Override
	public <BEAN, P> Setter<BEAN, P> buildSetter(Method method) {
		Setter<BEAN, P> setter = null;
		try {
			setter = lambaAccessorFactory.buildSetter(method);
		} catch (RuntimeException e) {
			logger.debug("Cannot use lamba setter accessor for method [{}] of class [{}], fallback to MethodHanderAccessor", method.getName(), method.getDeclaringClass().getName());
			setter = mhAccessorFactory.buildSetter(method);
		}
		return setter;
	}

	@Override
	public <BEAN, P> Setter<BEAN, P> buildSetter(Field field) {
		Setter<BEAN, P> setter = null;
		try {
			setter = lambaAccessorFactory.buildSetter(field);
		} catch (RuntimeException e) {
			logger.debug("Cannot use lamba setter accessor for field [{}] of class [{}], fallback to MethodHanderAccessor", field.getName(), field.getDeclaringClass().getName());
			setter = mhAccessorFactory.buildSetter(field);
		}
		return setter;
	}

	@Override
	public <BEAN, P> Getter<BEAN, P> buildGetter(Method method) {
		Getter<BEAN, P> setter = null;
		try {
			setter = lambaAccessorFactory.buildGetter(method);
		} catch (RuntimeException e) {
			logger.debug("Cannot use lamba getter accessor for field [{}] of class [{}], fallback to MethodHanderAccessor", method.getName(), method.getDeclaringClass().getName());
			setter = mhAccessorFactory.buildGetter(method);
		}
		return setter;
	}

	@Override
	public <BEAN, P> Getter<BEAN, P> buildGetter(Field field) {
		Getter<BEAN, P> setter = null;
		try {
			setter = lambaAccessorFactory.buildGetter(field);
		} catch (RuntimeException e) {
			logger.debug("Cannot use lamba getter accessor for field [{}] of class [{}], fallback to MethodHanderAccessor", field.getName(), field.getDeclaringClass().getName());
			setter = mhAccessorFactory.buildGetter(field);
		}
		return setter;
	}


}
