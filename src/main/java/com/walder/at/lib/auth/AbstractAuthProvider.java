package com.walder.at.lib.auth;

import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@SuperBuilder
public abstract class AbstractAuthProvider {
	protected final String url;
	protected final String username;
	@Getter(value = AccessLevel.NONE)
	protected final String password;

	protected abstract AuthType authType();

	protected abstract String resolveToken();

	private String resolveAuthHeader() {
		return String.format("%s %s", authType(), resolveToken());
	}

	public Invocation.Builder addAuthHeader(Invocation.Builder builder) {
		if (Objects.nonNull(authType())) {
			return builder.header(HttpHeaders.AUTHORIZATION, resolveAuthHeader());
		}
		return builder;
	}
}
