package app;

import org.junit.jupiter.api.Test;

import static core.framework.test.Assertions.assertConf;


/**
 * @author neo
 */
class ConfigTest extends IntegrationTest {
    @Test
    void conf() {
        assertConf().overridesDefaultResources();
    }
}
