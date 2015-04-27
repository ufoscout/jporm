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
package com.jporm.core.query.save.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.jporm.core.query.save.SaveOrUpdateQuery;

public class SaveOrUpdateQueryListDecorator<BEAN> implements SaveOrUpdateQuery<BEAN> {

	private final List<SaveOrUpdateQuery<BEAN>> queries = new ArrayList<>();
	private boolean executed;

	public void add(SaveOrUpdateQuery<BEAN> query) {
		queries.add(query);
	}

	public List<SaveOrUpdateQuery<BEAN>> getQueries() {
		return queries;
	}

	@Override
	public Stream<BEAN> execute() {
		executed = true;
		Stream<BEAN> stream = Stream.empty();
		for (SaveOrUpdateQuery<BEAN> updateQuery : queries ) {
			stream = Stream.concat(stream, updateQuery.execute());
		}
		return stream;
	}

}
