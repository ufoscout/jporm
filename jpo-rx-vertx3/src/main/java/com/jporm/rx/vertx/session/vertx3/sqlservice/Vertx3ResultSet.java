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
package com.jporm.rx.vertx.session.vertx3.sqlservice;

import io.vertx.ext.sql.ResultSet;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;

import com.jporm.commons.core.exception.JpoException;

public class Vertx3ResultSet implements com.jporm.types.io.ResultSet {

	private final io.vertx.ext.sql.ResultSet resultSet;
	private int position = -1;

	public Vertx3ResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	@Override
	public Array getArray(int columnIndex) {
		throw new JpoException("Type not supported");
	}

	@Override
	public Array getArray(String columnLabel) {
		throw new JpoException("Type not supported");

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
	public Blob getBlob(int columnIndex) {
		throw new JpoException("Type not supported");

	}

	@Override
	public Blob getBlob(String columnLabel) {
		throw new JpoException("Type not supported");

	}

	@Override
	public boolean getBoolean(int columnIndex) {
		return orDefault(resultSet.getResults().get(position).getBoolean(columnIndex), false);
	}

	@Override
	public boolean getBoolean(String columnLabel) {
		return orDefault(resultSet.getRows().get(position).getBoolean(columnLabel), false);
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
		return resultSet.getResults().get(position).getBinary(columnIndex);
	}

	@Override
	public byte[] getBytes(String columnLabel) {
		return resultSet.getRows().get(position).getBinary(columnLabel);
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
	public Clob getClob(int columnIndex) {
		throw new JpoException("Type not supported");

	}

	@Override
	public Clob getClob(String columnLabel) {
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
		return orDefault(resultSet.getResults().get(position).getDouble(columnIndex), 0d);
	}

	@Override
	public double getDouble(String columnLabel) {
		return orDefault(resultSet.getRows().get(position).getDouble(columnLabel), 0d);
	}

	@Override
	public float getFloat(int columnIndex) {
		return orDefault(resultSet.getResults().get(position).getFloat(columnIndex), 0f);
	}

	@Override
	public float getFloat(String columnLabel) {
		return orDefault(resultSet.getRows().get(position).getFloat(columnLabel), 0f);
	}

	@Override
	public int getInt(int columnIndex) {
		return orDefault(resultSet.getResults().get(position).getInteger(columnIndex), 0);
	}

	@Override
	public int getInt(String columnLabel) {
		return orDefault(resultSet.getRows().get(position).getInteger(columnLabel), 0);
	}

	@Override
	public long getLong(int columnIndex) {
		return orDefault(resultSet.getResults().get(position).getLong(columnIndex), 0l);
	}

	@Override
	public long getLong(String columnLabel) {
		return orDefault(resultSet.getRows().get(position).getLong(columnLabel), 0l);
	}

	@Override
	public NClob getNClob(int columnIndex) {
		throw new JpoException("Type not supported");

	}

	@Override
	public NClob getNClob(String columnLabel) {
		throw new JpoException("Type not supported");

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
	public Ref getRef(int columnIndex) {
		throw new JpoException("Type not supported");

	}

	@Override
	public Ref getRef(String columnLabel) {
		throw new JpoException("Type not supported");

	}

	@Override
	public RowId getRowId(int columnIndex) {
		throw new JpoException("Type not supported");
	}

	@Override
	public RowId getRowId(String columnLabel) {
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
	public SQLXML getSQLXML(int columnIndex) {
		throw new JpoException("Type not supported");

	}

	@Override
	public SQLXML getSQLXML(String columnLabel) {
		throw new JpoException("Type not supported");

	}

	@Override
	public String getString(int columnIndex) {
		return resultSet.getResults().get(position).getString(columnIndex);
	}

	@Override
	public String getString(String columnLabel) {
		return resultSet.getRows().get(position).getString(columnLabel);
	}

	@Override
	public Time getTime(int columnIndex) {
		return new Time(getLong(columnIndex));
	}

	@Override
	public Time getTime(String columnLabel) {
		return new Time(getLong(columnLabel));
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) {
		return new Timestamp(getLong(columnIndex));
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) {
		return new Timestamp(getLong(columnLabel));
	}

	@Override
	public URL getURL(int columnIndex) {
		throw new JpoException("Type not supported");
	}

	@Override
	public URL getURL(String columnLabel) {
		throw new JpoException("Type not supported");
	}

	<T> T orDefault(T value, T defaultValue) {
		if (value!=null) {
			return value;
		}
		return defaultValue;
	}

	@Override
	public boolean next() {
		return ++position < resultSet.getNumRows();
	}
}
