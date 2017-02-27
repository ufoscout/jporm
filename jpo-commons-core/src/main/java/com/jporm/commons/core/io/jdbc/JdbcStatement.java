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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.jporm.types.io.Statement;

public class JdbcStatement implements Statement {

	private final PreparedStatement ps;

	public JdbcStatement(final PreparedStatement ps) {
		this.ps = ps;
	}

	@Override
	public void setBigDecimal(final int parameterIndex, final BigDecimal x) {
		try {
			ps.setBigDecimal(parameterIndex + 1, x);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x) {
		try {
			ps.setBinaryStream(parameterIndex + 1, x);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setBoolean(final int parameterIndex, final boolean x) {
		try {
			ps.setBoolean(parameterIndex + 1, x);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setByte(final int parameterIndex, final byte x) {
		try {
			ps.setByte(parameterIndex + 1, x);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setBytes(final int parameterIndex, final byte[] x) {
		try {
			ps.setBytes(parameterIndex + 1, x);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader) {
		try {
			ps.setCharacterStream(parameterIndex + 1, reader);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setDate(final int parameterIndex, final Date date) {
		try {
			Timestamp ts = null;
			if (date != null) {
				ts = new Timestamp(date.getTime());
			}
			ps.setTimestamp(parameterIndex + 1, ts);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setDouble(final int parameterIndex, final double x) {
		try {
			ps.setDouble(parameterIndex + 1, x);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setFloat(final int parameterIndex, final float x) {
		try {
			ps.setFloat(parameterIndex + 1, x);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setInstant(final int parameterIndex, final Instant instant) {
		try {
			Timestamp ts = null;
			if (instant != null) {
				ts = Timestamp.from(instant);
			}
			ps.setTimestamp(parameterIndex + 1, ts);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setInt(final int parameterIndex, final int x) {
		try {
			ps.setInt(parameterIndex + 1, x);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setLocalDate(final int parameterIndex, final LocalDate date) {
		try {
			java.sql.Date sqlDate = null;
			if (date != null) {
				sqlDate = java.sql.Date.valueOf(date);
			}
			ps.setDate(parameterIndex + 1, sqlDate);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setLocalDateTime(final int parameterIndex, final LocalDateTime date) {
		try {
			Timestamp ts = null;
			if (date != null) {
				ts = Timestamp.valueOf(date);
			}
			ps.setTimestamp(parameterIndex + 1, ts);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setLong(final int parameterIndex, final long x) {
		try {
			ps.setLong(parameterIndex + 1, x);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setObject(final int parameterIndex, final Object x) {
		try {
			ps.setObject(parameterIndex + 1, x);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setShort(final int parameterIndex, final short x) {
		try {
			ps.setShort(parameterIndex + 1, x);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setSqlDate(final int parameterIndex, final java.sql.Date x) {
		try {
			ps.setDate(parameterIndex + 1, x);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setString(final int parameterIndex, final String x) {
		try {
			ps.setString(parameterIndex + 1, x);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x) {
		try {
			ps.setTimestamp(parameterIndex + 1, x);
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
