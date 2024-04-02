package com.walder.at.lib.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class Base64UtilsTest {
	@ParameterizedTest
	@MethodSource("encodeDataset")
	void encodeAsAuthHeader_allCombinations_success(final Username username,
													final Password password,
													final String expectedEncodedValue) {
		Assertions.assertEquals(expectedEncodedValue, Base64Utils.encodeAsAuthHeader(username.value, password.value));
	}

	private static Stream<Arguments> encodeDataset() {
		return Stream.of(
				Arguments.of(new Username("username"), new Password("password"), "dXNlcm5hbWU6cGFzc3dvcmQ="),
				Arguments.of(new Username(null), new Password(null), null),
				Arguments.of(new Username("username"), new Password(null), "dXNlcm5hbWU6bnVsbA=="),
				Arguments.of(new Username(null), new Password("password"), "bnVsbDpwYXNzd29yZA==")
		);
	}

	record Username(String value) {
	}

	record Password(String value) {
	}

}