package com.walder.at.lib.validation;

import com.walder.at.lib.utils.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public abstract class ErrorMessage {

	protected final String info;
	protected List<String> errors;

	protected <T> void notNullAddErrorMessage(final T actual, final String fieldName) {
		if (validateIsNull(actual)) {
			errors.add(generateNotNullMessage(fieldName, actual));
		}
	}

	protected <T> void isNullAddErrorMessage(final T actual, final String fieldName) {
		if (validateNotNull(actual)) {
			errors.add(generateNullMessage(fieldName, actual));
		}
	}

	protected <T> void notEqualsAddErrorMessage(final T actual, final T expected, final String fieldName) {
		if (!validateEquals(actual, expected)) {
			errors.add(generateEqualsMessage(fieldName, actual, expected));
		}
	}

	protected <T, R> void notSizeAddErrorMessage(final Collection<T> actual, final Collection<R> expected) {
		if (!validateEquals(actual.size(), expected.size())) {
			errors.add(generateSizeMessage(actual.size(), expected.size()));
		}
	}

	protected <T, R> void validateCustomCollection(final Collection<T> actualData,
												   final Collection<T> expectedData,
												   final Function<T, R> key,
												   final String fieldName) {

		final Map<R, T> actualTransformed = actualData.stream()
				.collect(Collectors.toMap(key, Function.identity()));

		final Map<R, T> expectedTransformed = expectedData.stream()
				.collect(Collectors.toMap(key, Function.identity()));

		writeMap(actualTransformed, expectedTransformed, fieldName);
	}

	private boolean validateIsNull(final Object object) {
		return Objects.isNull(object);
	}

	private boolean validateNotNull(final Object object) {
		return !validateIsNull(object);
	}

	private boolean validateEquals(final Object actual, final Object expected) {
		final String expectedString = transformToString(expected);
		final String actualString = transformToString(actual);
		return validateEquals(actualString, expectedString);
	}

	private <K, V> void writeMap(final Map<K, V> actualMap, final Map<K, V> expectedMap, final String fieldName) {
		expectedMap.keySet().forEach(
				expectedKey -> {
					final V actual = actualMap.get(expectedKey);
					final V expected = expectedMap.get(expectedKey);
					notEqualsAddErrorMessage(actual, expected, fieldName);
				}
		);
	}

	private String transformToString(final Object object) {
		return (object == null || StringUtils.isEmpty(object.toString())) ?
				null :
				object.toString();
	}

	private boolean validateEquals(final String actual, final String expected) {
		if (expected == null || StringUtils.isEmpty(expected)) {
			return StringUtils.isEmpty(actual);
		}
		return expected.equals(actual);
	}

	private <T> String generateSizeMessage(final T actual, final T expected) {
		return String.format("collection size does not correspond expected [ %s ] but found [ %s ]", expected, actual);
	}

	private <T> String generateEqualsMessage(final String fieldName, final T actual, final T expected) {
		return String.format("field %s are not equals expected [ %s ] but found [ %s ]", fieldName, expected, actual);
	}

	private <T> String generateNotNullMessage(final String fieldName, final T actual) {
		return String.format("field %s is not null expected null but found %s", fieldName, actual);
	}

	private <T> String generateNullMessage(final String fieldName, final T actual) {
		return String.format("field %s is null expected not null but found %s", fieldName, actual);
	}
}