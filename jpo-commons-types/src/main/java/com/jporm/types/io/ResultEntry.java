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
package com.jporm.types.io;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


public interface ResultEntry {

	BigDecimal getBigDecimal(int columnIndex);

	BigDecimal getBigDecimal(String columnLabel);

	InputStream getBinaryStream(int columnIndex);

	InputStream getBinaryStream(String columnLabel);

	boolean getBoolean(int columnIndex);

	boolean getBoolean(String columnLabel);

	byte getByte(int columnIndex);

	byte getByte(String columnLabel);

	byte[] getBytes(int columnIndex);

	byte[] getBytes(String columnLabel);

	Reader getCharacterStream(int columnIndex);

	Reader getCharacterStream(String columnLabel);

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

	Object getObject(int columnIndex);

	Object getObject(String columnLabel);

	short getShort(int columnIndex);

	short getShort(String columnLabel);

	String getString(int columnIndex);

	String getString(String columnLabel);

	Instant getInstant(int columnIndex);

	Instant getInstant(String columnLabel);

	LocalDateTime getLocalDateTime(int columnIndex);

	LocalDateTime getLocalDateTime(String columnLabel);

	LocalDate getLocalDate(String columnLabel);

	LocalDate getLocalDate(int columnIndex);

}
