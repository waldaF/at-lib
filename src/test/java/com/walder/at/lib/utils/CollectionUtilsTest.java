package com.walder.at.lib.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class CollectionUtilsTest {

	@ParameterizedTest
	@MethodSource("isEmptyDataset")
	public <T> void isEmpty_allVariants_success(final Collection<T> data, final boolean expectedEmpty) {
		Assertions.assertEquals(expectedEmpty, CollectionUtils.isEmpty(data));
	}

	@ParameterizedTest
	@MethodSource("isEmptyDataset")
	public <T> void isNotEmpty_allVariants_success(final Collection<T> data, final boolean expectedEmpty) {
		Assertions.assertEquals(!expectedEmpty, CollectionUtils.isNotEmpty(data));
	}

	@ParameterizedTest
	@MethodSource("isEmptyDatasetMap")
	public <K, V> void isEmpty_allVariantsForMap_success(final Map<K, V> map, final boolean expectedEmpty) {
		Assertions.assertEquals(expectedEmpty, CollectionUtils.isEmpty(map));
	}

	@ParameterizedTest
	@MethodSource("isEmptyDatasetMap")
	public <K, V> void isNotEmpty_allVariantsForMap_success(final Map<K, V> map, final boolean expectedEmpty) {
		Assertions.assertEquals(!expectedEmpty, CollectionUtils.isNotEmpty(map));
	}


	private static Stream<Arguments> isEmptyDataset() {
		return Stream.of(
				Arguments.of(null, true),
				Arguments.of(Collections.emptyList(), true),
				Arguments.of(List.of(" "), false),
				Arguments.of(List.of(""), false)
		);
	}

	private static Stream<Arguments> isEmptyDatasetMap() {
		return Stream.of(
				Arguments.of(null, true),
				Arguments.of(Map.of(), true),
				Arguments.of(Map.of("k", "v"), false)
		);
	}
}