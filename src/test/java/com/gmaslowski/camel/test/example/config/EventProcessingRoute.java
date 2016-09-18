package com.gmaslowski.camel.test.example.config;

import org.apache.camel.builder.RouteBuilder;

public class EventProcessingRoute extends RouteBuilder {

    public static final String PROCESS_EVENT_ROUTE = "processEvent";

    private final EventProcessingBean eventProcessingBean;

    public EventProcessingRoute(EventProcessingBean eventProcessingBean) {
        this.eventProcessingBean = eventProcessingBean;
    }

    @Override
    public void configure() throws Exception {
        from("activemq:queue:event?jmsMessageType=Object")
                .routeId(PROCESS_EVENT_ROUTE)
                .bean(eventProcessingBean)
                .to("mongodb:events");
    }
}
