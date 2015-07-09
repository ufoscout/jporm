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
package com.jporm.rx.vertx.session.vertx3.jdbcclient;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jporm.types.io.Statement;

import io.vertx.core.json.JsonArray;

public class Vertx3Statement implements Statement {

	private List<Object> params = new ArrayList<>();

	@Override
	public void setBigDecimal(int parameterIndex, BigDecimal x) {
		params.add(x);

	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x) {
		params.add(x);

	}

	@Override
	public void setBoolean(int parameterIndex, boolean x) {
		params.add(x);

	}

	@Override
	public void setByte(int parameterIndex, byte x) {
		params.add(x);

	}

	@Override
	public void setBytes(int parameterIndex, byte[] x) {
		params.add(x);

	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader x) {
		params.add(x);
	}

	@Override
	public void setDate(int parameterIndex, Date x) {
		params.add(x);

	}

	@Override
	public void setDouble(int parameterIndex, double x) {
		params.add(x);

	}

	@Override
	public void setFloat(int parameterIndex, float x) {
		params.add(x);

	}

	@Override
	public void setInt(int parameterIndex, int x) {
		params.add(x);

	}

	@Override
	public void setLong(int parameterIndex, long x) {
		params.add(x);

	}

	@Override
	public void setObject(int parameterIndex, Object x) {
		params.add(x);

	}

	@Override
	public void setShort(int parameterIndex, short x) {
		params.add(x);

	}

	@Override
	public void setString(int parameterIndex, String x) {
		params.add(x);

	}

	public JsonArray getParams() {
		return new JsonArray(params);
	}

	@Override
	public void setInstant(int parameterIndex, Instant instant) {
		int toBeImplemented;
		// TODO Auto-generated method stub

	}

	@Override
	public void setLocalDateTime(int parameterIndex, LocalDateTime date) {
		int toBeImplemented;
		// TODO Auto-generated method stub

	}

	@Override
	public void setLocalDate(int parameterIndex, LocalDate date) {
		int toBeImplemented;
		// TODO Auto-generated method stub

	}

}
