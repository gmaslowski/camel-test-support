package com.gmaslowski.camel.test.unit

import org.apache.camel.Processor
import org.apache.camel.builder.RouteBuilder

public abstract class ProcessorUnitTest extends CamelUnitTestBase {

    protected abstract Processor processorUnderTest()

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(fromEndpointUri)
                        .process(processorUnderTest())
                        .to(toEndpointUri)
            }
        }
    }
}
