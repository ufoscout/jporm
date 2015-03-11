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
package com.jporm.core.session.reader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jporm.core.query.ResultSetReader;

/**
 * 
 * @author ufo
 *
 */
public class ListResultSetReader implements ResultSetReader<List<Object[]>> {

	@Override
	public List<Object[]> read(final ResultSet resultSet) throws SQLException {
		int columnNumber = resultSet.getMetaData().getColumnCount();
		List<Object[]> resultList = new ArrayList<Object[]>();
		while ( resultSet.next() ) {
			Object[] objectArray = new Object[columnNumber];
			for (int i=0 ; i<columnNumber ; i++) {
				objectArray[i] = resultSet.getObject(i+1);
			}
			resultList.add(objectArray);
		}
		return resultList;
	}

}
