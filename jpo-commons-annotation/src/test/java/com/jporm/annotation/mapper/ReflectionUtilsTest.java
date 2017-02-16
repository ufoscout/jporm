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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Optional;

import org.junit.Test;

import com.jporm.annotation.BaseTestApi;
import com.jporm.annotation.Column;
import com.jporm.annotation.Generator;
import com.jporm.annotation.GeneratorType;
import com.jporm.annotation.Id;
import com.jporm.annotation.Table;

public class ReflectionUtilsTest extends BaseTestApi {

	@Test
	public void shuold_find_default_constructor_if_present() throws Exception {
		final Optional<Constructor<String>> stringConstructor = ReflectionUtils.getDefaultConstructor(String.class);
		assertTrue(stringConstructor.isPresent());
		final String newString = stringConstructor.get().newInstance();
		assertNotNull(newString);
	}

	@Test
	public void shuold_not_find_the_default_constructor_if_private() throws Exception {
		final Optional<Constructor<BeanWithPrivateConstructor>> constructor = ReflectionUtils.getDefaultConstructor(BeanWithPrivateConstructor.class);
		assertFalse(constructor.isPresent());
	}

	public static class BeanWithPrivateConstructor {
		public static BeanWithPrivateConstructor build() {return null;};
		private BeanWithPrivateConstructor() {}
	}

	@Test
	public void shuold_not_find_the_default_constructor_if_has_parameters() throws Exception {
		final Optional<Constructor<BeanWithParametersConstructor>> constructor = ReflectionUtils.getDefaultConstructor(BeanWithParametersConstructor.class);
		assertFalse(constructor.isPresent());
	}

	public static class BeanWithParametersConstructor {
		private BeanWithParametersConstructor(int param) {}
	}

	@Test
	public void shuold_find_static_methods() throws Exception {
		final Optional<Method> method = ReflectionUtils.getMethod(BeanWithPrivateConstructor.class, "build");
		assertTrue(method.isPresent());
		assertTrue(ReflectionUtils.isStatic(method.get()));
	}

	@Test
	public void should_return_method_annotations_from_parents() throws NoSuchMethodException, SecurityException {

		final Method one = LevelThree.class.getMethod("one");
		assertNotNull(one);
		assertTrue( ReflectionUtils.findAnnotation(LevelThree.class, one, Id.class).isPresent() );
		assertTrue( ReflectionUtils.findAnnotation(LevelThree.class, one, Generator.class).isPresent() );
		assertEquals( GeneratorType.SEQUENCE, ReflectionUtils.findAnnotation(LevelThree.class, one, Generator.class).get().generatorType() );

		final Method two = LevelThree.class.getMethod("two");
		assertNotNull(two);
		assertTrue( ReflectionUtils.findAnnotation(LevelThree.class, two, Id.class).isPresent() );
		assertTrue( ReflectionUtils.findAnnotation(LevelThree.class, two, Column.class).isPresent() );
		assertEquals( "THREE", ReflectionUtils.findAnnotation(LevelThree.class, two, Column.class).get().name() );

		final Method three = LevelThree.class.getMethod("three");
		assertNotNull(three);
		assertFalse( ReflectionUtils.findAnnotation(LevelThree.class, three, Id.class).isPresent() );
		assertTrue( ReflectionUtils.findAnnotation(LevelThree.class, three, Column.class).isPresent() );
		assertEquals( "THREE_FROM_TWO", ReflectionUtils.findAnnotation(LevelThree.class, three, Column.class).get().name() );

		final Method four = LevelThree.class.getMethod("four");
		assertNotNull(four);
		assertFalse( ReflectionUtils.findAnnotation(LevelThree.class, four, Id.class).isPresent() );
		assertTrue( ReflectionUtils.findAnnotation(LevelThree.class, four, Column.class).isPresent() );
		assertEquals( "FOUR_FROM_TWO", ReflectionUtils.findAnnotation(LevelThree.class, four, Column.class).get().name() );
	}

	@Test
	public void shuold_return_class_annotations_from_inherited_interfaces() throws Exception {
		final Optional<Method> method = ReflectionUtils.getMethod(BeanWithPrivateConstructor.class, "build");
		assertTrue(method.isPresent());
		assertTrue(ReflectionUtils.isStatic(method.get()));
	}

	@Table(tableName="LevelOneTable")
	interface LevelOne {
		@Id
		@Generator(generatorType=GeneratorType.NONE)
		void one();

		@Column(name="ONE")
		void two();

		void one(String arg);

		@Column(name="FOUR_FROM_ONE")
		void four();
	}

	abstract class LevelTwo implements LevelOne {
		@Override
		@Column(name="TWO")
		public abstract void two();
		@Column(name="THREE_FROM_TWO")
		public abstract String three();
		@Override
		@Column(name="FOUR_FROM_TWO")
		public abstract void four();
	}

	@Table(tableName="LevelThreeTable")
	class LevelThree extends LevelTwo {

		@Override
		@Generator(generatorType=GeneratorType.SEQUENCE)
		public void one() {}

		@Override
		public void one(String arg) {}

		@Override
		@Id
		@Column(name="THREE")
		public void two() {}

		@Override
		public String three() {return null;}

		@Override
		public void four() {}

	}

}
