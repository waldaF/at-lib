package com.walder.at.lib.petshop.dto.request;

import com.walder.at.lib.data.JsonRequestResolver;
import com.walder.at.lib.petshop.dto.Status;
import com.walder.at.lib.petshop.dto.response.Category;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder(toBuilder = true)
@Getter
public class PetRequestDto  implements JsonRequestResolver {
	private final Status status;
	private final Long id;
	private final String name;
	private final Category category;
	private final List<String> photoUrls;
	private static final String PATH = "/v2/pet";
	public String resolvePostPath(final String url) {
		return url.concat(PATH);
	}
	public String resolvePutPath(final String url) {
		return url.concat(PATH);
	}
}
