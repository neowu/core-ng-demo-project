package app;

import app.demo.job.DemoJob;
import core.framework.module.Module;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * @author neo
 */
public class JobModule extends Module {
    @Override
    protected void initialize() {
        schedule().timeZone(ZoneId.of("America/Los_Angeles"));

        DemoJob job = bind(DemoJob.class);
        LocalTime time = LocalTime.now().plusSeconds(5).truncatedTo(ChronoUnit.SECONDS);

        schedule().fixedRate("fixed-rate-job", job, Duration.ofSeconds(10));
        schedule().dailyAt("daily-job", job, time);

        schedule().trigger("trigger-job", job, previous -> previous.plusSeconds(30));

        schedule().weeklyAt("weekly-job", job, LocalDate.now().getDayOfWeek(), time);
        schedule().monthlyAt("monthly-job", job, 28, time);
    }
}
