package app;

import app.demo.job.DemoJob;
import core.framework.module.Module;

import java.time.DayOfWeek;
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
        schedule().secondly("fixed-rate-job", job, 5);
        LocalTime time = LocalTime.now().minusHours(3).plusSeconds(10).truncatedTo(ChronoUnit.SECONDS);

        schedule().dailyAt("daily-job", job, time);
        schedule().weeklyAt("weekly-job", job, DayOfWeek.MONDAY, time);
        schedule().monthlyAt("monthly-job", job, 10, time);
    }
}
