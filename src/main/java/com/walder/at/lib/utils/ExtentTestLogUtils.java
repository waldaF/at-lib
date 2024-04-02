package com.walder.at.lib.utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.walder.at.lib.client.Client;
import com.walder.at.lib.data.JsonRequestResolver;
import com.walder.at.lib.enumerations.ComplianceHttpStatus;
import com.walder.at.lib.enumerations.HttpOperation;
import com.walder.at.lib.response.ResponseVo;
import jakarta.ws.rs.core.Response;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class ExtentTestLogUtils {

	private static final Set<HttpOperation> BODY_OPERATIONS = Set.of(
			HttpOperation.POST,
			HttpOperation.PUT
	);

	public static void logRequest(final ExtentTest extentTest,
								  final Client client,
								  final HttpOperation httpOperation,
								  final JsonRequestResolver requestDto) {

		final var url = requestDto.getCalledUri(httpOperation, client.getUrl());
		final var markup = MarkupHelper.createLabel(httpOperation.name(), ExtentColor.BLUE).getMarkup();
		extentTest.info(String.format("%s %s", markup, url));
		if (BODY_OPERATIONS.contains(httpOperation)) {
			logBody(extentTest, requestDto);
		}
	}

	public static void logResponse(final ExtentTest extentTest, final ResponseVo responseVo) {
		logInnerResponseWhenFailed(extentTest, responseVo);
	}

	private static void logBody(final ExtentTest extentTest,
								final JsonRequestResolver request) {
		final var requestPayload = request.transformPayload();
		if (StringUtils.isNotEmpty(requestPayload)) {
			final Markup markup = MarkupHelper.createCodeBlock(requestPayload, CodeLanguage.JSON);
			extentTest.info(markup);
		}
	}

	private static void logInnerResponseWhenFailed(final ExtentTest extentTest,
												   final ResponseVo responseVo) {

		final ComplianceHttpStatus complianceHttpStatus = responseVo.getComplianceHttpStatus();
		final Response.StatusType expectedStatus = responseVo.getExpectedStatus();

		final Response.StatusType actualStatus = responseVo.getActualStatus();

		final String duration = String.format("Duration %s [ms]", responseVo.getRequestDurationMillis());
		extentTest.info(MarkupHelper.createLabel(duration, ExtentColor.CYAN));

		if (complianceHttpStatus == ComplianceHttpStatus.FALSE) {
			final String statusMsgFail = errorHttpStatus(
					new ActualStatus(actualStatus),
					new ExpectedStatus(expectedStatus)
			);
			final Markup markup = MarkupHelper.createCodeBlock(responseVo.getResponseAsString(), CodeLanguage.JSON);
			logInnerResponse(extentTest, markup, responseVo);
			extentTest.fail(statusMsgFail);
		} else {
			final String statusMsgInfo = String.format("Got the expected http status %s %s",
					actualStatus.getStatusCode(),
					actualStatus.getReasonPhrase()
			);
			extentTest.info(statusMsgInfo);
		}
	}

	private static void logInnerResponse(final ExtentTest extentTest,
										 final Markup markup,
										 final ResponseVo responseVo) {
		if (StringUtils.isEmpty(responseVo.getResponseAsString())) {
			extentTest.info(MarkupHelper.createLabel("EMPTY SERVER RESPONSE", ExtentColor.GREEN));
		} else {
			extentTest.info(MarkupHelper.createLabel("RESPONSE", ExtentColor.GREEN));
			extentTest.info(markup);
		}
	}

	private static String errorHttpStatus(final ActualStatus actualStatus,
										  final ExpectedStatus expectedStatus) {
		var expected = expectedStatus.value;
		var actual = actualStatus.value;
		return String.format("Expected http status %s %s but found %s %s ",
				expected.getStatusCode(),
				expected.getReasonPhrase(),
				actual.getStatusCode(),
				actual.getReasonPhrase()
		);
	}

	record ActualStatus(Response.StatusType value) {
	}

	record ExpectedStatus(Response.StatusType value) {
	}
}
