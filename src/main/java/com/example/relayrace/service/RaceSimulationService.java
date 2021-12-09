package com.example.relayrace.service;

import com.example.relayrace.domain.Runner;
import com.example.relayrace.domain.Team;
import com.example.relayrace.domain.TeamResult;
import com.example.relayrace.service.print.ResultPrinter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class RaceSimulationService {

    private final RaceBuilder raceBuilder;
    private final ResultPrinter printer;
    private final ExecutorService executorService;

    public void simulate(int numberOfTeams, int teamSize) {

        log.info("Simulation has been started!");

        AtomicInteger position = new AtomicInteger(1);

        List<Team> teams = raceBuilder.build(numberOfTeams, teamSize);

        /*
        Rationale behind choosing CompletableFuture:
        For this simple task Parallel Stream can work as well, because we don't need the composing feature of CompletableFuture.
        However in production environment it's more safe to use a solution which enables execution
        on a dedicated thread pool (instead of the common fork-join pool), especially if it's not a CPU bound task,
        thus applying a 'bulkhead pattern'. A dedicated thread pool also enables more accurate unit testing of the concurrency.
        In this task I rather stayed with what Java can provide out of the box, but Reactive extensions,
        such as Reactor and RxJava gives much more features and convenience.
         */

        CompletableFuture<Void> raceSimulation = CompletableFuture.allOf(teams.stream()
                .map(team -> CompletableFuture.runAsync(() -> simulate(team, position), executorService))
                .collect(Collectors.toList())
                .toArray(CompletableFuture[]::new));

        try {
            raceSimulation.get();
        } catch (Exception e) {
            log.error("Simulation has been failed!", e);
        } finally {
            executorService.shutdown();
        }

        log.info("Simulation completed!");

    }

    private void simulate(Team team, AtomicInteger position) {

        Duration totalDuration = team.getRunners()
                .stream()
                .map(this::simulate)
                .reduce(Duration.ZERO, Duration::plus);

        TeamResult result = new TeamResult(position.getAndIncrement(), team, totalDuration);

        printer.print(result);
    }

    private Duration simulate(Runner runner) {
        try {
            Duration duration = runner.getDuration();
            Thread.sleep(duration.toMillis());
            return duration;
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected interrupt happened!", e);
        }
    }

}
