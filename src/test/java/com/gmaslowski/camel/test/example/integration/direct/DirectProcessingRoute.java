
package com.gmaslowski.camel.test.example.integration.direct;

import org.apache.camel.builder.RouteBuilder;

public class DirectProcessingRoute extends RouteBuilder {

    public static final String DIRECT_PROCESSING_ROUTE = "processEvent";

    @Override
    public void configure() throws Exception {
        from("direct:input")
                .routeId(DIRECT_PROCESSING_ROUTE)
                .to("direct:output");
    }
}
