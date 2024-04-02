package com.walder.at.lib.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SerializationUtilsTest {
	private final TestingDto testingDto = new TestingDto("ola", "hola", null);
	private final String testingDtoAsJson = "{\"someFieldNaming\":\"" + testingDto.someFieldNaming() + "\",\"simple\":\"" + testingDto.simple() + "\"}";
	private final String testingDtoAsUpperCamelCaseJson = "{\"SomeFieldNaming\":\"" + testingDto.someFieldNaming() + "\",\"Simple\":\"" + testingDto.simple() + "\"}";
	private final String testingDtoAsLowerCamelCaseUnderscoresJson = "{\"some_field_naming\":\"" + testingDto.someFieldNaming() + "\",\"simple\":\"" + testingDto.simple() + "\"}";

	@Test
	void serialize_default_happyPath_success() {
		Assertions.assertEquals(testingDtoAsJson, SerializationUtils.serialize(testingDto));
	}

	@Test
	void serializeUpperCamelCase_happyPath_success() {
		Assertions.assertEquals(testingDtoAsUpperCamelCaseJson, SerializationUtils.serializeUpperCamelCase(testingDto));
	}

	@Test
	void serializeLowerCamelCaseUnderscores_happyPath_success() {
		Assertions.assertEquals(testingDtoAsLowerCamelCaseUnderscoresJson, SerializationUtils.serializeLowerLowerCamelCaseUnderscores(testingDto));
	}

	@Test
	void deserialize_default_happyPath_success() {
		Assertions.assertEquals(testingDto, SerializationUtils.deserialize(testingDtoAsJson, TestingDto.class));
	}

	@Test
	void deserializeUpperCamelCase_happyPath_success() {
		Assertions.assertEquals(testingDto, SerializationUtils.deserializeUpperCamelCase(testingDtoAsUpperCamelCaseJson, TestingDto.class));
	}

	@Test
	void deserializeLowerCamelCaseUnderscores_happyPath_success() {
		Assertions.assertEquals(testingDto, SerializationUtils.deserializeLowerCamelCaseUnderscores(testingDtoAsLowerCamelCaseUnderscoresJson, TestingDto.class));
	}

	private record TestingDto(String someFieldNaming, String simple, Integer count) {
	}
}