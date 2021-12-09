package com.example.relayrace.service;

import com.example.relayrace.domain.Runner;
import com.example.relayrace.domain.Team;
import com.example.relayrace.domain.TeamResult;
import com.example.relayrace.service.print.ResultPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.util.StopWatch;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RaceSimulationServiceTest {

    private static final Duration DURATION_200_MS = Duration.of(200, ChronoUnit.MILLIS);
    private static final Duration DURATION_100_MS = Duration.of(100, ChronoUnit.MILLIS);

    @Mock
    private RaceBuilder raceBuilder;

    @Mock
    private ResultPrinter printer;

    @Spy
    private ExecutorService executor = Executors.newFixedThreadPool(10);

    @InjectMocks
    private RaceSimulationService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private static Stream<Arguments> scenarios() {
        return Stream.of(
                Arguments.of(
                        "Empty race, no teams",
                        0, 0,
                        List.of(),
                        List.of(),
                        0L, 50L
                ),
                Arguments.of(
                        "One runner in one team",
                        1, 1,
                        List.of(team1()),
                        List.of(new TeamResult(1, team1(), DURATION_200_MS)),
                        200L, 250L
                ),
                Arguments.of(
                        "Multiple runners in one team",
                        1, 2,
                        List.of(team2()),
                        List.of(new TeamResult(1, team2(), DURATION_200_MS.plus(DURATION_100_MS))),
                        300L, 350L
                ),
                Arguments.of(
                        "Multiple teams with one runner in each",
                        2, 1,
                        List.of(team1(), team3()),
                        List.of(
                                new TeamResult(1, team3(), DURATION_100_MS),
                                new TeamResult(2, team1(), DURATION_200_MS)),
                        200L, 250L
                )
        );
    }

    @ParameterizedTest
    @MethodSource("scenarios")
    void shouldSimulateRace(String name, int numberOfTeams, int teamSize,
                            List<Team> teams, List<TeamResult> results,
                            long minDuration, long maxDuration) {
        // given
        when(raceBuilder.build(numberOfTeams, teamSize)).thenReturn(teams);
        StopWatch stopWatch = new StopWatch();

        // when
        stopWatch.start();
        service.simulate(numberOfTeams, teamSize);
        stopWatch.stop();

        // then
        stopWatch.getLastTaskTimeMillis();
        assertTrue(stopWatch.getLastTaskTimeMillis() >= minDuration);
        assertTrue(stopWatch.getLastTaskTimeMillis() <= maxDuration);
        results.forEach(result -> verify(printer).print(result));
        verify(executor, times(numberOfTeams)).execute(Mockito.any(Runnable.class));

    }

    private static Team team1() {
        return Team.builder()
                .runners(List.of(new Runner(DURATION_200_MS)))
                .build();
    }

    private static Team team2() {
        return Team.builder()
                .runners(List.of(new Runner(DURATION_200_MS), new Runner(DURATION_100_MS)))
                .build();
    }

    private static Team team3() {
        return Team.builder()
                .runners(List.of(new Runner(DURATION_100_MS)))
                .build();
    }

}