package com.walder.at.lib.petshop.dto.response;

import com.walder.at.lib.petshop.dto.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class PetResponseDto {
	private final Status status;
	private final Long id;
	private final String name;
	private final Category category;
	private final List<String> photoUrls;
}
