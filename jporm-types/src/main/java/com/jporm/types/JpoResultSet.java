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
package com.jporm.types;

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
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;

public interface JpoResultSet {

	Array getArray(int columnIndex) throws SQLException;

	Array getArray(String columnLabel) throws SQLException;

	InputStream getAsciiStream(int columnIndex) throws SQLException;

	InputStream getAsciiStream(String columnLabel) throws SQLException;

	BigDecimal getBigDecimal(int columnIndex) throws SQLException;

	BigDecimal getBigDecimal(String columnLabel) throws SQLException;

	InputStream getBinaryStream(int columnIndex) throws SQLException;

	InputStream getBinaryStream(String columnLabel) throws SQLException;

	Blob getBlob(int columnIndex) throws SQLException;

	Blob getBlob(String columnLabel) throws SQLException;

	boolean getBoolean(int columnIndex) throws SQLException;

	boolean getBoolean(String columnLabel) throws SQLException;

	byte getByte(int columnIndex) throws SQLException;

	byte getByte(String columnLabel) throws SQLException;

	byte[] getBytes(int columnIndex) throws SQLException;

	byte[] getBytes(String columnLabel) throws SQLException;

	Reader getCharacterStream(int columnIndex) throws SQLException;

	Reader getCharacterStream(String columnLabel) throws SQLException;

	Clob getClob(int columnIndex) throws SQLException;

	Clob getClob(String columnLabel) throws SQLException;

	Date getDate(int columnIndex) throws SQLException;

	Date getDate(String columnLabel) throws SQLException;

	double getDouble(int columnIndex) throws SQLException;

	double getDouble(String columnLabel) throws SQLException;

	float getFloat(int columnIndex) throws SQLException;

	float getFloat(String columnLabel) throws SQLException;

	int getInt(int columnIndex) throws SQLException;

	int getInt(String columnLabel) throws SQLException;

	long getLong(int columnIndex) throws SQLException;

	long getLong(String columnLabel) throws SQLException;

	Reader getNCharacterStream(int columnIndex) throws SQLException;

	Reader getNCharacterStream(String columnLabel) throws SQLException;

	NClob getNClob(int columnIndex) throws SQLException;

	NClob getNClob(String columnLabel) throws SQLException;

	String getNString(int columnIndex) throws SQLException;

	String getNString(String columnLabel) throws SQLException;

	Object getObject(int columnIndex) throws SQLException;

	Object getObject(String columnLabel) throws SQLException;

	Ref getRef(int columnIndex) throws SQLException;

	Ref getRef(String columnLabel) throws SQLException;

	RowId getRowId(int columnIndex) throws SQLException;

	RowId getRowId(String columnLabel) throws SQLException;

	short getShort(int columnIndex) throws SQLException;

	short getShort(String columnLabel) throws SQLException;

	SQLXML getSQLXML(int columnIndex) throws SQLException;

	SQLXML getSQLXML(String columnLabel) throws SQLException;

	String getString(int columnIndex) throws SQLException;

	String getString(String columnLabel) throws SQLException;

	Time getTime(int columnIndex) throws SQLException;

	Time getTime(String columnLabel) throws SQLException;

	Timestamp getTimestamp(int columnIndex) throws SQLException;

	Timestamp getTimestamp(String columnLabel) throws SQLException;

	URL getURL(int columnIndex) throws SQLException;

	URL getURL(String columnLabel) throws SQLException;

}
