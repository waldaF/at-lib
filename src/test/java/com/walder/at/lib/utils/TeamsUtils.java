package com.walder.at.lib.utils;

import com.walder.at.lib.dto.response.whalebone.Team;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@UtilityClass
public class TeamsUtils {
	private static final int EXPECTED_OCCURRENCES = 1;

	private static Predicate<Team> FILTER_BY_DIVISION_PREDICATE(final String divisionName) {
		return team -> team.getDivision() != null && divisionName.equals(team.getDivision().getName());
	}

	public static String findOldestTeam(final List<Team> teams) {
		if (teams == null || teams.isEmpty()) {
			return null;
		}
		return teams.stream()
				.min(Comparator.comparingInt(Team::getFounded))
				.get()
				.getName();
	}

	public static List<String> findCitiesHavingMoreThanOneTeam(final List<Team> teams) {
		if (teams == null || teams.isEmpty()) {
			return Collections.emptyList();
		}

		return teams.stream()
				.collect(Collectors.groupingBy(Team::getLocation))
				.entrySet()
				.stream()
				.filter(entry -> entry.getValue().size() > EXPECTED_OCCURRENCES)
				.map(Map.Entry::getKey)
				.toList();
	}

	public static List<String> findTeamsByDivision(final String filterByDivisionName,
												   final List<Team> teams) {
		if (teams == null || teams.isEmpty()) {
			return Collections.emptyList();
		}
		if (filterByDivisionName == null || filterByDivisionName.isEmpty()) {
			return Collections.emptyList();
		}

		return teams.stream()
				.filter(FILTER_BY_DIVISION_PREDICATE(filterByDivisionName))
				.map(Team::getLocation)
				.toList();
	}
}
