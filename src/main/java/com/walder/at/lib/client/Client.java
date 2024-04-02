package com.walder.at.lib.client;

import com.walder.at.lib.auth.AbstractAuthProvider;
import com.walder.at.lib.data.JsonRequestResolver;
import com.walder.at.lib.enumerations.HttpOperation;
import com.walder.at.lib.exceptions.HttpMethodNotSupportedException;
import com.walder.at.lib.response.ResponseVo;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public class Client {
	private final DataResource dataResource;

	public Client(AbstractAuthProvider abstractAuthProvider) {
		this.dataResource = new DataResource(abstractAuthProvider);
	}

	public ResponseVo grant(final JsonRequestResolver request) {
		return sendRest(request, HttpOperation.POST, Response.Status.CREATED);
	}

	public ResponseVo grantOk(final JsonRequestResolver request) {
		return sendRest(request, HttpOperation.POST, Response.Status.OK);
	}

	public ResponseVo grantBadRequest(final JsonRequestResolver request) {
		return sendRest(request, HttpOperation.POST, Response.Status.BAD_REQUEST);
	}

	public ResponseVo put(final JsonRequestResolver request) {
		return sendRest(request, HttpOperation.PUT, Response.Status.OK);
	}

	public ResponseVo putBadRequest(final JsonRequestResolver request) {
		return sendRest(request, HttpOperation.PUT, Response.Status.BAD_REQUEST);
	}

	public ResponseVo get(final JsonRequestResolver request) {
		return sendRest(request, HttpOperation.GET, Response.Status.OK);
	}

	public ResponseVo getNotFound(final JsonRequestResolver request) {
		return sendRest(request, HttpOperation.GET, Response.Status.NOT_FOUND);
	}

	public ResponseVo getBadRequest(final JsonRequestResolver request) {
		return sendRest(request, HttpOperation.GET, Response.Status.BAD_REQUEST);
	}

	public String getUrl() {
		return this.dataResource.getUrl();
	}

	private ResponseVo sendRest(final JsonRequestResolver requestDto,
								final HttpOperation httpOperation,
								final Response.StatusType expectedHttpStatus) {

		final LocalDateTime start = LocalDateTime.now();
		final var clientBuilder = new ClientBuilderSslHandler().withFakeSslContext();
		try (var client = clientBuilder.build()) {

			final var webTarget = client.target(requestDto.resolvePath(dataResource.getUrl(), httpOperation));
			final WebTargetRequestVo webTargetRequestVo = WebTargetRequestVo.builder()
					.webTarget(webTarget)
					.requestResolver(requestDto)
					.httpOperation(httpOperation)
					.expectedHttpStatus(expectedHttpStatus)
					.build();
			return executeAndMapToResponseVo(webTargetRequestVo);
		} catch (Exception ex) {
			final LocalDateTime endTime = LocalDateTime.now();
			return ResponseVo.builder()
					.requestAsString(requestDto.transformPayload())
					.responseAsString(ex.getMessage())
					.expectedStatus(expectedHttpStatus)
					.actualStatus(Response.Status.INTERNAL_SERVER_ERROR)
					.httpOperation(httpOperation)
					.start(start)
					.end(endTime)
					.requestDurationMillis(ChronoUnit.MILLIS.between(start, endTime))
					.exception(ex)
					.build();
		}
	}

	private ResponseVo executeAndMapToResponseVo(final WebTargetRequestVo webTargetRequestVo) {
		return switch (webTargetRequestVo.getHttpOperation()) {
			case POST -> this.dataResource.post(webTargetRequestVo);
			case PUT -> this.dataResource.put(webTargetRequestVo);
			case GET -> this.dataResource.get(webTargetRequestVo);
			default -> throw new HttpMethodNotSupportedException("Not supported HTTP method");
		};
	}
}