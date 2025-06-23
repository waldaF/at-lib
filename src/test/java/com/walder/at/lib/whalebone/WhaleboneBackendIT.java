package com.walder.at.lib.whalebone;

import com.aventstack.extentreports.ExtentTest;
import com.walder.at.lib.SuiteGroup;
import com.walder.at.lib.petshop.dto.request.whalebone.TeamsRequestDto;
import com.walder.at.lib.petshop.dto.response.whalebone.TeamsResponse;
import com.walder.at.lib.petshop.dto.vo.MoreThanOneTeamHelperVo;
import com.walder.at.lib.petshop.dto.vo.TeamByDiviosionHelperVo;
import com.walder.at.lib.petshop.dto.vo.TeamsResponseVo;
import com.walder.at.lib.report.ExtentReportManager;
import com.walder.at.lib.validator.TeamsValidator;
import com.walder.at.lib.wrapper.ClientWrapper;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.List;

@Test(groups = {SuiteGroup.Whalebone.WHALEBONE_E2E})
@Slf4j
public class WhaleboneBackendIT extends WhaleBackendGenericIT {
	@Test(description =
			"<ul>" +
					"<li>Call https://qa-assignment.dev1.whalebone.io/api/teams and verify foloowing results </li>" +
					"<li>verify the response returned expected count of teams (32 in total)</li>" +
					"<li>verify the oldest team is Montreal Canadiens</li>" +
					"<li>verify there's a city with more than 1 team and verify names of those teams</li>" +
					"<li>verify there are 8 teams in the Metropolitan division and verify them by their names</li>" +
					"</ul>")
	public void whaleboneBackendGetFlowVerifyCountWithOldestTeam() {
		final ExtentTest extentTest = ExtentReportManager.getTest();

		final var moreThanOneTeamHelperVo = new MoreThanOneTeamHelperVo(2, List.of("New York Islanders", "New York Rangers"));
		final var teamByDiviosionHelperVo = new TeamByDiviosionHelperVo("Metropolitan division",List.of("Testteam Name", "Ttrest team name"));

		final var expectedResultVo = TeamsResponseVo.builder()
				.teamSize(EXPECTED_TEAM_COUNT)
				.grandpaTeamName(EXPECTED_GRADPA_TEAM_NAME_AWORD_ZISKAVA)
				.moreThanOneTeamHelperVo(moreThanOneTeamHelperVo)
				.teamByDivisionHelperVo(teamByDiviosionHelperVo)
				.build();

		final TeamsRequestDto request = new TeamsRequestDto();

		final TeamsResponse actualResponse = ClientWrapper.get(this.whaleBoneClientUnauth, request, extentTest)
				.load(TeamsResponse.class);
		TeamsValidator.validateTeamResponse(extentTest, actualResponse, expectedResultVo);
	}

	private static final Integer EXPECTED_TEAM_COUNT = 33;
	private static final String EXPECTED_GRADPA_TEAM_NAME_AWORD_ZISKAVA = "Montreal Canadiensss";

}

