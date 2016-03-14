package com.gmaslowski.camel.test.integration

import org.apache.camel.Exchange
import org.apache.camel.builder.AdviceWithRouteBuilder
import org.apache.camel.component.mock.MockComponent
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.test.junit4.CamelTestSupport
import org.junit.After
import org.junit.Before

import static org.mockito.MockitoAnnotations.initMocks

abstract class CamelRouteIntegrationTestBase extends CamelTestSupport {

    public CamelRouteIntegrationTestBase() {
        super()
        initMocks(this)
    }

    protected abstract CamelRouteTestConfiguration testConfiguration()

    @Before
    public void startCamel() throws Exception {

        context.getRouteDefinition(testConfiguration().getRouteName())
                .adviceWith(
                context,
                {
                    replaceFromEndpoint(testConfiguration().mockFromEndpointName, this as AdviceWithRouteBuilder)
                    replaceToEndpoints(testConfiguration().getMockToEndpoints(), this as AdviceWithRouteBuilder)
                })

        mockComponentForSchemes(testConfiguration().getMockSchemes())

        context.start()
        template = context.createProducerTemplate()
    }

    @After
    public void stopCamel() {
        context.stop()
    }

    protected static <T> void givenMockReturns(MockEndpoint endpoint, T value) {
        endpoint.returnReplyBody({ Exchange exchange, Class type ->
            return value;
        });
    }

    @Override
    public boolean isUseAdviceWith() {
        return true
    }

    @Override
    public boolean isDumpRouteCoverage() {
        return true
    }

    private void mockComponentForSchemes(List<String> schemes) {
        schemes.each { scheme -> context.addComponent(scheme, new MockComponent()) }
    }

    private static replaceFromEndpoint(String fromUri, AdviceWithRouteBuilder adviceBuilder) {
        adviceBuilder.replaceFromWith(fromUri)
    }

    private static replaceToEndpoints(Map<String, String> endpointMappings, AdviceWithRouteBuilder adviceBuilder) {
        endpointMappings.each {
            fromEndpoint, toEndpoint ->
                adviceBuilder.interceptSendToEndpoint(fromEndpoint)
                        .skipSendToOriginalEndpoint()
                        .to(toEndpoint)
        }
    }

}
