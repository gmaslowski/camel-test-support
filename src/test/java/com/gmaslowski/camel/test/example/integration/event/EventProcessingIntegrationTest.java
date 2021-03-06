package com.gmaslowski.camel.test.example.integration.event;

import com.gmaslowski.camel.test.integration.CamelRouteIntegrationTestBase;
import com.gmaslowski.camel.test.integration.CamelRouteIntegrationTestConfiguration;
import org.apache.camel.EndpointInject;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

import java.util.Date;

public class EventProcessingIntegrationTest extends CamelRouteIntegrationTestBase {

    @Override
    protected CamelRouteIntegrationTestConfiguration testConfiguration() {
        return CamelRouteIntegrationTestConfiguration.builder()
                .routeId(EventProcessingRoute.PROCESS_EVENT_ROUTE)
                .mockFromEndpointName("direct:start")
                .mockToEndpoint("mongodb:*", "mock:result")
                .build();
    }

    @EndpointInject(uri = "mock:result")
    private MockEndpoint resultEndpoint;

    @Test
    public void shouldStoreEventIntoMongoDB() throws Exception {
        // given
        Event event = new Event();
        event.created = new Date();

        // expect
        resultEndpoint.expectedMessageCount(1);

        // when
        template.sendBody("direct:start", event);

        // then
        resultEndpoint.assertIsSatisfied();
        assertNotNull(resultEndpoint.getExchanges().get(0).getIn().getBody(Event.class).processed);
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new EventProcessingRoute(new EventProcessingBean());
    }

}
