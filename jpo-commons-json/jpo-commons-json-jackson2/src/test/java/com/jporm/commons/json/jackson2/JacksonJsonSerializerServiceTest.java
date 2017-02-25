package com.jporm.commons.json.jackson2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.Test;

import com.jporm.commons.json.BaseJackson2JsonTestApi;
import com.jporm.commons.json.JsonService;

public class JacksonJsonSerializerServiceTest extends BaseJackson2JsonTestApi {

	private final JsonService jsonSerializerService = new Jackson2JsonService();

	@Test
	public void testJson() {
		final SerializerBean message = new SerializerBean();
		message.setId(new SecureRandom().nextLong());
		message.setName(UUID.randomUUID().toString());
		message.setDate(LocalDate.now());

		final String json = jsonSerializerService.toJson(message);
		assertNotNull(json);
		assertTrue(json.contains( "" + message.getId() ));

		getLogger().info("JSON content: /n[{}]", json);

		final SerializerBean fromJson = jsonSerializerService.fromJson(SerializerBean.class, json);
		assertNotNull(fromJson);
		assertEquals( message.getId(), fromJson.getId() );
		assertEquals( message.getDate(), fromJson.getDate() );
		assertEquals( message.getName(), fromJson.getName() );

	}

	/*
	@Test
	public void testJsonOutputStream() throws UnsupportedEncodingException {
		final SerializerBean message = new SerializerBean();
		message.setId(new SecureRandom().nextLong());
		message.setName(UUID.randomUUID().toString());
		message.setDate(LocalDate.now());

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		jsonSerializerService.toPrettyPrintedJson(message, baos);

		final String json = baos.toString(StandardCharsets.UTF_8.name());

		assertNotNull(json);
		assertTrue(json.contains( "" + message.getId() ));

		getLogger().info("JSON content: /n[{}]", json);

		final SerializerBean fromJson = jsonSerializerService.fromJson(SerializerBean.class, json);
		assertNotNull(fromJson);
		assertEquals( message.getId(), fromJson.getId() );
		assertEquals( message.getDate(), fromJson.getDate() );
		assertEquals( message.getName(), fromJson.getName() );

	}
	 */
}
