package com.walder.at.lib.response;

import com.google.gson.FieldNamingPolicy;
import com.walder.at.lib.enumerations.ComplianceHttpStatus;
import com.walder.at.lib.enumerations.HttpOperation;
import com.walder.at.lib.exceptions.InvalidClientResponseException;
import com.walder.at.lib.utils.SerializationUtils;
import jakarta.ws.rs.core.Response;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Builder
@Getter
public class ResponseVo {
	private final String requestAsString;
	private final String responseAsString;
	private final HttpOperation httpOperation;
	private final Throwable exception;
	private final String uri;
	private final String username;
	private final String queryParams;
	private final LocalDateTime start;
	private final LocalDateTime end;
	private final Long requestDurationMillis;
	private final Response.StatusType actualStatus;
	private final Response.StatusType expectedStatus;
	private final ComplianceHttpStatus complianceHttpStatus;

	private static final String EMPTY_RESPONSE = "Empty response for ";

	public <T> T load(final Class<T> clazz) {
		return getClazzResponse(clazz, FieldNamingPolicy.IDENTITY)
				.orElseThrow(() -> new InvalidClientResponseException(EMPTY_RESPONSE + clazz.getName()));
	}

	public <T> T loadCamelCasePayload(final Class<T> clazz) {
		return getClazzResponse(clazz, FieldNamingPolicy.UPPER_CAMEL_CASE)
				.orElseThrow(() -> new InvalidClientResponseException(EMPTY_RESPONSE + clazz.getName()));
	}

	private <T> Optional<T> getClazzResponse(final Class<T> clazz, final FieldNamingPolicy fieldNamingPolicy) {
		if (Objects.isNull(clazz)) {
			throw new IllegalArgumentException("Deserialize to null class argument is invalid.");
		}
		return this.getComplianceHttpStatus() == ComplianceHttpStatus.TRUE
				? deserializeJsonPayload(this.getResponseAsString(), clazz, fieldNamingPolicy)
				: Optional.empty();
	}

	private <T> Optional<T> deserializeJsonPayload(final String payload,
												   final Class<T> clazz,
												   final FieldNamingPolicy fieldNamingPolicy) {

		return switch (fieldNamingPolicy) {
			case UPPER_CAMEL_CASE -> Optional.ofNullable(SerializationUtils.deserializeUpperCamelCase(payload, clazz));
			default -> Optional.ofNullable(SerializationUtils.deserialize(payload, clazz));
		};
	}
}
