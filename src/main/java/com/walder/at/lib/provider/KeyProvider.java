package com.walder.at.lib.provider;

import com.walder.at.lib.utils.StringUtils;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class KeyProvider {

	public static String loadProperty(final String key) {
		return findProperty(key).orElseThrow(() -> new IllegalArgumentException("Not found value for key " + key));
	}

	public static Optional<String> findProperty(final String key) {
		final String propValue = System.getProperty(key);
		if (StringUtils.isEmpty(propValue)) {
			return Optional.empty();
		}
		return Optional.of(propValue);
	}
}
