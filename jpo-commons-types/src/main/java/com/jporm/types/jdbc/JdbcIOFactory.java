/*******************************************************************************
 * Copyright 2017 Francesco Cina'
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
package com.jporm.types.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class JdbcIOFactory {

	private final static JdbcIO<BigDecimal> bigDecimalIO = new BigDecimalJdbcIO();
	private final static JdbcIO<Boolean> booleanIO = new BooleanPrimitiveJdbcIO();
	private final static JdbcIO<Byte> byteIO = new BytePrimitiveJdbcIO();
	private final static JdbcIO<byte[]> bytesIO = new BytesJdbcIO();
	private final static JdbcIO<Date> dateIO = new DateJdbcIO();
	private final static JdbcIO<Double> doubleIO = new DoublePrimitiveJdbcIO();
	private final static JdbcIO<Float> floatIO = new FloatPrimitiveJdbcIO();
	private final static JdbcIO<InputStream> inputStreamIO = new InputStreamJdbcIO();
	private final static JdbcIO<Instant> instantIO = new InstantJdbcIO();
	private final static JdbcIO<Integer> integerIO = new IntegerPrimitiveJdbcIO();
	private final static JdbcIO<LocalDate> localDateIO = new LocalDateJdbcIO();
	private final static JdbcIO<LocalDateTime> localDateTimeIO = new LocalDateTimeJdbcIO();
	private final static JdbcIO<Long> longIO = new LongPrimitiveJdbcIO();
	private final static JdbcIO<Object> objectIO = new ObjectJdbcIO();
	private final static JdbcIO<Reader> readerIO = new ReaderJdbcIO();
	private final static JdbcIO<Short> shortIO = new ShortPrimitiveJdbcIO();
	private final static JdbcIO<java.sql.Date> sqlDateIO = new SqlDateJdbcIO();
	private final static JdbcIO<String> stringIO = new StringJdbcIO();
	private final static JdbcIO<Timestamp> timestampIO = new TimestampJdbcIO();

	public static JdbcIO<BigDecimal> getBigDecimal() {
		return bigDecimalIO;
	}
	public static JdbcIO<Boolean> getBoolean() {
		return booleanIO;
	}
	public static JdbcIO<Byte> getByte() {
		return byteIO;
	}
	public static JdbcIO<byte[]> getBytes() {
		return bytesIO;
	}
	public static JdbcIO<Date> getDate() {
		return dateIO;
	}
	public static JdbcIO<Double> getDouble() {
		return doubleIO;
	}
	public static JdbcIO<Float> getFloat() {
		return floatIO;
	}
	public static JdbcIO<InputStream> getInputStream() {
		return inputStreamIO;
	}
	public static JdbcIO<Instant> getInstant() {
		return instantIO;
	}
	public static JdbcIO<Integer> getInteger() {
		return integerIO;
	}
	public static JdbcIO<LocalDate> getLocalDate() {
		return localDateIO;
	}
	public static JdbcIO<LocalDateTime> getLocalDateTime() {
		return localDateTimeIO;
	}
	public static JdbcIO<Long> getLong() {
		return longIO;
	}
	public static JdbcIO<Object> getObject() {
		return objectIO;
	}
	public static JdbcIO<Reader> getReader() {
		return readerIO;
	}
	public static JdbcIO<Short> getShort() {
		return shortIO;
	}
	public static JdbcIO<java.sql.Date> getSqlDate() {
		return sqlDateIO;
	}
	public static JdbcIO<String> getString() {
		return stringIO;
	}
	public static JdbcIO<Timestamp> getTimestamp() {
		return timestampIO;
	}

}
