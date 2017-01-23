package com.gmaslowski.camel.test.example.integration.direct;

import com.gmaslowski.camel.test.integration.CamelRouteIntegrationTestBase;
import com.gmaslowski.camel.test.integration.CamelRouteIntegrationTestConfiguration;
import org.apache.camel.EndpointInject;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

public class DirectEndpointIntegrationTest extends CamelRouteIntegrationTestBase {

    @Override
    protected CamelRouteIntegrationTestConfiguration testConfiguration() {
        return CamelRouteIntegrationTestConfiguration.builder()
                .routeId(DirectProcessingRoute.DIRECT_PROCESSING_ROUTE)
                .mockFromEndpointName("direct:start")
                .mockToEndpoint("direct:output", "mock:result")
                .build();
    }

    @EndpointInject(uri = "mock:result")
    private MockEndpoint resultEndpoint;

    @Test
    public void shouldProperlyMockDirectEndpoint() throws Exception {
        // given
        String data = "data";

        // expect
        resultEndpoint.expectedMessageCount(1);

        // when
        template.sendBody("direct:start", data);

        // then
        resultEndpoint.assertIsSatisfied();
        assertEquals(resultEndpoint.getExchanges().get(0).getIn().getBody(String.class), data);
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new DirectProcessingRoute();
    }

}
