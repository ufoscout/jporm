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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jporm.types.io.Statement;

import io.vertx.core.json.JsonArray;

public class Vertx3Statement implements Statement {

    private List<Object> params = new ArrayList<>();

    public JsonArray getParams() {
        return new JsonArray(params);
    }

    @Override
    public void setBigDecimal(final int parameterIndex, final BigDecimal x) {
        params.add(x);

    }

    @Override
    public void setBinaryStream(final int parameterIndex, final InputStream x) {
        params.add(x);

    }

    @Override
    public void setBoolean(final int parameterIndex, final boolean x) {
        params.add(x);

    }

    @Override
    public void setByte(final int parameterIndex, final byte x) {
        params.add(x);

    }

    @Override
    public void setBytes(final int parameterIndex, final byte[] x) {
        params.add(x);

    }

    @Override
    public void setCharacterStream(final int parameterIndex, final Reader x) {
        params.add(x);
    }

    @Override
    public void setDate(final int parameterIndex, final Date x) {
        params.add(x);

    }

    @Override
    public void setDouble(final int parameterIndex, final double x) {
        params.add(x);

    }

    @Override
    public void setFloat(final int parameterIndex, final float x) {
        params.add(x);

    }

    @Override
    public void setInstant(final int parameterIndex, final Instant instant) {
        int toBeImplemented;
        // TODO Auto-generated method stub

    }

    @Override
    public void setInt(final int parameterIndex, final int x) {
        params.add(x);

    }

    @Override
    public void setLocalDate(final int parameterIndex, final LocalDate date) {
        int toBeImplemented;
        // TODO Auto-generated method stub

    }

    @Override
    public void setLocalDateTime(final int parameterIndex, final LocalDateTime date) {
        int toBeImplemented;
        // TODO Auto-generated method stub

    }

    @Override
    public void setLong(final int parameterIndex, final long x) {
        params.add(x);

    }

    @Override
    public void setObject(final int parameterIndex, final Object x) {
        params.add(x);

    }

    @Override
    public void setShort(final int parameterIndex, final short x) {
        params.add(x);

    }

    @Override
    public void setSqlDate(final int parameterIndex, final java.sql.Date x) {
        int toBeImplemented;
        // TODO Auto-generated method stub

    }

    @Override
    public void setString(final int parameterIndex, final String x) {
        params.add(x);

    }

    @Override
    public void setTimestamp(final int parameterIndex, final Timestamp x) {
        int toBeImplemented;
        // TODO Auto-generated method stub

    }

}
