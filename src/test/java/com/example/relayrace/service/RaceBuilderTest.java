package com.example.relayrace.service;

import com.example.relayrace.domain.Runner;
import com.example.relayrace.domain.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RaceBuilderTest {

    private static int LOWER_BOUND_IN_MILLIS = 9500;
    private static int UPPER_BOUND_IN_MILLIS = 10500;

    @Mock
    private Random random;

    @InjectMocks
    private RaceBuilder builder;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        when(random.ints(LOWER_BOUND_IN_MILLIS, UPPER_BOUND_IN_MILLIS))
                .thenAnswer(invocation -> IntStream.builder().add(10000).build());
    }

    @Test
    void shouldBuildEmptyRace() {

        // when
        List<Team> result = builder.build(0, 0);

        // then
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void shouldBuildTeam1WithName() {
        // when
        List<Team> result = builder.build(1, 0);

        // then
        assertEquals(List.of(team("Team 1")), result);
    }

    @Test
    void shouldBuildMultipleTeamsWithName() {
        // when
        List<Team> result = builder.build(2, 0);

        // then
        assertEquals(List.of(team("Team 1"), team("Team 2")), result);
    }

    @Test
    void shouldBuildTeamWithRunner() {
        // when
        List<Team> result = builder.build(1, 1);

        // then
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getRunners().size());
    }

    @Test
    void shouldBuildTeamWithMultipleRunners() {
        // when
        List<Team> result = builder.build(1, 5);

        // then
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getRunners().size());
    }

    @Test
    void shouldBuildTeamWithRunnerWithDuration() {
        // when
        List<Team> result = builder.build(1, 1);

        // then
        assertEquals(List.of(team(List.of(new Runner(Duration.of(10, ChronoUnit.SECONDS))))), result);
    }

    private Team team(String name) {
        return Team.builder()
                .name(name)
                .runners(Collections.emptyList())
                .build();
    }

    private Team team(List<Runner> runners) {
        return Team.builder()
                .name("Team 1")
                .runners(runners)
                .build();
    }
}