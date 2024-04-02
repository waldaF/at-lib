package com.walder.at.lib.exceptions;

import com.google.gson.JsonSyntaxException;

public class DeserializationUtilsException extends CommonAtLibException {
	public DeserializationUtilsException(String message, JsonSyntaxException cause) {
		super(message, cause);
	}

}
