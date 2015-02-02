/*******************************************************************************
 * Copyright 2015 Francesco Cina'
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
package com.jporm.core.query.strategy;

import static org.junit.Assert.*;

import java.util.stream.Stream;

import org.junit.Test;

import com.jporm.core.BaseTestApi;
import com.jporm.core.query.delete.DeleteExecutionStrategy;
import com.jporm.core.query.update.UpdateExecutionStrategy;

public class QueryExecutionStrategyTest extends BaseTestApi {

	@Test
	public void testDeleteStrategyWithBatchUpdate() {
		boolean returnsCountOfRowsInBatchUpdate = true;
		QueryExecutionStrategy strategy = QueryExecutionStrategy.build(returnsCountOfRowsInBatchUpdate);

		int result = strategy.executeDelete(new DeleteExecutionStrategy() {

			@Override
			public int executeWithSimpleUpdate() {
				return 0;
			}

			@Override
			public int executeWithBatchUpdate() {
				return 1;
			}
		});

		assertEquals(1, result);
	}

	@Test
	public void testDeleteStrategyWithSimpleUpdate() {
		boolean returnsCountOfRowsInBatchUpdate = false;
		QueryExecutionStrategy strategy = QueryExecutionStrategy.build(returnsCountOfRowsInBatchUpdate);

		int result = strategy.executeDelete(new DeleteExecutionStrategy() {

			@Override
			public int executeWithSimpleUpdate() {
				return 0;
			}

			@Override
			public int executeWithBatchUpdate() {
				return 1;
			}
		});

		assertEquals(0, result);
	}

	@Test
	public void testUpdateStrategyWithBatchUpdate() {
		boolean returnsCountOfRowsInBatchUpdate = true;
		QueryExecutionStrategy strategy = QueryExecutionStrategy.build(returnsCountOfRowsInBatchUpdate);

		Stream<Integer> result = strategy.executeUpdate(new UpdateExecutionStrategy<Integer>() {

			@Override
			public Stream<Integer> executeWithBatchUpdate() {
				return Stream.of(1);
			}

			@Override
			public Stream<Integer> executeWithSimpleUpdate() {
				return Stream.of(0);
			}
		});

		assertEquals(1, result.iterator().next().intValue());
	}

	@Test
	public void testUpdateStrategyWithSimpleUpdate() {
		boolean returnsCountOfRowsInBatchUpdate = false;
		QueryExecutionStrategy strategy = QueryExecutionStrategy.build(returnsCountOfRowsInBatchUpdate);

		Stream<Integer> result = strategy.executeUpdate(new UpdateExecutionStrategy<Integer>() {

			@Override
			public Stream<Integer> executeWithBatchUpdate() {
				return Stream.of(1);
			}

			@Override
			public Stream<Integer> executeWithSimpleUpdate() {
				return Stream.of(0);
			}
		});

		assertEquals(0, result.iterator().next().intValue());
	}
}
