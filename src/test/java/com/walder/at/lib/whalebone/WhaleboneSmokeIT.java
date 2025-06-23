package com.walder.at.lib.whalebone;

import com.walder.at.lib.SuiteGroup;
import com.walder.at.lib.petshop.dto.request.whalebone.TeamsRequestDto;
import com.walder.at.lib.report.ExtentReportManager;
import com.walder.at.lib.wrapper.ClientWrapper;
import org.testng.annotations.Test;

@Test(groups = {SuiteGroup.Whalebone.WHALEBONE_SMOKE})
public class WhaleboneSmokeIT extends WhaleBackendGenericIT {
	@Test
	public void health() {
		var extentTest = ExtentReportManager.getTest();
		ClientWrapper.get(this.whaleBoneClientUnauth, new TeamsRequestDto(), extentTest);
	}
}

