camel-test-support
==================

A library supporting testing Apache Camel based routes, processors, converters, splitters.

### Examples

#### Processor test example
```
public class ExampleProcessorTest extends CamelProcessorUnitTest {

    @Override
    protected Processor processorUnderTest() {
        return ...; // processor instance
    }

    @Test
    public void shouldMapToProperType() throws InterruptedException {
        // expect
        resultEndpoint.expectedMessagesMatches(...);

        // when
        template.sendBody(processorInput);

        // then
        resultEndpoint.assertIsSatisfied();
    }
}
```
