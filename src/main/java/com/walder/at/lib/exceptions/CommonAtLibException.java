package com.walder.at.lib.exceptions;

public class CommonAtLibException extends RuntimeException {
	protected CommonAtLibException(String message) {
		super(message);
	}
	protected CommonAtLibException(String message, Throwable cause) {
		super(message, cause);
	}
}
