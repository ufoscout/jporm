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
package com.jporm.annotation.mapper.immutables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.jporm.annotation.BaseTestApi;
import com.jporm.annotation.mapper.clazz.ClassDescriptor;
import com.jporm.annotation.mapper.clazz.ClassDescriptorBuilderImpl;
import com.jporm.annotation.mapper.clazz.FieldDescriptor;

public class ImmutablesClassDescriptorTest extends BaseTestApi {

	@Test
	public void should_create_class_descriptor_for_immutables() {
		final ClassDescriptor<ImmutableBeanValue> cd = new ClassDescriptorBuilderImpl<>(ImmutableBeanValue.class).build();
		assertNotNull(cd);

		assertEquals( "IMMUTABLE", cd.getTableInfo().getTableName());

		{
			final FieldDescriptor<ImmutableBeanValue, Object> id = cd.getFieldDescriptorByJavaName("id");
			assertTrue( id.getGetter().getAccessor().isPresent() );
			assertTrue( id.getSetter().getAccessor().isPresent() );
			assertTrue( id.isIdentifier() );
			assertEquals( "ID", id.getColumnInfo().getDBColumnName() );
		}

		{
			assertFalse( Arrays.asList(cd.getAllColumnJavaNames()).contains("isIgnored") );
		}

		{
			final FieldDescriptor<ImmutableBeanValue, Object> version = cd.getFieldDescriptorByJavaName("version");
			assertTrue( version.getGetter().getAccessor().isPresent() );
			assertTrue( version.getSetter().getAccessor().isPresent() );
			assertTrue( version.getVersionInfo().isVersionable() );
			assertEquals( "VERSION", version.getColumnInfo().getDBColumnName() );
		}

		{
			final FieldDescriptor<ImmutableBeanValue, Object> columnOne = cd.getFieldDescriptorByJavaName("columnOne");
			assertTrue( columnOne.getGetter().getAccessor().isPresent() );
			assertTrue( columnOne.getSetter().getAccessor().isPresent() );
			assertEquals( "ANNOTATED_COLUMN", columnOne.getColumnInfo().getDBColumnName() );
		}

		{
			final FieldDescriptor<ImmutableBeanValue, Object> columnTwo = cd.getFieldDescriptorByJavaName("columnTwo");
			assertTrue( columnTwo.getGetter().getAccessor().isPresent() );
			assertTrue( columnTwo.getSetter().getAccessor().isPresent() );
			assertEquals( "COLUMN_TWO", columnTwo.getColumnInfo().getDBColumnName() );
		}

		{
			final FieldDescriptor<ImmutableBeanValue, Object> columnThree = cd.getFieldDescriptorByJavaName("columnThree");
			assertTrue( columnThree.getGetter().getAccessor().isPresent() );
			assertTrue( columnThree.getSetter().getAccessor().isPresent() );
			assertEquals( "COLUMN_THREE", columnThree.getColumnInfo().getDBColumnName() );
		}

		{
			final FieldDescriptor<ImmutableBeanValue, Object> columnFour = cd.getFieldDescriptorByJavaName("columnFour");
			assertTrue( columnFour.getGetter().getAccessor().isPresent() );
			assertTrue( columnFour.getSetter().getAccessor().isPresent() );
			assertEquals( "ANNOTATED_OPTIONAL_COLUMN", columnFour.getColumnInfo().getDBColumnName() );
		}
	}

}
