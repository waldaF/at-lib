package com.walder.at.lib.validator;

import com.aventstack.extentreports.ExtentTest;
import com.walder.at.lib.petshop.dto.response.whalebone.TeamsResponse;
import com.walder.at.lib.petshop.dto.vo.TeamsResponseVo;
import com.walder.at.lib.utils.TeamsUtils;
import com.walder.at.lib.validation.Validator;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.Function;

@UtilityClass
public class TeamsValidator {

	public static void validateTeamResponse(final ExtentTest extentTest,
											final TeamsResponse actual,
											final TeamsResponseVo expectedVo) {
		final Validator validator = new Validator("For teams response", extentTest);
		final String actualOldestTeam = TeamsUtils.findOldestTeam(actual.getTeams());

		final List<String> actualLocationsHaveMoreTeams = TeamsUtils.findCitiesHavingMoreThanOneTeam(actual.getTeams());
		validator.validateEquals(actual.getTeams().size(), expectedVo.getTeamSize(), "team size");
		validator.validateEquals(actualOldestTeam, expectedVo.getGrandpaTeamName(), "oldest team name");

		final var moreThanOneTeamHelperVo = expectedVo.getMoreThanOneTeamHelperVo();
		validator.validateEquals(actualLocationsHaveMoreTeams.size(), moreThanOneTeamHelperVo.getCities().size(), "number of cities with multiple teams");
		validator.validateCollectionsContains(actualLocationsHaveMoreTeams, moreThanOneTeamHelperVo.getCities(), Function.identity(), "cities with multiple team");

		final var expectedVoTeamByDivisionHelperVo = expectedVo.getTeamByDivisionHelperVo();
		final List<String> actualTeamsByDivision = TeamsUtils.findTeamsByDivision(
				expectedVoTeamByDivisionHelperVo.getDivisionName(),
				actual.getTeams()
		);
		validator.validateCollectionsContains(actualTeamsByDivision,
				expectedVoTeamByDivisionHelperVo.getTeamsByDivisionResult(),
				Function.identity(),
				"teams by division ".concat(expectedVoTeamByDivisionHelperVo.getDivisionName())
		);

		validator.logToReporterAndMarkFailedIfError();
	}
}
