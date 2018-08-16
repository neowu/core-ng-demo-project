package app.demo;

import app.DemoServiceApp;
import core.framework.http.HTTPClient;
import core.framework.test.module.AbstractTestModule;

import static org.mockito.Mockito.mock;

/**
 * @author neo
 */
public class TestModule extends AbstractTestModule {
    @Override
    protected void initialize() {
        overrideBinding(HTTPClient.class, mock(HTTPClient.class));

        load(new DemoServiceApp());
    }
}
