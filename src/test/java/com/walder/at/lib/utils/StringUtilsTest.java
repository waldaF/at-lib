package com.walder.at.lib.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class StringUtilsTest {

	@ParameterizedTest
	@MethodSource("stringDataset")
	void isEmpty_allPossibilities_success(final String value, final boolean result) {
		Assertions.assertEquals(result, StringUtils.isEmpty(value));
	}

	@ParameterizedTest
	@MethodSource("stringDataset")
	void isNotEmpty_allPossibilities_success(final String value, final boolean result) {
		Assertions.assertEquals(!result, StringUtils.isNotEmpty(value));
	}

	private static Stream<Arguments> stringDataset() {
		return Stream.of(
				Arguments.of("Tester", false),
				Arguments.of("", true),
				Arguments.of(null, true),
				Arguments.of(" ", false),
				Arguments.of("d dsa", false)
		);
	}
}