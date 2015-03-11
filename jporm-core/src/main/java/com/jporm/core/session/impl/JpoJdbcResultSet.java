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
package com.jporm.core.session.impl;

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
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;

import com.jporm.types.JpoResultSet;

public class JpoJdbcResultSet implements JpoResultSet {

	private final ResultSet rs;

	public JpoJdbcResultSet(ResultSet rs) {
		this.rs = rs;
	}

	@Override
	public Array getArray(int columnIndex) throws SQLException {
		return rs.getArray(columnIndex);
	}

	@Override
	public Array getArray(String columnLabel) throws SQLException {
		return rs.getArray(columnLabel);
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return rs.getBigDecimal(columnIndex);
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return rs.getBigDecimal(columnLabel);
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return rs.getBinaryStream(columnIndex);
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		return rs.getBinaryStream(columnLabel);
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		return rs.getBlob(columnIndex);
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException {
		return rs.getBlob(columnLabel);
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		return rs.getBoolean(columnIndex);
	}

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		return rs.getBoolean(columnLabel);
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {
		return rs.getByte(columnIndex);
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		return rs.getByte(columnLabel);
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		return rs.getBytes(columnIndex);
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException {
		return rs.getBytes(columnLabel);
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return rs.getCharacterStream(columnIndex);
	}

	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		return rs.getCharacterStream(columnLabel);
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		return rs.getClob(columnIndex);
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException {
		return rs.getClob(columnLabel);
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException {
		return rs.getDate(columnIndex);
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException {
		return rs.getDate(columnLabel);
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException {
		return rs.getDouble(columnIndex);
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException {
		return rs.getDouble(columnLabel);
	}

	@Override
	public float getFloat(int columnIndex) throws SQLException {
		return rs.getFloat(columnIndex);
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException {
		return rs.getFloat(columnLabel);
	}

	@Override
	public int getInt(int columnIndex) throws SQLException {
		return rs.getInt(columnIndex);
	}

	@Override
	public int getInt(String columnLabel) throws SQLException {
		return rs.getInt(columnLabel);
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {
		return rs.getLong(columnIndex);
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		return rs.getLong(columnLabel);
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		return rs.getNClob(columnIndex);
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		return rs.getNClob(columnLabel);
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		return rs.getObject(columnIndex);
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		return rs.getObject(columnLabel);
	}

	@Override
	public Ref getRef(int columnIndex) throws SQLException {
		return rs.getRef(columnIndex);
	}

	@Override
	public Ref getRef(String columnLabel) throws SQLException {
		return rs.getRef(columnLabel);
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		return rs.getRowId(columnIndex);
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		return rs.getRowId(columnLabel);
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		return rs.getShort(columnIndex);
	}

	@Override
	public short getShort(String columnLabel) throws SQLException {
		return rs.getShort(columnLabel);
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return rs.getSQLXML(columnIndex);
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return rs.getSQLXML(columnLabel);
	}

	@Override
	public String getString(int columnIndex) throws SQLException {
		return rs.getString(columnIndex);
	}

	@Override
	public String getString(String columnLabel) throws SQLException {
		return rs.getString(columnLabel);
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		return rs.getTime(columnIndex);
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException {
		return rs.getTime(columnLabel);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return rs.getTimestamp(columnIndex);
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		return rs.getTimestamp(columnLabel);
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException {
		return rs.getURL(columnIndex);
	}

	@Override
	public URL getURL(String columnLabel) throws SQLException {
		return rs.getURL(columnLabel);
	}

}
