package app.demo.job;

import core.framework.scheduler.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author neo
 */
public class DemoJob implements Job {
    private final Logger logger = LoggerFactory.getLogger(DemoJob.class);

    @Override
    public void execute() {
        logger.debug("run job");
    }
}
