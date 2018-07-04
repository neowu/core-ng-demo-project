package app.demo;

import org.junit.jupiter.api.Test;

import static core.framework.test.Assertions.assertConfDirectory;


/**
 * @author neo
 */
class ConfigTest extends IntegrationTest {
    @Test
    void conf() {
        assertConfDirectory().overridesDefaultResources();
    }
}
