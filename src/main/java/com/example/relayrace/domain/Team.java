package com.example.relayrace.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Team {

    private String name;
    private List<Runner> runners;

}
