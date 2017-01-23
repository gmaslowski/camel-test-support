package com.gmaslowski.camel.test.integration;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockComponent;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.mockito.MockitoAnnotations.initMocks;

public abstract class CamelRouteIntegrationTestBase extends CamelTestSupport {

    private static final Logger log = LoggerFactory.getLogger(CamelRouteIntegrationTestBase.class);
    private static final String DIRECT_SCHEME = "direct";

    public CamelRouteIntegrationTestBase() {
        super();
        initMocks(this);
    }

    protected abstract CamelRouteIntegrationTestConfiguration testConfiguration();

    @Before
    public void startCamel() throws Exception {

        context.getRouteDefinition(testConfiguration().getRouteId())
                .adviceWith(context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        replaceFromEndpoint(testConfiguration().getMockFromEndpointName(), this);
                        replaceToEndpoints(testConfiguration().getMockToEndpoints(), this);
                    }
                });

        mockComponentForSchemes(testConfiguration().getMockSchemes());

        context.start();
        template = context.createProducerTemplate();
    }

    @After
    public void stopCamel() throws Exception {
        context.stop();
    }

    protected <T> void givenMockReturns(MockEndpoint endpoint, T value) {
        endpoint.returnReplyBody(new Expression() {
            @Override
            public T evaluate(Exchange exchange, Class type) {
                return value;
            }
        });
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    public boolean isDumpRouteCoverage() {
        return true;
    }

    private void mockComponentForSchemes(List<String> schemes) {
        schemes.forEach(this::mockComponentForScheme);
    }

    private void mockComponentForScheme(String scheme) {
        if (!DIRECT_SCHEME.equals(scheme) && !context.getComponentNames().contains(scheme)) {
            log.debug("Mocking component: {}", scheme);
            context.addComponent(scheme, new MockComponent());
            return;
        }
        log.debug("Component already mocked: {}", scheme);
    }

    private void replaceFromEndpoint(String fromUri, AdviceWithRouteBuilder adviceBuilder) {
        adviceBuilder.replaceFromWith(fromUri);
        log.debug("Replacing from endpoint: {}.", fromUri);
    }

    private void replaceToEndpoints(Map<String, String> endpointMappings, AdviceWithRouteBuilder adviceBuilder) {
        endpointMappings.forEach((fromEndpoint, toEndpoint) -> {

            mockComponentForScheme(fromEndpoint.substring(0, fromEndpoint.indexOf(":")));

            adviceBuilder.interceptSendToEndpoint(fromEndpoint)
                    .skipSendToOriginalEndpoint()
                    .to(toEndpoint);
            log.debug("Replacing to endpoint {} with {}.", fromEndpoint, toEndpoint);
        });
    }
}
