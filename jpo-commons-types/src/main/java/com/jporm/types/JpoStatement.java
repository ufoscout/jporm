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

public interface JpoStatement {

	void setArray(int parameterIndex, Array x) throws SQLException;

	void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException;

	void setBinaryStream(int parameterIndex, InputStream x) throws SQLException;

	void setBlob(int parameterIndex, Blob x) throws SQLException;

	void setBoolean(int parameterIndex, boolean x) throws SQLException;

	void setByte(int parameterIndex, byte x) throws SQLException;

	void setBytes(int parameterIndex, byte[] x) throws SQLException;

	void setCharacterStream(int parameterIndex, Reader reader) throws SQLException;

	void setClob(int parameterIndex, Clob x) throws SQLException;

	void setDate(int parameterIndex, Date x) throws SQLException;

	void setDouble(int parameterIndex, double x) throws SQLException;

	void setFloat(int parameterIndex, float x) throws SQLException;

	void setInt(int parameterIndex, int x) throws SQLException;

	void setLong(int parameterIndex, long x) throws SQLException;

	void setNClob(int parameterIndex, NClob value) throws SQLException;

	void setObject(int parameterIndex, Object x) throws SQLException;

	void setRef(int parameterIndex, Ref x) throws SQLException;

	void setRowId(int parameterIndex, RowId x) throws SQLException;

	void setShort(int parameterIndex, short x) throws SQLException;

	void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException;

	void setString(int parameterIndex, String x) throws SQLException;

	void setTime(int parameterIndex, Time x) throws SQLException;

	void setTimestamp(int parameterIndex, Timestamp x) throws SQLException;

	void setURL(int parameterIndex, URL x) throws SQLException;

}
