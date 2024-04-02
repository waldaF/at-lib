package com.walder.at.lib.utils;

import lombok.experimental.UtilityClass;

import java.util.Base64;

@UtilityClass
public class Base64Utils {
	private static final Base64.Encoder ENCODER = Base64.getEncoder();

	public static String encodeAsAuthHeader(final String username, final String password) {
		if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password)) {
			return null;
		}
		return encode(String.format("%s:%s", username, password));
	}
	private static String encode(final String data) {
		if (StringUtils.isEmpty(data)) {
			return null;
		}

		return new String(ENCODER.encode(data.getBytes()));
	}
}
