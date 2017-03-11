package com.jporm.types.ext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.junit.Test;

import com.jporm.types.converter.OffsetDateTimeToLocalDateTimeTimestampConverter;

/**
*
* @author Armand Beuvens
*
*         Jan 7, 2016
*/

public class OffsetDateTimeToLocalDateTimeTimestampConverterTest {
	
	private OffsetDateTimeToLocalDateTimeTimestampConverter converter = new OffsetDateTimeToLocalDateTimeTimestampConverter();
	
	@Test
	public void cloneTest() {
		OffsetDateTime now = OffsetDateTime.now();
		OffsetDateTime clonedDate = converter.clone(now);
		assertTrue(now == clonedDate);		
	}
	
	@Test
	public void fromJdbcTypeTest() {
		assertNull(converter.fromJdbcType(null));
		LocalDateTime now = LocalDateTime.now();
		OffsetDateTime date = converter.fromJdbcType(now);
		assertEquals(now.atZone(ZoneId.systemDefault()).toOffsetDateTime(), date);
	}
	
	@Test
	public void toJdbcTypeTest() {
		assertNull(converter.toJdbcType(null));
		OffsetDateTime now = OffsetDateTime.now();
		LocalDateTime date = converter.toJdbcType(now);
		assertEquals(now.toLocalDateTime(), date);
	}

}
