package com.walder.at.lib.validator;

import com.aventstack.extentreports.ExtentTest;
import com.walder.at.lib.petshop.dto.Status;
import com.walder.at.lib.petshop.dto.request.PetRequestDto;
import com.walder.at.lib.petshop.dto.response.PetResponseDto;
import com.walder.at.lib.validation.Validator;

import java.util.List;
import java.util.function.Function;

public class PetStoreValidator {

	public static void validatePetStoreResponse(final ExtentTest extentTest,
												final PetResponseDto actual,
												final PetRequestDto expected) {
		final Validator validator = new Validator("For pet response " + actual.getId(), extentTest);
		validator.validateEquals(actual.getName(), expected.getName() + "Mike", "name");
		validator.validateEquals(actual.getStatus(), Status.AVAILABLE, "status");
		final var expectedUrls = List.of("url1", "url4", "url5");
		validator.validateCollectionsContains(
				actual.getPhotoUrls(),
				expectedUrls,
				Function.identity(),
				"photoUrls"
		);
		validator.logToReporter();
	}
}
