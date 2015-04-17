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
 *          ON : Mar 5, 2013
 * ----------------------------------------------------------------------------
 */
package com.jporm.commons.core.query.find.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.Test;

import com.jporm.commons.core.BaseCommonsCoreTestApi;
import com.jporm.commons.core.inject.ServiceCatalogImpl;

/**
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Mar 5, 2013
 *
 * @author Francesco Cina'
 * @version $Revision
 */
@SuppressWarnings("nls")
public class CacheStrategyImplTest extends BaseCommonsCoreTestApi{

	@Test
	public void testCache() {

		CacheStrategy cacheStrategy = new CacheStrategyImpl(new ServiceCatalogImpl());

		String sql = "sql" + UUID.randomUUID(); //$NON-NLS-1$
		List<Object> values = Arrays.asList(new Object[]{Integer.MAX_VALUE, Integer.MIN_VALUE});

		final ArrayList<Integer> result = new ArrayList<Integer>();

		final int howMany = 100;
		final AtomicBoolean callbackCalled = new AtomicBoolean(false);

		Consumer<List<Integer>> ifFoundCallback = new Consumer<List<Integer>>() {

			@Override
			public void accept(List<Integer> results) {
				for (int i=0; i<results.size(); i++) {
					result.add(results.get(i));
				}
			}
		};

		CacheStrategyCallback<Integer> csc = new CacheStrategyCallback<Integer>() {
			@Override
			public void doWhenNotInCache(final CacheStrategyEntry<Integer> cacheStrategyEntry) {
				callbackCalled.set(true);
				for (int i=0; i<howMany; i++) {
					Integer bean = new Random().nextInt();
					result.add(bean);
					cacheStrategyEntry.add(bean);
				}
				cacheStrategyEntry.end();
			}
		};

		String cacheName = "cacheName";
		//FIND NOT IN CACHE
		cacheStrategy.find(cacheName, sql, values, new ArrayList<String>(), ifFoundCallback, csc);
		assertTrue(callbackCalled.get());
		assertEquals(howMany, result.size());

		//FIND IN CACHE
		callbackCalled.set(false);
		final List<Integer> oldResult = (List<Integer>) result.clone();
		result.clear();
		cacheStrategy.find(cacheName, sql, values, new ArrayList<String>(), ifFoundCallback, csc);
		assertFalse(callbackCalled.get());
		assertEquals(howMany, result.size());
		assertEquals(oldResult, result);

	}

}
