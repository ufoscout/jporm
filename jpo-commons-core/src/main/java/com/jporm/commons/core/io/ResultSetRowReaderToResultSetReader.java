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
package com.jporm.commons.core.io;

import java.util.ArrayList;
import java.util.List;

import com.jporm.types.io.ResultSet;
import com.jporm.types.io.ResultSetReader;
import com.jporm.types.io.ResultSetRowReader;

/**
 *
 * @author ufo
 *
 */
public class ResultSetRowReaderToResultSetReader<T> implements ResultSetReader<List<T>> {

	private final ResultSetRowReader<T> rsrr;
	public ResultSetRowReaderToResultSetReader(final ResultSetRowReader<T> rsrr) {
		this.rsrr = rsrr;

	}
	@Override
	public List<T> read(final ResultSet resultSet) {
		final List<T> results = new ArrayList<T>();
		int rowNum = 0;
		while ( resultSet.next() ) {
			results.add(this.rsrr.readRow(resultSet, rowNum++));
		}
		return results;
	}

}
