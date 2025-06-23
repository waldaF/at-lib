package com.walder.at.lib.petshop.dto.request.whalebone;

import com.walder.at.lib.data.JsonRequestResolver;

public class TeamsRequestDto implements JsonRequestResolver {

	private static final String PATH = "/api/teams";

	@Override
	public String resolveGetPath(final String url) {
		return url.concat(PATH);
	}
}