package com.jporm.types.converter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import com.jporm.types.TypeConverter;
import com.jporm.types.jdbc.JdbcIO;
import com.jporm.types.jdbc.JdbcIOFactory;

/**
 *
 * @author Armand Beuvens
 *
 *         Jan 7, 2016
 */
public class OffsetDateTimeToLocalDateTimeTimestampConverter implements TypeConverter<OffsetDateTime, LocalDateTime> {

	private final JdbcIO<LocalDateTime> jdbcIO = JdbcIOFactory.getLocalDateTime();

	@Override
	public OffsetDateTime clone(final OffsetDateTime source) {
		return source;
	}

	@Override
	public OffsetDateTime fromJdbcType(final LocalDateTime value) {
		if (value == null) {
			return null;
		}
		return value.atZone(ZoneId.systemDefault()).toOffsetDateTime();
	}

	@Override
	public JdbcIO<LocalDateTime> getJdbcIO() {
		return jdbcIO;
	}

	@Override
	public Class<OffsetDateTime> propertyType() {
		return OffsetDateTime.class;
	}

	@Override
	public LocalDateTime toJdbcType(final OffsetDateTime value) {
		if (value == null) {
			return null;
		}
		return value.toLocalDateTime();
	}

}
