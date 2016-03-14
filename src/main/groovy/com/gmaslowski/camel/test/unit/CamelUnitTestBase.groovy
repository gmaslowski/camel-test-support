package com.gmaslowski.camel.test.unit

import org.apache.camel.EndpointInject
import org.apache.camel.Produce
import org.apache.camel.ProducerTemplate
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.test.junit4.CamelTestSupport

abstract class CamelUnitTestBase extends CamelTestSupport {

    public static final String toEndpoint = "mock:result"
    public static final String fromEndpoint = "direct:start"

    @EndpointInject(uri = CamelUnitTestBase.toEndpoint)
    MockEndpoint resultEndpoint

    @Produce(uri = CamelUnitTestBase.fromEndpoint)
    ProducerTemplate template
}
