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

import java.lang.reflect.Method;

import org.junit.Test;

import com.jporm.annotation.BaseTestApi;
import com.jporm.annotation.Column;
import com.jporm.annotation.Generator;
import com.jporm.annotation.GeneratorType;
import com.jporm.annotation.Id;

public class ReflectionUtilsTest extends BaseTestApi {

	@Test
	public void should_return_annotations_from_parents() throws NoSuchMethodException, SecurityException {

		final Method one = LevelThree.class.getMethod("one");
		assertNotNull(one);
		assertTrue( ReflectionUtils.findInheritedAnnotation(LevelThree.class, one, Id.class).isPresent() );
		assertTrue( ReflectionUtils.findInheritedAnnotation(LevelThree.class, one, Generator.class).isPresent() );
		assertEquals( GeneratorType.SEQUENCE, ReflectionUtils.findInheritedAnnotation(LevelThree.class, one, Generator.class).get().generatorType() );

		final Method two = LevelThree.class.getMethod("two");
		assertNotNull(two);
		assertTrue( ReflectionUtils.findInheritedAnnotation(LevelThree.class, two, Id.class).isPresent() );
		assertTrue( ReflectionUtils.findInheritedAnnotation(LevelThree.class, two, Column.class).isPresent() );
		assertEquals( "THREE", ReflectionUtils.findInheritedAnnotation(LevelThree.class, two, Column.class).get().name() );

		final Method three = LevelThree.class.getMethod("three");
		assertNotNull(three);
		assertFalse( ReflectionUtils.findInheritedAnnotation(LevelThree.class, three, Id.class).isPresent() );
		assertTrue( ReflectionUtils.findInheritedAnnotation(LevelThree.class, three, Column.class).isPresent() );
		assertEquals( "THREE_FROM_TWO", ReflectionUtils.findInheritedAnnotation(LevelThree.class, three, Column.class).get().name() );

		final Method four = LevelThree.class.getMethod("four");
		assertNotNull(four);
		assertFalse( ReflectionUtils.findInheritedAnnotation(LevelThree.class, four, Id.class).isPresent() );
		assertTrue( ReflectionUtils.findInheritedAnnotation(LevelThree.class, four, Column.class).isPresent() );
		assertEquals( "FOUR_FROM_TWO", ReflectionUtils.findInheritedAnnotation(LevelThree.class, four, Column.class).get().name() );
	}


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
