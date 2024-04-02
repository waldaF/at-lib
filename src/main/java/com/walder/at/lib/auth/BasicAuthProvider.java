package com.walder.at.lib.auth;

import com.walder.at.lib.utils.Base64Utils;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class BasicAuthProvider extends AbstractAuthProvider {
	@Override
	protected AuthType authType() {
		return AuthType.BASIC;
	}

	@Override
	protected String resolveToken() {
		return Base64Utils.encodeAsAuthHeader(this.username, this.password);
	}
}