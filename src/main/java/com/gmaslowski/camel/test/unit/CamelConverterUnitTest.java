package com.gmaslowski.camel.test.unit;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;

public abstract class CamelConverterUnitTest extends CamelUnitTestBase {

    protected abstract Class converterUnderTest(CamelContext context);

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(fromEndpointUri)
                        .convertBodyTo(converterUnderTest(context()))
                        .to(toEndpointUri);
            }
        };
    }

}
