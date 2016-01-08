package com.jporm.types.ext;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import com.jporm.types.TypeConverter;

/**
*
* @author Armand Beuvens
*
*         Jan 7, 2016
*/
public class OffsetDateTimeToLocalDateTimeTimestampConverter implements TypeConverter<OffsetDateTime, LocalDateTime> {
	
	@Override
    public OffsetDateTime clone(final OffsetDateTime source) {
		return OffsetDateTime.of(source.toLocalDateTime(), source.getOffset());
    }

    @Override
    public OffsetDateTime fromJdbcType(final LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return value.atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }

    @Override
    public Class<LocalDateTime> jdbcType() {
        return LocalDateTime.class;
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
