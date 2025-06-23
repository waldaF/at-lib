package com.walder.at.lib.petshop.dto.response.whalebone;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Team {
    private final String name;
    private String location;
    private final int founded;
    private final int firstYearOfPlay;
    private final Division division;
    private final String officialSiteUrl;
}
