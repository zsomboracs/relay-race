package com.example.relayrace;

import com.example.relayrace.service.RaceSimulationService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AppStartupRunner implements ApplicationRunner {

    private final RaceSimulationService simulation;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        simulation.simulate(6, 4);
    }
}