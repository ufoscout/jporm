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
package com.jporm.types.ext;

import java.sql.Timestamp;
import java.util.Date;

import com.jporm.types.TypeWrapper;

/**
 * 
 * @author Francesco Cina'
 *
 * Mar 27, 2012
 */
public class UtilDateToSqlTimestampWrapper implements TypeWrapper<Date, Timestamp> {

	@Override
	public Class<Timestamp> jdbcType() {
		return Timestamp.class;
	}

	@Override
	public Class<Date> propertyType() {
		return Date.class;
	}

	@Override
	public Date wrap(final Timestamp value) {
		return value;
	}

	@Override
	public Timestamp unWrap(final Date value) {
		if (value==null) {
			return null;
		}
		return new Timestamp(value.getTime());
	}

	@Override
	public Date clone(final Date source) {
		return source;
	}

}
