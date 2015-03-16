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
/* ----------------------------------------------------------------------------
 *     PROJECT : JPOrm
 *
 *  CREATED BY : Francesco Cina'
 *          ON : Mar 14, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.commons.core.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.jporm.commons.core.BaseCommonsCoreTestApi;
import com.jporm.commons.core.query.cache.impl.SqlCacheImpl;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 14, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
public class AQueryRootTest extends BaseCommonsCoreTestApi {

	private final AtomicInteger renderCalled = new AtomicInteger(0);
	private final AtomicInteger version = new AtomicInteger(0);

	private final AQueryRoot smartQuery = new AQueryRoot(new SqlCacheImpl()) {

		@Override
		public void appendValues(final List<Object> values) {
			getLogger().info("called"); //$NON-NLS-1$
		}

		@Override
		public int getVersion() {
			getLogger().info("called"); //$NON-NLS-1$
			return version.get();
		}

		@Override
		public void renderSql(final StringBuilder queryBuilder) {
			queryBuilder.append(UUID.randomUUID());
			getLogger().info("called"); //$NON-NLS-1$
			renderCalled.incrementAndGet();
		}
	};

	@Test
	public void testVersioning() {

		assertEquals( 0 , renderCalled.get() );
		assertEquals( 0 , version.get() );

		String render = smartQuery.renderSql();
		assertEquals( 1 , renderCalled.get() );
		assertEquals( 0 , version.get() );

		assertEquals(render, smartQuery.renderSql());
		assertEquals( 1 , renderCalled.get() );
		assertEquals( 0 , version.get() );

		version.getAndIncrement();
		String newRender = smartQuery.renderSql();
		assertFalse(render.equals(newRender));
		assertEquals( 2 , renderCalled.get() );
		assertEquals( 1 , version.get() );

		assertEquals(newRender, smartQuery.renderSql());
		assertEquals( 2 , renderCalled.get() );
		assertEquals( 1 , version.get() );
	}

}
