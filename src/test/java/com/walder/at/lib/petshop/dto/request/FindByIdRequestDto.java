package com.walder.at.lib.petshop.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.walder.at.lib.data.JsonRequestResolver;
import lombok.AllArgsConstructor;
import org.glassfish.jersey.uri.UriTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
public class FindByIdRequestDto implements JsonRequestResolver {
	private static final String PATH = "/v2/pet/{petId}";
	@JsonIgnore
	private final Long id;

	@Override
	public String resolveGetPath(final String url) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("petId", Objects.isNull(this.id) ? "" : "" + this.id);
		return new UriTemplate(url.concat(PATH)).createURI(parameters);
	}
}
