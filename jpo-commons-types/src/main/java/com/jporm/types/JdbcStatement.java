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
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;

public class JdbcStatement implements Statement {

	private final PreparedStatement ps;

	public JdbcStatement(PreparedStatement ps) {
		this.ps = ps;
	}

	@Override
	public void setArray(int parameterIndex, Array x) {
		try {
			ps.setArray(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setBigDecimal(int parameterIndex, BigDecimal x) {
		try {
			ps.setBigDecimal(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x) {
		try {
			ps.setBinaryStream(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setBlob(int parameterIndex, Blob x) {
		try {
			ps.setBlob(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setBoolean(int parameterIndex, boolean x) {
		try {
			ps.setBoolean(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setByte(int parameterIndex, byte x) {
		try {
			ps.setByte(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setBytes(int parameterIndex, byte[] x) {
		try {
			ps.setBytes(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader) {
		try {
			ps.setCharacterStream(parameterIndex, reader);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setClob(int parameterIndex, Clob x) {
		try {
			ps.setClob(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setDate(int parameterIndex, Date x) {
		try {
			ps.setDate(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setDouble(int parameterIndex, double x) {
		try {
			ps.setDouble(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setFloat(int parameterIndex, float x) {
		try {
			ps.setFloat(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setInt(int parameterIndex, int x) {
		try {
			ps.setInt(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setLong(int parameterIndex, long x) {
		try {
			ps.setLong(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setNClob(int parameterIndex, NClob value) {
		try {
			ps.setNClob(parameterIndex, value);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setObject(int parameterIndex, Object x) {
		try {
			ps.setObject(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setRef(int parameterIndex, Ref x) {
		try {
			ps.setRef(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setRowId(int parameterIndex, RowId x) {
		try {
			ps.setRowId(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setShort(int parameterIndex, short x) {
		try {
			ps.setShort(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject) {
		try {
			ps.setSQLXML(parameterIndex, xmlObject);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setString(int parameterIndex, String x) {
		try {
			ps.setString(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setTime(int parameterIndex, Time x) {
		try {
			ps.setTime(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x) {
		try {
			ps.setTimestamp(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setURL(int parameterIndex, URL x) {
		try {
			ps.setURL(parameterIndex, x);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
