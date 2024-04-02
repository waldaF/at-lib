package com.walder.at.lib.petshop;

import com.walder.at.lib.SuiteGroup;
import com.walder.at.lib.petshop.dto.Status;
import com.walder.at.lib.wrapper.ClientWrapper;
import com.walder.at.lib.petshop.dto.request.FindByStatusRequestDto;
import com.walder.at.lib.report.ExtentReportManager;
import org.testng.annotations.Test;

@Test(groups = {SuiteGroup.PetStore.SMOKE})
public class PetShopSmokeIT extends PetStoreGenericIT {
	@Test
	public void health() {
		var extentTest = ExtentReportManager.getTest();
		ClientWrapper.get(this.petStoreClient, new FindByStatusRequestDto(Status.UNKNOWN), extentTest);
	}
}