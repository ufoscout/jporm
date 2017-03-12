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

	Statement setBigDecimal(int parameterIndex, BigDecimal x);

	Statement setBinaryStream(int parameterIndex, InputStream x);

	Statement setBoolean(int parameterIndex, boolean x);

	Statement setByte(int parameterIndex, byte x);

	Statement setBytes(int parameterIndex, byte[] x);

	Statement setCharacterStream(int parameterIndex, Reader reader);

	Statement setDate(int parameterIndex, Date x);

	Statement setDouble(int parameterIndex, double x);

	Statement setFloat(int parameterIndex, float x);

	Statement setInstant(int parameterIndex, Instant instant);

	Statement setInt(int parameterIndex, int x);

	Statement setLocalDate(int parameterIndex, LocalDate date);

	Statement setLocalDateTime(int parameterIndex, LocalDateTime date);

	Statement setLong(int parameterIndex, long x);

	Statement setObject(int parameterIndex, Object x);

	Statement setShort(int parameterIndex, short x);

	Statement setSqlDate(int parameterIndex, java.sql.Date x);

	Statement setString(int parameterIndex, String x);

	Statement setTimestamp(int parameterIndex, Timestamp x);

}
