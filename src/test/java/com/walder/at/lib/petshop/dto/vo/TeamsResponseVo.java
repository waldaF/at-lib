package com.walder.at.lib.petshop.dto.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Getter
public class TeamsResponseVo {
	private final int teamSize;
	private final String grandpaTeamName;
	private final MoreThanOneTeamHelperVo moreThanOneTeamHelperVo;
	private final TeamByDiviosionHelperVo teamByDivisionHelperVo;
}

