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

public class JdbcResultSet implements ResultSet {

	private final java.sql.ResultSet rs;

	public JdbcResultSet(java.sql.ResultSet rs) {
		this.rs = rs;
	}

	@Override
	public Array getArray(int columnIndex) {
		try {
			return rs.getArray(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Array getArray(String columnLabel) {
		try {
			return rs.getArray(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) {
		try {
			return rs.getBigDecimal(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) {
		try {
			return rs.getBigDecimal(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) {
		try {
			return rs.getBinaryStream(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) {
		try {
			return rs.getBinaryStream(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Blob getBlob(int columnIndex) {
		try {
			return rs.getBlob(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Blob getBlob(String columnLabel) {
		try {
			return rs.getBlob(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean getBoolean(int columnIndex) {
		try {
			return rs.getBoolean(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean getBoolean(String columnLabel) {
		try {
			return rs.getBoolean(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte getByte(int columnIndex) {
		try {
			return rs.getByte(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte getByte(String columnLabel) {
		try {
			return rs.getByte(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] getBytes(int columnIndex) {
		try {
			return rs.getBytes(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] getBytes(String columnLabel) {
		try {
			return rs.getBytes(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Reader getCharacterStream(int columnIndex) {
		try {
			return rs.getCharacterStream(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Reader getCharacterStream(String columnLabel) {
		try {
			return rs.getCharacterStream(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Clob getClob(int columnIndex) {
		try {
			return rs.getClob(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Clob getClob(String columnLabel) {
		try {
			return rs.getClob(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Date getDate(int columnIndex) {
		try {
			return rs.getDate(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Date getDate(String columnLabel) {
		try {
			return rs.getDate(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public double getDouble(int columnIndex) {
		try {
			return rs.getDouble(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public double getDouble(String columnLabel) {
		try {
			return rs.getDouble(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public float getFloat(int columnIndex) {
		try {
			return rs.getFloat(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public float getFloat(String columnLabel) {
		try {
			return rs.getFloat(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getInt(int columnIndex) {
		try {
			return rs.getInt(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getInt(String columnLabel) {
		try {
			return rs.getInt(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public long getLong(int columnIndex) {
		try {
			return rs.getLong(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public long getLong(String columnLabel) {
		try {
			return rs.getLong(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public NClob getNClob(int columnIndex) {
		try {
			return rs.getNClob(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public NClob getNClob(String columnLabel) {
		try {
			return rs.getNClob(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object getObject(int columnIndex) {
		try {
			return rs.getObject(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object getObject(String columnLabel) {
		try {
			return rs.getObject(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Ref getRef(int columnIndex) {
		try {
			return rs.getRef(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Ref getRef(String columnLabel) {
		try {
			return rs.getRef(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public RowId getRowId(int columnIndex) {
		try {
			return rs.getRowId(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public RowId getRowId(String columnLabel) {
		try {
			return rs.getRowId(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public short getShort(int columnIndex) {
		try {
			return rs.getShort(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public short getShort(String columnLabel) {
		try {
			return rs.getShort(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) {
		try {
			return rs.getSQLXML(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) {
		try {
			return rs.getSQLXML(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getString(int columnIndex) {
		try {
			return rs.getString(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getString(String columnLabel) {
		try {
			return rs.getString(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Time getTime(int columnIndex) {
		try {
			return rs.getTime(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Time getTime(String columnLabel) {
		try {
			return rs.getTime(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) {
		try {
			return rs.getTimestamp(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) {
		try {
			return rs.getTimestamp(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public URL getURL(int columnIndex) {
		try {
			return rs.getURL(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public URL getURL(String columnLabel) {
		try {
			return rs.getURL(columnLabel);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean next() {
		try {
			return rs.next();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
