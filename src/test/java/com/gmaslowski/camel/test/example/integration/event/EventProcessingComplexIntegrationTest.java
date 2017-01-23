package com.gmaslowski.camel.test.example.integration.event;

import com.gmaslowski.camel.test.integration.CamelRouteIntegrationTestBase;
import com.gmaslowski.camel.test.integration.CamelRouteIntegrationTestConfiguration;
import org.apache.camel.EndpointInject;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

import java.util.Date;

public class EventProcessingComplexIntegrationTest extends CamelRouteIntegrationTestBase {
    @Override
    protected CamelRouteIntegrationTestConfiguration testConfiguration() {
        return CamelRouteIntegrationTestConfiguration.builder()
                .routeId(EventProcessingRoute.PROCESS_EVENT_ROUTE)
                .mockFromEndpointName("direct:start")
                .mockToEndpoint("mongodb:events", "mock:events")
                .mockToEndpoint("mongodb:eventsArchive", "mock:eventsArchive")
                .build();
    }

    @EndpointInject(uri = "mock:events")
    private MockEndpoint mongoEventsEndpoint;

    @EndpointInject(uri = "mock:eventsArchive")
    private MockEndpoint mongoEventsArchiveEndpoint;

    @Test
    public void shouldStoreEventAndArchiveIntoMongoDB() throws Exception {
        // given
        Event event = new Event();
        event.created = new Date();

        // expect
        mongoEventsEndpoint.expectedMessageCount(1);
        mongoEventsArchiveEndpoint.expectedMessageCount(1);

        // when
        template.sendBody("direct:start", event);

        // then
        mongoEventsEndpoint.assertIsSatisfied();
        mongoEventsArchiveEndpoint.assertIsSatisfied();
        assertNotNull(mongoEventsEndpoint.getExchanges().get(0).getIn().getBody(Event.class).processed);
        assertNotNull(mongoEventsArchiveEndpoint.getExchanges().get(0).getIn().getBody(Event.class).processed);
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new EventProcessingComplexRoute(new EventProcessingBean());
    }

}
