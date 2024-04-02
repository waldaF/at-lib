package com.walder.at.lib.petshop;

import com.aventstack.extentreports.ExtentTest;
import com.walder.at.lib.SuiteGroup;
import com.walder.at.lib.petshop.dto.Status;
import com.walder.at.lib.petshop.dto.request.FindByIdRequestDto;
import com.walder.at.lib.petshop.dto.request.FindByStatusRequestDto;
import com.walder.at.lib.petshop.dto.request.PetRequestDto;
import com.walder.at.lib.petshop.dto.response.Category;
import com.walder.at.lib.petshop.dto.response.PetResponseDto;
import com.walder.at.lib.provider.KeyProvider;
import com.walder.at.lib.report.ExtentReportManager;
import com.walder.at.lib.validation.Validator;
import com.walder.at.lib.validator.PetStoreValidator;
import com.walder.at.lib.wrapper.ClientWrapper;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

@Test(groups = {SuiteGroup.PetStore.E2E})
@Slf4j
public class PetShopIT extends PetStoreGenericIT {
	@Test(description =
			"<ul>" +
					"<li>Dataset contains all statuses for creating pet store data</li>" +
					"<li>Grant thata, then get via new Id and validat eresponses are same</li>" +
					"<li>Just shown using E2E rest api testing via Java</li>" +
					"<li>Test can call another Rest API, kafka, RabbitMQ etc it is just shown</li>" +
					"<li>Test validates that are created. There are added fake values to see how validation is shown</li>" +
					"</ul>",
			dataProvider = "petStoreDataset")
	public void petStoreFlow(final Status status) {
		final ExtentTest extentTest = ExtentReportManager.getTest();

		final PetRequestDto request = PetRequestDto.builder()
				.status(status)
				.name("AwesomePet")
				.photoUrls(List.of("url1", "url2", "url3"))
				.category(new Category(1, "test"))
				.build();
		final PetResponseDto grantResponse = ClientWrapper.grantOk(this.petStoreClient, request, extentTest)
				.load(PetResponseDto.class);
		extentTest.info("Granted pet " + grantResponse.getName());
		PetStoreValidator.validatePetStoreResponse(extentTest, grantResponse, request);

		final var requestGet = new FindByIdRequestDto(grantResponse.getId());
		final PetResponseDto getResponse =ClientWrapper.get(this.petStoreClient, requestGet, extentTest)
				.load(PetResponseDto.class);
		PetStoreValidator.validatePetStoreResponse(extentTest, getResponse, request);
	}

	@Test(description =
			"<ul>" +
					"<li>Find all by status</li>" +
					"</ul>",
			dataProvider = "petStoreDataset")
	public void petStoreFindAll(final Status status) {
		final ExtentTest extentTest = ExtentReportManager.getTest();
		final var findAll = new FindByStatusRequestDto(status);
		ClientWrapper.get(this.petStoreClient, findAll, extentTest);
	}

	@DataProvider()
	public static Object[][] petStoreDataset() {
		return new Object[][]{
				{Status.AVAILABLE},
				{Status.PENDING},
				{Status.SOLD}
		};
	}

}
