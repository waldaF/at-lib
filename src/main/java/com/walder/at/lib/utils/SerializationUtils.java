package com.walder.at.lib.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.walder.at.lib.exceptions.DeserializationUtilsException;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.BiFunction;

import static com.walder.at.lib.enumerations.DateFormat.UTC_DATE_TIME;

@UtilityClass
public class SerializationUtils {
	private static final Gson gson = init().create();
	private static final Gson gsonUpperCamelCase = init().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
	private static final Gson gsonLowerCamelCaseUnderscores = init().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

	public static String serialize(final Object object) {
		return isEmpty(object) ? null : gson.toJson(object);
	}

	public static String serializeUpperCamelCase(final Object object) {
		return isEmpty(object) ? null : gsonUpperCamelCase.toJson(object);
	}

	public static String serializeLowerLowerCamelCaseUnderscores(final Object object) {
		return isEmpty(object) ? null : gsonLowerCamelCaseUnderscores.toJson(object);
	}

	public static <T> T deserialize(final String json, Class<T> tClass) {
		return commonFunctionWrapper(json, tClass, gson::fromJson);
	}

	public static <T> T deserializeUpperCamelCase(final String json, Class<T> tClass) {
		return commonFunctionWrapper(json, tClass, gsonUpperCamelCase::fromJson);
	}

	public static <T> T deserializeLowerCamelCaseUnderscores(final String json, Class<T> tClass) {
		return commonFunctionWrapper(json, tClass, gsonLowerCamelCaseUnderscores::fromJson);
	}
	private static <T> T commonFunctionWrapper(final String json,
											   final Class<T> clazz,
											   final BiFunction<String, Class<T>, T> biFunction) {

		try {
			return biFunction.apply(json, clazz);
		} catch (JsonSyntaxException jse) {
			final String errMsg = "Error during deserialize " + json + "\nTO " + clazz.getName() + ".class\n";
			throw new DeserializationUtilsException(errMsg, jse);
		}
	}

	private static GsonBuilder init() {
		return new GsonBuilder()
				.addSerializationExclusionStrategy(new ExclusionStrategy() {
					@Override
					public boolean shouldSkipField(FieldAttributes fieldAttributes) {
						return fieldAttributes.getAnnotation(JsonIgnore.class) != null;
					}

					@Override
					public boolean shouldSkipClass(Class<?> aClass) {
						return false;
					}
				})
				.setDateFormat(UTC_DATE_TIME.getValue())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
	}

	private static boolean isEmpty(final Object object) {
		return object == null || "null".equals(object);
	}

	static class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
		private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(UTC_DATE_TIME.getValue());

		@Override
		public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
			return new JsonPrimitive(formatter.format(localDateTime));
		}
	}

	static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
		@Override
		public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime();
		}
	}
}