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
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public interface Statement {

    void setBigDecimal(int parameterIndex, BigDecimal x);

    void setBinaryStream(int parameterIndex, InputStream x);

    void setBoolean(int parameterIndex, boolean x);

    void setByte(int parameterIndex, byte x);

    void setBytes(int parameterIndex, byte[] x);

    void setCharacterStream(int parameterIndex, Reader reader);

    void setDate(int parameterIndex, Date x);

    void setDouble(int parameterIndex, double x);

    void setFloat(int parameterIndex, float x);

    void setInstant(int parameterIndex, Instant instant);

    void setInt(int parameterIndex, int x);

    void setLocalDate(int parameterIndex, LocalDate date);

    void setLocalDateTime(int parameterIndex, LocalDateTime date);

    void setLong(int parameterIndex, long x);

    void setObject(int parameterIndex, Object x);

    void setShort(int parameterIndex, short x);

    void setSqlDate(int parameterIndex, java.sql.Date x);

    void setString(int parameterIndex, String x);

    void setTimestamp(int parameterIndex, Timestamp x);

}
