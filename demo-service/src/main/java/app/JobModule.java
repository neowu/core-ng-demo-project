package app;

import app.demo.job.DemoJob;
import core.framework.api.Module;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * @author neo
 */
public class JobModule extends Module {
    @Override
    protected void initialize() {
        DemoJob job = bind(DemoJob.class);
//        schedule().fixedRate("fixed-rate-job", job, Duration.ofSeconds(15));
        LocalTime time = LocalTime.now().plusSeconds(10).truncatedTo(ChronoUnit.SECONDS);
        schedule().dailyAt("daily-job", job, time);
        schedule().weeklyAt("weekly-job", job, DayOfWeek.MONDAY, time);
        schedule().monthlyAt("monthly-job", job, 10, time);
    }
}
