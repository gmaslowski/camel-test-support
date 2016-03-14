package com.gmaslowski.camel.test.unit

import org.apache.camel.Expression
import org.apache.camel.builder.RouteBuilder

abstract class SplitterUnitTest extends CamelUnitTestBase {

    protected abstract Expression splitterUnderTest()

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(fromEndpointUri)
                        .split(splitterUnderTest())
                        .to(toEndpointUri)
            }
        }
    }
}

