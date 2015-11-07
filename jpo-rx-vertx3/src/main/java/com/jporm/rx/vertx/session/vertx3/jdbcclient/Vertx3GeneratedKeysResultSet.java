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
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.jporm.commons.core.exception.JpoException;

import io.vertx.core.json.JsonArray;

public class Vertx3GeneratedKeysResultSet implements com.jporm.types.io.ResultSet {

    private final JsonArray keys;
    private int position = -1;
    private final List<String> generatedColumnNames;

    public Vertx3GeneratedKeysResultSet(final JsonArray keys, final String[] generatedColumnNames) {
        this.keys = keys;
        this.generatedColumnNames = Arrays.asList(generatedColumnNames);
    }

    @Override
    public BigDecimal getBigDecimal(final int columnIndex) {
        return BigDecimal.valueOf(getLong(columnIndex));
    }

    @Override
    public BigDecimal getBigDecimal(final String columnLabel) {
        return BigDecimal.valueOf(getLong(columnLabel));
    }

    @Override
    public InputStream getBinaryStream(final int columnIndex) {
        throw new JpoException("Type not supported");

    }

    @Override
    public InputStream getBinaryStream(final String columnLabel) {
        throw new JpoException("Type not supported");

    }

    @Override
    public boolean getBoolean(final int columnIndex) {
        return orDefault(keys.getBoolean(columnIndex), false);
    }

    @Override
    public boolean getBoolean(final String columnLabel) {
        return getBoolean(getPosition(columnLabel));
    }

    @Override
    public byte getByte(final int columnIndex) {
        throw new JpoException("Type not supported");
    }

    @Override
    public byte getByte(final String columnLabel) {
        throw new JpoException("Type not supported");
    }

    @Override
    public byte[] getBytes(final int columnIndex) {
        return keys.getBinary(columnIndex);
    }

    @Override
    public byte[] getBytes(final String columnLabel) {
        return getBytes(getPosition(columnLabel));
    }

    @Override
    public Reader getCharacterStream(final int columnIndex) {
        throw new JpoException("Type not supported");

    }

    @Override
    public Reader getCharacterStream(final String columnLabel) {
        throw new JpoException("Type not supported");

    }

    @Override
    public Date getDate(final int columnIndex) {
        return new Date(getLong(columnIndex));
    }

    @Override
    public Date getDate(final String columnLabel) {
        return new Date(getLong(columnLabel));
    }

    @Override
    public double getDouble(final int columnIndex) {
        return orDefault(keys.getDouble(columnIndex), 0d);
    }

    @Override
    public double getDouble(final String columnLabel) {
        return getDouble(getPosition(columnLabel));
    }

    @Override
    public float getFloat(final int columnIndex) {
        return orDefault(keys.getFloat(columnIndex), 0f);
    }

    @Override
    public float getFloat(final String columnLabel) {
        return getFloat(getPosition(columnLabel));
    }

    @Override
    public Instant getInstant(final int columnIndex) {
        int toBeImplemented;
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Instant getInstant(final String columnLabel) {
        int toBeImplemented;
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getInt(final int columnIndex) {
        return orDefault(keys.getInteger(columnIndex), 0);
    }

    @Override
    public int getInt(final String columnLabel) {
        return getInt(getPosition(columnLabel));
    }

    @Override
    public LocalDate getLocalDate(final int columnIndex) {
        int toBeImplemented;
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LocalDate getLocalDate(final String columnLabel) {
        int toBeImplemented;
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LocalDateTime getLocalDateTime(final int columnIndex) {
        int toBeImplemented;
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LocalDateTime getLocalDateTime(final String columnLabel) {
        int toBeImplemented;
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getLong(final int columnIndex) {
        return orDefault(keys.getLong(columnIndex), 0l);
    }

    @Override
    public long getLong(final String columnLabel) {
        return getLong(getPosition(columnLabel));
    }

    @Override
    public Object getObject(final int columnIndex) {
        throw new JpoException("Type not supported");
    }

    @Override
    public Object getObject(final String columnLabel) {
        throw new JpoException("Type not supported");
    }

    private int getPosition(final String columnLabel) {
        return generatedColumnNames.indexOf(columnLabel);
    }

    @Override
    public short getShort(final int columnIndex) {
        throw new JpoException("Type not supported");
    }

    @Override
    public short getShort(final String columnLabel) {
        throw new JpoException("Type not supported");
    }

    @Override
    public java.sql.Date getSqlDate(final int columnIndex) {
        int toBeImplemented;
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public java.sql.Date getSqlDate(final String columnLabel) {
        int toBeImplemented;
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getString(final int columnIndex) {
        return keys.getString(columnIndex);
    }

    @Override
    public String getString(final String columnLabel) {
        return getString(getPosition(columnLabel));
    }

    @Override
    public Timestamp getTimestamp(final int columnIndex) {
        int toBeImplemented;
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Timestamp getTimestamp(final String columnLabel) {
        int toBeImplemented;
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean next() {
        return (++position < 1) && !keys.isEmpty();
    }

    <T> T orDefault(final T value, final T defaultValue) {
        if (value != null) {
            return value;
        }
        return defaultValue;
    }
}
