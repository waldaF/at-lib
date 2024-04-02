package com.walder.at.lib.utils;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@UtilityClass
public class CollectionUtils {
	public static <T> boolean isEmpty(T... data) {
		return Objects.isNull(data) || data.length == 0;
	}
	public static <T> boolean isEmpty(Collection<T> col) {
		return col == null || col.isEmpty();
	}
	public static <T> boolean isNotEmpty(Collection<T> col) {
		return !isEmpty(col);
	}

	public static <T> boolean isNotEmpty(T... data) {
		return !isEmpty(data);
	}

	public static <K, V> boolean isEmpty(final Map<K, V> map) {
		return map == null || map.isEmpty();
	}

	public static <K, V> boolean isNotEmpty(final Map<K, V> map) {
		return !isEmpty(map);
	}

	public static <T> List<T> copyAsImmutableList(Collection<T> in) {
		return Objects.isNull(in) || in.isEmpty() ? Collections.emptyList() : List.copyOf(in);
	}
}