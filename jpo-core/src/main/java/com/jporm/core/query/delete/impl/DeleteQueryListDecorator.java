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
package com.jporm.core.query.delete.impl;

import java.util.ArrayList;
import java.util.List;

import com.jporm.core.query.delete.DeleteQuery;

public class DeleteQueryListDecorator implements DeleteQuery {

	private final List<DeleteQuery> deleteQueries = new ArrayList<>();
	private boolean executed;

	@Override
	public void execute() {
		now();
	}

	@Override
	public boolean isExecuted() {
		return executed;
	}

	@Override
	public int now() {
		executed = true;
		return deleteQueries.stream().mapToInt(query -> query.now()).sum();
	}

	public void add(DeleteQuery query) {
		deleteQueries.add(query);
	}

	public List<DeleteQuery> getDeleteQueries() {
		return deleteQueries;
	}

}