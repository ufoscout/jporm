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
import java.util.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.jporm.commons.core.exception.JpoException;

import io.vertx.core.json.JsonArray;

public class Vertx3GeneratedKeysResultSet implements com.jporm.types.io.ResultSet {

	private final JsonArray keys;
	private int position = -1;
	private final List<String> generatedColumnNames;

	public Vertx3GeneratedKeysResultSet(JsonArray keys, String[] generatedColumnNames) {
		this.keys = keys;
		this.generatedColumnNames = Arrays.asList(generatedColumnNames);
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) {
		return BigDecimal.valueOf(getLong(columnIndex));
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) {
		return BigDecimal.valueOf(getLong(columnLabel));
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) {
		throw new JpoException("Type not supported");

	}

	@Override
	public InputStream getBinaryStream(String columnLabel) {
		throw new JpoException("Type not supported");

	}

	@Override
	public boolean getBoolean(int columnIndex) {
		return orDefault(keys.getBoolean(columnIndex), false);
	}

	@Override
	public boolean getBoolean(String columnLabel) {
		return getBoolean(getPosition(columnLabel));
	}

	@Override
	public byte getByte(int columnIndex) {
		throw new JpoException("Type not supported");
	}

	@Override
	public byte getByte(String columnLabel) {
		throw new JpoException("Type not supported");
	}

	@Override
	public byte[] getBytes(int columnIndex) {
		return keys.getBinary(columnIndex);
	}

	@Override
	public byte[] getBytes(String columnLabel) {
		return getBytes(getPosition(columnLabel));
	}

	@Override
	public Reader getCharacterStream(int columnIndex) {
		throw new JpoException("Type not supported");

	}

	@Override
	public Reader getCharacterStream(String columnLabel) {
		throw new JpoException("Type not supported");

	}

	@Override
	public Date getDate(int columnIndex) {
		return new Date(getLong(columnIndex));
	}

	@Override
	public Date getDate(String columnLabel) {
		return new Date(getLong(columnLabel));
	}

	@Override
	public double getDouble(int columnIndex) {
		return orDefault(keys.getDouble(columnIndex), 0d);
	}

	@Override
	public double getDouble(String columnLabel) {
		return getDouble(getPosition(columnLabel));
	}

	@Override
	public float getFloat(int columnIndex) {
		return orDefault(keys.getFloat(columnIndex), 0f);
	}

	@Override
	public float getFloat(String columnLabel) {
		return getFloat(getPosition(columnLabel));
	}

	@Override
	public int getInt(int columnIndex) {
		return orDefault(keys.getInteger(columnIndex), 0);
	}

	@Override
	public int getInt(String columnLabel) {
		return getInt(getPosition(columnLabel));
	}

	@Override
	public long getLong(int columnIndex) {
		int fixMe;
		return orDefault(keys.getLong(columnIndex-1), 0l);
	}

	@Override
	public long getLong(String columnLabel) {
		return getLong(getPosition(columnLabel));
	}

	@Override
	public Object getObject(int columnIndex) {
		throw new JpoException("Type not supported");
	}

	@Override
	public Object getObject(String columnLabel) {
		throw new JpoException("Type not supported");
	}

	@Override
	public short getShort(int columnIndex) {
		throw new JpoException("Type not supported");
	}

	@Override
	public short getShort(String columnLabel) {
		throw new JpoException("Type not supported");
	}

	@Override
	public String getString(int columnIndex) {
		return keys.getString(columnIndex);
	}

	@Override
	public String getString(String columnLabel) {
		return getString(getPosition(columnLabel));
	}

	<T> T orDefault(T value, T defaultValue) {
		if (value!=null) {
			return value;
		}
		return defaultValue;
	}

	private int getPosition(String columnLabel) {
		return generatedColumnNames.indexOf(columnLabel);
	}

	@Override
	public boolean next() {
		return (++position < 1) && !keys.isEmpty();
	}

	@Override
	public Instant getInstant(int columnIndex) {
		int toBeImplemented;
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instant getInstant(String columnLabel) {
		int toBeImplemented;
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalDateTime getLocalDateTime(int columnIndex) {
		int toBeImplemented;
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalDateTime getLocalDateTime(String columnLabel) {
		int toBeImplemented;
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalDate getLocalDate(String columnLabel) {
		int toBeImplemented;
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalDate getLocalDate(int columnIndex) {
		int toBeImplemented;
		// TODO Auto-generated method stub
		return null;
	}
}
