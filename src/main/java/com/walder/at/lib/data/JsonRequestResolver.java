package com.walder.at.lib.data;

import com.walder.at.lib.enumerations.HttpOperation;
import com.walder.at.lib.utils.SerializationUtils;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

public interface JsonRequestResolver {
	default String resolvePatchPath(final String url) {
		throw new UnsupportedOperationException("Unsupported operation PATCH");
	}

	default String resolveGetPath(final String url) {
		throw new UnsupportedOperationException("Unsupported operation GET");
	}

	default String resolvePutPath(final String url) {
		throw new UnsupportedOperationException("Unsupported operation PUT");
	}

	default String resolvePostPath(final String url) {
		throw new UnsupportedOperationException("Unsupported operation POST");
	}

	default String resolveDeletePath(final String url) {
		throw new UnsupportedOperationException("Unsupported operation DELETE");
	}

	default String transformPayload() {
		return SerializationUtils.serialize(this);
	}

	default MediaType getMediaType() {
		return MediaType.APPLICATION_JSON_TYPE;
	}

	default String getCalledUri(final HttpOperation httpOperation,
								final String url) {

		return Map.of(httpOperation, resolvePath(url, httpOperation)).get(httpOperation);
	}

	default String resolvePath(final String url, final HttpOperation httpOperation) {
		return switch (httpOperation) {
			case PATCH -> resolvePatchPath(url);
			case POST -> resolvePostPath(url);
			case PUT -> resolvePutPath(url);
			case GET -> resolveGetPath(url);
			case DELETE -> resolveDeletePath(url);
		};
	}
}
