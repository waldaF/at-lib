package com.walder.at.lib.dto.response.whalebone;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class TeamsResponse {
    private final List<Team> teams;
}
