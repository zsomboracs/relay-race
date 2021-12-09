package com.example.relayrace.service;

import com.example.relayrace.domain.Runner;
import com.example.relayrace.domain.Team;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class RaceBuilder {

    private static final String TEAM_NAME_PATTERN = "Team %d";

    private static int LOWER_BOUND_IN_MILLIS = 9500;
    private static int UPPER_BOUND_IN_MILLIS = 10500;

    private final Random random;

    public List<Team> build(int numberOfTeams, int teamSize) {
        return IntStream.rangeClosed(1, numberOfTeams)
                .mapToObj(i -> buildTeam(i, teamSize))
                .collect(Collectors.toList());
    }

    private Team buildTeam(int index, int size) {
        return Team.builder()
                .name(String.format(TEAM_NAME_PATTERN, index))
                .runners(IntStream.rangeClosed(1, size)
                        .mapToObj(j -> buildRunner())
                        .collect(Collectors.toList()))
                .build();
    }

    private Runner buildRunner() {
        int durationInMillis = random.ints(LOWER_BOUND_IN_MILLIS, UPPER_BOUND_IN_MILLIS)
                .findFirst()
                .getAsInt();
        return new Runner(Duration.of(durationInMillis, ChronoUnit.MILLIS));
    }

}
