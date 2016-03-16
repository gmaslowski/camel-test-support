package com.gmaslowski.camel.test.unit;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;

abstract class CamelUnitTestBase extends CamelTestSupport {

    protected final String fromEndpointUri = "direct:start";
    protected final String toEndpointUri = "mock:result";

    @EndpointInject(uri = toEndpointUri)
    protected MockEndpoint resultEndpoint;

    @Produce(uri = fromEndpointUri)
    protected ProducerTemplate template;

}
