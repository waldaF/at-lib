package com.walder.at.lib.whalebone;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class MoreThanOneTeamHelperVo {
	private final int moreThanOneTeam;
	private final List<String> cities;
}
