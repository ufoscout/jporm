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
package com.jporm.core.query.update;

import com.jporm.query.update.Update;

public abstract class AUpdate<BEAN> implements Update<BEAN> {

	private int queryTimeout;
	private boolean executed;

	@Override
	public BEAN now() {
		executed = true;
		return doNow();
	}

	protected abstract BEAN doNow();

	@Override
	public void execute() {
		now();
	}

	@Override
	public boolean isExecuted() {
		return executed;
	}

	@Override
	public Update<BEAN> timeout(int queryTimeout) {
		this.queryTimeout = queryTimeout;
		return this;
	}

	@Override
	public int getTimeout() {
		return queryTimeout;
	}



}
