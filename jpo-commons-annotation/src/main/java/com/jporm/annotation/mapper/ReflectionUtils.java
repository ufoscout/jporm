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
package com.jporm.annotation.mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ReflectionUtils {

	/**
	 *
	 * Return all fields of a class including the ones inherited from super classes
	 *
	 * @param type
	 * @return
	 */
	public static List<Field> getAllInheritedFields(final Class<?> type) {
		final List<Field> fields = new ArrayList<>();
		for (Class<?> c = type; c != null; c = c.getSuperclass()) {
			fields.addAll(Arrays.asList(c.getDeclaredFields()));
		}
		return fields;
	}

	/**
	 *
	 * Return annotations on the element including Annotations on overridden methods.
	 *
	 * In case a class extends from a super class that implements an interface, annotations on the interface have the priority.
	 *
	 * @param method
	 * @param annotationType
	 * @return
	 */
	public static <A extends Annotation, C> Optional<A> findInheritedAnnotation(Class<C> ownerClass, Method method, Class<A> annotationType) {
		if (method.getAnnotation(annotationType)!=null) {
			return Optional.of(method.getAnnotation(annotationType));
		}

		for (Class<?> c = ownerClass; c != null; c = c.getSuperclass()) {
			final Optional<Method> cMethod = getMethod(c, method.getName(), method.getParameterTypes());
			if (cMethod.isPresent() && cMethod.get().getAnnotation(annotationType)!=null) {
				return Optional.of(cMethod.get().getAnnotation(annotationType));
			}
		}

		return Optional.empty();
	}

	/**
	 * It returns the {@link Annotation} if present in the {@link Field}, in its getter or its setter including inherited ones.
	 *
	 * @param ownerClass
	 * @param field
	 * @param getter
	 * @param setter
	 * @param annotationType
	 * @return
	 */
	public static <A extends Annotation, C> Optional<A> findAnnotation(Class<C> ownerClass, Field field, Optional<Method> getter, Optional<Method> setter, Class<A> annotationType) {
		final Optional<A> fromField = Optional.ofNullable(field.getAnnotation(annotationType));
		if (fromField.isPresent()) {
			return fromField;
		}
		final Optional<A> fromGetter = getter.flatMap(get -> findInheritedAnnotation(ownerClass, get, annotationType));
		if (fromGetter.isPresent()) {
			return fromGetter;
		}
		final Optional<A> fromSetter = setter.flatMap(set -> findInheritedAnnotation(ownerClass, set, annotationType));
		if (fromSetter.isPresent()) {
			return fromSetter;
		}
		return Optional.empty();
	}

	public static <C> Optional<Method> getMethod(Class<C> ownerClass, String name, Class<?>... parameterTypes) {
		try {
			return Optional.of( ownerClass.getMethod(name, parameterTypes) );
		} catch (final Exception e) {
			return Optional.empty();
		}
	}
}
