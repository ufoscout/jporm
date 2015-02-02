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

import java.util.stream.Stream;

import com.jporm.core.query.delete.DeleteExecutionStrategy;
import com.jporm.core.query.update.UpdateExecutionStrategy;

public class QueryExecutionStrategyBatchUpdate implements QueryExecutionStrategy {

	@Override
	public int executeDelete(DeleteExecutionStrategy strategy) {
		return strategy.executeWithBatchUpdate();
	}

	@Override
	public <BEAN> Stream<BEAN> executeUpdate(UpdateExecutionStrategy<BEAN> strategy) {
		return strategy.executeWithBatchUpdate();
	}

}
