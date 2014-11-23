/*******************************************************************************
 * Copyright 2013 Francesco Cina' Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 ******************************************************************************/
package com.jporm.core.factory;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jporm.core.session.reader.ArrayResultSetReader;
import com.jporm.core.session.reader.ArrayResultSetReaderUnique;
import com.jporm.core.session.reader.BigDecimalResultSetReader;
import com.jporm.core.session.reader.BigDecimalResultSetReaderUnique;
import com.jporm.core.session.reader.ListResultSetReader;
import com.jporm.core.session.reader.StringResultSetReader;
import com.jporm.core.session.reader.StringResultSetReaderUnique;
import com.jporm.session.ResultSetReader;

/**
 * <class_description>
 * <p>
 * <b>notes</b>:
 * <p>
 * ON : Mar 9, 2013
 *
 * @author - Francesco Cina
 * @version $Revision
 */
public class ObjectBuilder {

	private ObjectBuilder() {
	}

	public static final String[] EMPTY_STRING_ARRAY = new String[0];
	public static final List<String> EMPTY_STRING_LIST = Collections.EMPTY_LIST;
	public static final Map<String, Object> EMPTY_MAP = Collections.EMPTY_MAP;

	public static final ResultSetReader<String> RESULT_SET_READER_STRING_UNIQUE = new StringResultSetReaderUnique();
	public static final ResultSetReader<String> RESULT_SET_READER_STRING = new StringResultSetReader();
	public static final ResultSetReader<BigDecimal> RESULT_SET_READER_BIG_DECIMAL_UNIQUE = new BigDecimalResultSetReaderUnique();
	public static final ResultSetReader<BigDecimal> RESULT_SET_READER_BIG_DECIMAL = new BigDecimalResultSetReader();
	public static final ResultSetReader<Object[]> RESULT_SET_READER_ARRAY_UNIQUE = new ArrayResultSetReaderUnique();
	public static final ResultSetReader<Object[]> RESULT_SET_READER_ARRAY = new ArrayResultSetReader();
	public static final ResultSetReader<List<Object[]>> RESULT_SET_READER_LIST = new ListResultSetReader();

	public static <KEY, VALUE> Map<KEY, VALUE> newMaxSizeMap(final int maxSize) {
		return new LinkedHashMap<KEY, VALUE>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(final Map.Entry<KEY, VALUE> eldest) {
				return size() > maxSize;
			}
		};
	}

}
