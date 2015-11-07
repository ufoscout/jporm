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
import java.util.Date;

import com.jporm.commons.core.exception.JpoException;

import io.vertx.ext.sql.ResultSet;

public class Vertx3ResultSet implements com.jporm.types.io.ResultSet {

    private final io.vertx.ext.sql.ResultSet resultSet;
    private int position = -1;

    public Vertx3ResultSet(final ResultSet resultSet) {
        this.resultSet = resultSet;
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
        return orDefault(resultSet.getResults().get(position).getBoolean(columnIndex), false);
    }

    @Override
    public boolean getBoolean(final String columnLabel) {
        return orDefault(resultSet.getRows().get(position).getBoolean(columnLabel), false);
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
        return resultSet.getResults().get(position).getBinary(columnIndex);
    }

    @Override
    public byte[] getBytes(final String columnLabel) {
        return resultSet.getRows().get(position).getBinary(columnLabel);
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
        return orDefault(resultSet.getResults().get(position).getDouble(columnIndex), 0d);
    }

    @Override
    public double getDouble(final String columnLabel) {
        return orDefault(resultSet.getRows().get(position).getDouble(columnLabel), 0d);
    }

    @Override
    public float getFloat(final int columnIndex) {
        return orDefault(resultSet.getResults().get(position).getFloat(columnIndex), 0f);
    }

    @Override
    public float getFloat(final String columnLabel) {
        return orDefault(resultSet.getRows().get(position).getFloat(columnLabel), 0f);
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
        return orDefault(resultSet.getResults().get(position).getInteger(columnIndex), 0);
    }

    @Override
    public int getInt(final String columnLabel) {
        return orDefault(resultSet.getRows().get(position).getInteger(columnLabel), 0);
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
        return orDefault(resultSet.getResults().get(position).getLong(columnIndex), 0l);
    }

    @Override
    public long getLong(final String columnLabel) {
        return orDefault(resultSet.getRows().get(position).getLong(columnLabel), 0l);
    }

    @Override
    public Object getObject(final int columnIndex) {
        throw new JpoException("Type not supported");
    }

    @Override
    public Object getObject(final String columnLabel) {
        throw new JpoException("Type not supported");
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
        return resultSet.getResults().get(position).getString(columnIndex);
    }

    @Override
    public String getString(final String columnLabel) {
        return resultSet.getRows().get(position).getString(columnLabel);
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
        return ++position < resultSet.getNumRows();
    }

    <T> T orDefault(final T value, final T defaultValue) {
        if (value != null) {
            return value;
        }
        return defaultValue;
    }
}
