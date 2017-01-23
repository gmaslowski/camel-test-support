package com.gmaslowski.camel.test.example.integration.event;

import java.util.Date;

public class EventProcessingBean {

    public Event process(Event event) {
        event.processed = new Date();
        return event;
    }
}
