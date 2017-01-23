package com.gmaslowski.camel.test.example.integration.event;

import org.apache.camel.builder.RouteBuilder;

public class EventProcessingComplexRoute extends RouteBuilder {
    public static final String PROCESS_EVENT_ROUTE = "processEvent";

    private final EventProcessingBean eventProcessingBean;

    public EventProcessingComplexRoute(EventProcessingBean eventProcessingBean) {
        this.eventProcessingBean = eventProcessingBean;
    }

    @Override
    public void configure() throws Exception {
        from("activemq:queue:event?jmsMessageType=Object")
                .routeId(PROCESS_EVENT_ROUTE)
                .bean(eventProcessingBean)
                .to("mongodb:events")
                .to("mongodb:eventsArchive");
    }

}
