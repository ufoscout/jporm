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
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;

public interface ResultSet {

	boolean next();

	Array getArray(int columnIndex);

	Array getArray(String columnLabel);

	BigDecimal getBigDecimal(int columnIndex);

	BigDecimal getBigDecimal(String columnLabel);

	InputStream getBinaryStream(int columnIndex);

	InputStream getBinaryStream(String columnLabel);

	Blob getBlob(int columnIndex);

	Blob getBlob(String columnLabel);

	boolean getBoolean(int columnIndex);

	boolean getBoolean(String columnLabel);

	byte getByte(int columnIndex);

	byte getByte(String columnLabel);

	byte[] getBytes(int columnIndex);

	byte[] getBytes(String columnLabel);

	Reader getCharacterStream(int columnIndex);

	Reader getCharacterStream(String columnLabel);

	Clob getClob(int columnIndex);

	Clob getClob(String columnLabel);

	Date getDate(int columnIndex);

	Date getDate(String columnLabel);

	double getDouble(int columnIndex);

	double getDouble(String columnLabel);

	float getFloat(int columnIndex);

	float getFloat(String columnLabel);

	int getInt(int columnIndex);

	int getInt(String columnLabel);

	long getLong(int columnIndex);

	long getLong(String columnLabel);

	NClob getNClob(int columnIndex);

	NClob getNClob(String columnLabel);

	Object getObject(int columnIndex);

	Object getObject(String columnLabel);

	Ref getRef(int columnIndex);

	Ref getRef(String columnLabel);

	RowId getRowId(int columnIndex);

	RowId getRowId(String columnLabel);

	short getShort(int columnIndex);

	short getShort(String columnLabel);

	SQLXML getSQLXML(int columnIndex);

	SQLXML getSQLXML(String columnLabel);

	String getString(int columnIndex);

	String getString(String columnLabel);

	Time getTime(int columnIndex);

	Time getTime(String columnLabel);

	Timestamp getTimestamp(int columnIndex);

	Timestamp getTimestamp(String columnLabel);

	URL getURL(int columnIndex);

	URL getURL(String columnLabel);

}
