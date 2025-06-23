package com.walder.at.lib.petshop.dto.response.whalebone;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
public class TeamsResponse {
    private final List<Team> teams;
}
