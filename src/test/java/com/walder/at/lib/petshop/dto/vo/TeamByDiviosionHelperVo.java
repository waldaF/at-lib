package com.walder.at.lib.petshop.dto.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class TeamByDiviosionHelperVo {
	private final String divisionName;
	private final List<String> teamsByDivisionResult;
}
