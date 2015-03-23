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

public interface Statement {

	void setArray(int parameterIndex, Array x);

	void setBigDecimal(int parameterIndex, BigDecimal x);

	void setBinaryStream(int parameterIndex, InputStream x);

	void setBlob(int parameterIndex, Blob x);

	void setBoolean(int parameterIndex, boolean x);

	void setByte(int parameterIndex, byte x);

	void setBytes(int parameterIndex, byte[] x);

	void setCharacterStream(int parameterIndex, Reader reader);

	void setClob(int parameterIndex, Clob x);

	void setDate(int parameterIndex, Date x);

	void setDouble(int parameterIndex, double x);

	void setFloat(int parameterIndex, float x);

	void setInt(int parameterIndex, int x);

	void setLong(int parameterIndex, long x);

	void setNClob(int parameterIndex, NClob value);

	void setObject(int parameterIndex, Object x);

	void setRef(int parameterIndex, Ref x);

	void setRowId(int parameterIndex, RowId x);

	void setShort(int parameterIndex, short x);

	void setSQLXML(int parameterIndex, SQLXML xmlObject);

	void setString(int parameterIndex, String x);

	void setTime(int parameterIndex, Time x);

	void setTimestamp(int parameterIndex, Timestamp x);

	void setURL(int parameterIndex, URL x);

}
