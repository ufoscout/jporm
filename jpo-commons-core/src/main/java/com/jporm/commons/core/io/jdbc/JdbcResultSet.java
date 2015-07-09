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
package com.jporm.commons.core.io.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.jporm.types.io.ResultSet;

public class JdbcResultSet implements ResultSet {

	private final java.sql.ResultSet rs;

	public JdbcResultSet(java.sql.ResultSet rs) {
		this.rs = rs;
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
	public Date getDate(int columnIndex) {
		try {
			return rs.getTimestamp(columnIndex);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Date getDate(String columnLabel) {
		try {
			return rs.getTimestamp(columnLabel);
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
	public Instant getInstant(int columnIndex) {
		try {
			Timestamp ts = rs.getTimestamp(columnIndex);
			if (ts == null) {
				return null;
			}
			return ts.toInstant();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Instant getInstant(String columnLabel) {
		try {
			Timestamp ts = rs.getTimestamp(columnLabel);
			if (ts == null) {
				return null;
			}
			return ts.toInstant();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public LocalDateTime getLocalDateTime(int columnIndex) {
		try {
			Timestamp ts = rs.getTimestamp(columnIndex);
			if (ts == null) {
				return null;
			}
			return ts.toLocalDateTime();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public LocalDateTime getLocalDateTime(String columnLabel) {
		try {
			Timestamp ts = rs.getTimestamp(columnLabel);
			if (ts == null) {
				return null;
			}
			return ts.toLocalDateTime();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public LocalDate getLocalDate(int columnIndex) {
		try {
			Timestamp ts = rs.getTimestamp(columnIndex);
			if (ts == null) {
				return null;
			}
			return ts.toLocalDateTime().toLocalDate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public LocalDate getLocalDate(String columnLabel) {
		try {
			Timestamp ts = rs.getTimestamp(columnLabel);
			if (ts == null) {
				return null;
			}
			return ts.toLocalDateTime().toLocalDate();
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
