package app.demo;

import core.framework.test.EnvResourceValidator;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author neo
 */
class EnvConfTest {
    @Test
    void validateEnvResource() throws IOException {
        new EnvResourceValidator().validate();
    }
}
