package com.walder.at.lib.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthType {
	BASIC("Basic"),
	BEARER("Bearer");
	private final String name;
}