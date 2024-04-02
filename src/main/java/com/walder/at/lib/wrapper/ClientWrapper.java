package com.walder.at.lib.wrapper;

import com.aventstack.extentreports.ExtentTest;
import com.walder.at.lib.client.Client;
import com.walder.at.lib.data.JsonRequestResolver;
import com.walder.at.lib.enumerations.HttpOperation;
import com.walder.at.lib.response.ResponseVo;
import com.walder.at.lib.utils.ExtentTestLogUtils;
import lombok.experimental.UtilityClass;

import java.util.function.BiFunction;

@UtilityClass
public class ClientWrapper {

	public static ResponseVo grant(final Client client, final JsonRequestResolver request, final ExtentTest extentTest) {
		return execute(Client::grant, HttpOperation.POST, new HelperVo(client, request, extentTest));
	}

	public static ResponseVo grantOk(final Client client, final JsonRequestResolver request, final ExtentTest extentTest) {
		return execute(Client::grantOk, HttpOperation.POST, new HelperVo(client, request, extentTest));
	}

	public static ResponseVo grantBadRequest(final Client client,
											 final JsonRequestResolver request,
											 final ExtentTest extentTest) {
		return execute(Client::grantBadRequest, HttpOperation.POST,
				new HelperVo(client, request, extentTest));
	}

	public static ResponseVo get(final Client client, final JsonRequestResolver request, final ExtentTest extentTest) {
		return execute(Client::get, HttpOperation.GET, new HelperVo(client, request, extentTest));
	}

	public static ResponseVo getNotFound(final Client client, final JsonRequestResolver request, final ExtentTest extentTest) {
		return execute(Client::getNotFound, HttpOperation.GET,
				new HelperVo(client, request, extentTest));
	}

	public static ResponseVo getBadRequest(final Client client, final JsonRequestResolver request, final ExtentTest extentTest) {
		return execute(Client::getBadRequest, HttpOperation.GET,
				new HelperVo(client, request, extentTest));
	}

	private ResponseVo execute(final BiFunction<Client, JsonRequestResolver, ResponseVo> call,
							   final HttpOperation httpOperation,
							   final HelperVo helperVo) {
		final var request = helperVo.request;
		final var client = helperVo.client;
		final var extentTest = helperVo.extentTest;
		ExtentTestLogUtils.logRequest(extentTest, client, httpOperation, request);
		var responseVo = call.apply(client, request);
		ExtentTestLogUtils.logResponse(extentTest, responseVo);
		return responseVo;
	}

	private record HelperVo(Client client, JsonRequestResolver request, ExtentTest extentTest) {
	}
}
