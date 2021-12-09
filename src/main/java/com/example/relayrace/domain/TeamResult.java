package com.example.relayrace.domain;

import lombok.Value;

import java.time.Duration;

@Value
public class TeamResult {

    private int position;
    private Team team;
    private Duration duration;

}
