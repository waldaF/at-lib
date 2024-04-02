package com.walder.at.lib.validation;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.walder.at.lib.common.constant.Constant;
import com.walder.at.lib.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

public class Validator extends ErrorMessage {

	private final ExtentTest extentTest;

	public Validator(String info, final ExtentTest extentTest) {
		super(info);
		this.errors = new ArrayList<>();
		this.extentTest = extentTest;
	}

	public <T, R> void validateCollectionsContains(final Collection<T> actual,
												   final Collection<T> expected,
												   final Function<T, R> keyFunction,
												   final String fieldName) {

		var actualNullSafe = CollectionUtils.copyAsImmutableList(actual);
		var expectedNullSafe = CollectionUtils.copyAsImmutableList(expected);
		super.validateCustomCollection(actualNullSafe, expectedNullSafe, keyFunction, fieldName);
	}

	public <T> void isNull(final T value, final String fieldName) {
		super.notNullAddErrorMessage(value, fieldName);
	}

	public <T> void notNull(final T value, final String fieldName) {
		super.isNullAddErrorMessage(value, fieldName);
	}

	public void validateEquals(final Object actual, final Object expected, final String fieldName) {
		super.notEqualsAddErrorMessage(actual, expected, fieldName);
	}

	public <T, R> void validateSize(final Collection<T> actual, final Collection<R> expected) {
		super.notSizeAddErrorMessage(actual, expected);
	}

	public void logToReporter() {
		if (CollectionUtils.isEmpty(this.errors)) {
			return;
		}
		final String keyMessage = this.info + Constant.LINE_SEPARATOR;
		final String messages = String.join(Constant.LINE_SEPARATOR, errors);
		final Markup markup = MarkupHelper.createCodeBlock(keyMessage + messages);
		extentTest.fail(markup);
	}
}