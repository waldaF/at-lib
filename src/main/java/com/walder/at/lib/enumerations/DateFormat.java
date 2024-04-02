package com.walder.at.lib.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DateFormat {
	ISO_E8601DT_W_D("yyyy-MM-dd'T'HH:mm:ss"),
	UTC_DATE_TIME("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private final String value;
}
