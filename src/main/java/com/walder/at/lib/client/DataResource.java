package com.walder.at.lib.client;

import com.walder.at.lib.auth.AbstractAuthProvider;
import com.walder.at.lib.enumerations.ComplianceHttpStatus;
import com.walder.at.lib.response.ResponseVo;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

@RequiredArgsConstructor
@Getter
class DataResource {
	private final AbstractAuthProvider authProvider;

	 String getUrl() {
		return authProvider.getUrl();
	}

	 ResponseVo get(final WebTargetRequestVo webTargetRequestVo) {
		final var response = resolveAuthAndAccept(webTargetRequestVo, Invocation.Builder::get);
		return from(webTargetRequestVo, null, response);
	}

	 ResponseVo put(final WebTargetRequestVo webTargetRequestVo) {
		final var request = webTargetRequestVo.getRequestResolver();
		final var data = request.transformPayload();
		final var response = resolveAuthAndAccept(
				webTargetRequestVo,
				res -> res.put(Entity.entity(data, request.getMediaType()))
		);
		return from(webTargetRequestVo, data, response);
	}

	 ResponseVo post(final WebTargetRequestVo webTargetRequestVo) {
		final var request = webTargetRequestVo.getRequestResolver();
		final var data = request.transformPayload();
		final var response = resolveAuthAndAccept(
				webTargetRequestVo,
				res -> res.post(Entity.entity(data, request.getMediaType()))
		);
		return from(webTargetRequestVo, data, response);
	}

	private Response resolveAuthAndAccept(final WebTargetRequestVo webTargetRequestVo,
										final Function<Invocation.Builder, Response> function) {
		final var request = webTargetRequestVo.getWebTarget().request();
		final var invocationBuilder = authProvider.addAuthHeader(request)
				.accept(webTargetRequestVo.getRequestResolver().getMediaType());
		return function.apply(invocationBuilder);
	}

	private ResponseVo from(final WebTargetRequestVo webTargetRequestVo,
							final String data,
							final Response response) {

		final var startTime = webTargetRequestVo.getStartTime();
		final var endTime = LocalDateTime.now();
		final String responseAsString = response.readEntity(String.class);
		final var actualStatus = response.getStatusInfo();
		final var expectedStatus = webTargetRequestVo.getExpectedHttpStatus();
		final var complianceStatus = actualStatus.getStatusCode() == expectedStatus.getStatusCode()
				? ComplianceHttpStatus.TRUE
				: ComplianceHttpStatus.FALSE;

		final Long requestDuration = ChronoUnit.MILLIS.between(webTargetRequestVo.getStartTime(), endTime);
		final WebTarget targetUri = webTargetRequestVo.getWebTarget();

		return ResponseVo.builder()
				.requestAsString(data)
				.responseAsString(responseAsString)
				.uri(targetUri.getUri().toString())
				.httpOperation(webTargetRequestVo.getHttpOperation())
				.start(startTime)
				.end(endTime)
				.requestDurationMillis(requestDuration)
				.actualStatus(actualStatus)
				.expectedStatus(expectedStatus)
				.complianceHttpStatus(complianceStatus)
				.queryParams(targetUri.getUri().getQuery())
				.build();
	}
}