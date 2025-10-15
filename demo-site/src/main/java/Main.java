import app.DemoSiteApp;
import core.framework.log.LogLevel;
import core.framework.log.LogLevels;

/**
 * @author neo
 */
void main() {
    LogLevels.infoLevel("com.test.", LogLevel.WARN);
    LogLevels.traceLevel("com.test.", LogLevel.INFO);

    new DemoSiteApp().start();
}
