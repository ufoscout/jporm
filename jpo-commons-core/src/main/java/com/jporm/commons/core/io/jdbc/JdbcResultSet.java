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

import com.jporm.types.io.ResultEntry;
import com.jporm.types.io.ResultSet;

public class JdbcResultSet implements ResultSet, ResultEntry {

	private final java.sql.ResultSet rs;

	public JdbcResultSet(final java.sql.ResultSet rs) {
		this.rs = rs;
	}

	@Override
	public BigDecimal getBigDecimal(final int columnIndex) {
		try {
			return rs.getBigDecimal(columnIndex + 1);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public BigDecimal getBigDecimal(final String columnLabel) {
		try {
			return rs.getBigDecimal(columnLabel);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public InputStream getBinaryStream(final int columnIndex) {
		try {
			return rs.getBinaryStream(columnIndex + 1);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public InputStream getBinaryStream(final String columnLabel) {
		try {
			return rs.getBinaryStream(columnLabel);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean getBoolean(final int columnIndex) {
		try {
			return rs.getBoolean(columnIndex + 1);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean getBoolean(final String columnLabel) {
		try {
			return rs.getBoolean(columnLabel);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte getByte(final int columnIndex) {
		try {
			return rs.getByte(columnIndex + 1);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte getByte(final String columnLabel) {
		try {
			return rs.getByte(columnLabel);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] getBytes(final int columnIndex) {
		try {
			return rs.getBytes(columnIndex + 1);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] getBytes(final String columnLabel) {
		try {
			return rs.getBytes(columnLabel);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Reader getCharacterStream(final int columnIndex) {
		try {
			return rs.getCharacterStream(columnIndex + 1);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Reader getCharacterStream(final String columnLabel) {
		try {
			return rs.getCharacterStream(columnLabel);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Date getDate(final int columnIndex) {
		try {
			return rs.getTimestamp(columnIndex + 1);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Date getDate(final String columnLabel) {
		try {
			return rs.getTimestamp(columnLabel);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public double getDouble(final int columnIndex) {
		try {
			return rs.getDouble(columnIndex + 1);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public double getDouble(final String columnLabel) {
		try {
			return rs.getDouble(columnLabel);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public float getFloat(final int columnIndex) {
		try {
			return rs.getFloat(columnIndex + 1);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public float getFloat(final String columnLabel) {
		try {
			return rs.getFloat(columnLabel);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Instant getInstant(final int columnIndex) {
		try {
			final Timestamp ts = rs.getTimestamp(columnIndex + 1);
			if (ts == null) {
				return null;
			}
			return ts.toInstant();
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Instant getInstant(final String columnLabel) {
		try {
			final Timestamp ts = rs.getTimestamp(columnLabel);
			if (ts == null) {
				return null;
			}
			return ts.toInstant();
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getInt(final int columnIndex) {
		try {
			return rs.getInt(columnIndex + 1);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getInt(final String columnLabel) {
		try {
			return rs.getInt(columnLabel);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public LocalDate getLocalDate(final int columnIndex) {
		try {
			final java.sql.Date sqlDate = rs.getDate(columnIndex + 1);
			if (sqlDate == null) {
				return null;
			}
			return sqlDate.toLocalDate();
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public LocalDate getLocalDate(final String columnLabel) {
		try {
			final java.sql.Date sqlDate = rs.getDate(columnLabel);
			if (sqlDate == null) {
				return null;
			}
			return sqlDate.toLocalDate();
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public LocalDateTime getLocalDateTime(final int columnIndex) {
		try {
			final Timestamp ts = rs.getTimestamp(columnIndex + 1);
			if (ts == null) {
				return null;
			}
			return ts.toLocalDateTime();
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public LocalDateTime getLocalDateTime(final String columnLabel) {
		try {
			final Timestamp ts = rs.getTimestamp(columnLabel);
			if (ts == null) {
				return null;
			}
			return ts.toLocalDateTime();
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public long getLong(final int columnIndex) {
		try {
			return rs.getLong(columnIndex + 1);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public long getLong(final String columnLabel) {
		try {
			return rs.getLong(columnLabel);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object getObject(final int columnIndex) {
		try {
			return rs.getObject(columnIndex + 1);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object getObject(final String columnLabel) {
		try {
			return rs.getObject(columnLabel);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public short getShort(final int columnIndex) {
		try {
			return rs.getShort(columnIndex + 1);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public short getShort(final String columnLabel) {
		try {
			return rs.getShort(columnLabel);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public java.sql.Date getSqlDate(final int columnIndex) {
		try {
			return rs.getDate(columnIndex + 1);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public java.sql.Date getSqlDate(final String columnLabel) {
		try {
			return rs.getDate(columnLabel);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getString(final int columnIndex) {
		try {
			return rs.getString(columnIndex + 1);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getString(final String columnLabel) {
		try {
			return rs.getString(columnLabel);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Timestamp getTimestamp(final int columnIndex) {
		try {
			return rs.getTimestamp(columnIndex + 1);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Timestamp getTimestamp(final String columnLabel) {
		try {
			return rs.getTimestamp(columnLabel);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean hasNext = false;
	private boolean consumed = true;

	@Override
	public boolean hasNext() {
		try {
			if (consumed) {
				consumed = false;
				hasNext = rs.next();
			}
			return hasNext;
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ResultEntry next() {
		consumed = true;
		return this;
	}

}
