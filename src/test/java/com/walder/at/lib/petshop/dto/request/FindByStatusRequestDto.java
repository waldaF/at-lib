package com.walder.at.lib.petshop.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.walder.at.lib.data.JsonRequestResolver;
import com.walder.at.lib.petshop.dto.Status;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FindByStatusRequestDto implements JsonRequestResolver {

	private static final String PATH = "/v2/pet/findByStatus";
	@JsonIgnore
	private final Status status;

	@Override
	public String resolveGetPath(final String url) {
		try (Client client = ClientBuilder.newClient()) {
			final WebTarget webTarget = client.target(url.concat(PATH));
			if (this.status == null) {
				return webTarget.getUri().toString();
			}
			return webTarget.queryParam("status", status.name().toLowerCase()).getUri().toString();
		}
	}
}
