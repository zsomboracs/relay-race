package com.example.relayrace.service.print;

import com.example.relayrace.domain.TeamResult;
import com.ibm.icu.text.RuleBasedNumberFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
public class SimpleResultLogger implements ResultPrinter {

    @Override
    public void print(TeamResult result) {

        RuleBasedNumberFormat numberFormat = new RuleBasedNumberFormat(Locale.ENGLISH, RuleBasedNumberFormat.ORDINAL);

        log.info("{} {} - {} seconds",
                numberFormat.format(result.getPosition()),
                result.getTeam().getName(),
                result.getDuration().toMillis() / 1000.0);
    }
}
