package com.walder.at.lib.auth;


import lombok.experimental.SuperBuilder;

@SuperBuilder
public class UnAuthProvider extends AbstractAuthProvider {
	@Override
	protected AuthType authType() {
		return null;
	}

	@Override
	protected String resolveToken() {
		return null;
	}
}

