camel-test-support
==================

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.gmaslowski/camel-test-support/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.gmaslowski/camel-test-support)
[![Build Status](https://snap-ci.com/gmaslowski/camel-test-support/branch/master/build_image)](https://snap-ci.com/gmaslowski/camel-test-support/branch/master)

A library supporting testing Apache Camel based routes, processors, converters, splitters. It's main purpose is to create better-to-use and not-so-cluttered API on top of camel-test's API.

### Add to project

#### Gradle

```groovy
testCompile 'com.gmaslowski:camel-test-support:1.0'
```

#### Maven

```xml
<dependency>
    <groupId>com.gmaslowski</groupId>
    <artifactId>camel-test-support</artifactId>
    <version>1.0</version>
    <scope>test</scope>
</dependency>
```

#### sbt

```scala
libraryDependencies += "com.gmaslowski" % "camel-test-support" % "1.0" % "test"
```

### Examples

For working examples refer to ``src/test/java/``.

#### Processor test example

```java
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
```

#### Integration test example
```java
public class EventProcessingIntegrationTest extends CamelRouteIntegrationTestBase {

    @Override
    protected CamelRouteIntegrationTestConfiguration testConfiguration() {
        return CamelRouteIntegrationTestConfiguration.builder()
                .routeId(PROCESS_EVENT_ROUTE)
                .mockFromEndpointName("direct:start")
                .mockToEndpoint("mongodb:*", "mock:result")
                .build();
    }

    @EndpointInject(uri = "mock:result")
    private MockEndpoint resultEndpoint;

    @Test
    public void shouldStoreEventIntoMongoDB() throws Exception {
        // given
        Event event = new Event();
        event.created = new Date();

        // expect
        resultEndpoint.expectedMessageCount(1);

        // when
        template.sendBody("direct:start", event);

        // then
        resultEndpoint.assertIsSatisfied();
        assertNotNull(resultEndpoint.getExchanges().get(0).getIn().getBody(Event.class).processed);
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new EventProcessingRoute(new EventProcessingBean());
    }

}
```
