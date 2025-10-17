import app.DemoSiteApp;
import core.framework.log.LogLevel;
import core.framework.log.LogLevels;

/**
 * @author neo
 */
void main() {
    LogLevels.add("com.test.", LogLevel.WARN, LogLevel.ERROR);
    LogLevels.add("com.test2.", LogLevel.WARN, LogLevel.WARN);

    new DemoSiteApp().start();
}
