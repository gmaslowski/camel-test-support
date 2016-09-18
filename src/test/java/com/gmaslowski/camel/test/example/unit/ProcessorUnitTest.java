package com.gmaslowski.camel.test.example.unit;

import com.gmaslowski.camel.test.unit.CamelProcessorUnitTest;
import org.apache.camel.Processor;
import org.junit.Test;

public class ProcessorUnitTest extends CamelProcessorUnitTest {

    static class Input { String value; }
    static class Output { String value; @Override public boolean equals(Object obj) { return ((Output)obj).value.equals(this.value); }}

    @Override
    protected Processor processorUnderTest() {
        return exchange -> {
            Input input = exchange.getIn().getBody(Input.class);
            Output output = new Output();
            output.value = input.value;
            exchange.getIn().setBody(output);
        };
    }

    @Test
    public void shouldTestProcessor() throws InterruptedException {
        // given
        Input input = new Input();
        input.value = "message";

        Output expected = new Output();
        expected.value = "message";

        // expect
        resultEndpoint.expectedBodiesReceived(expected);

        // when
        template.sendBody(input);

        // then
        resultEndpoint.assertIsSatisfied();
    }
}
