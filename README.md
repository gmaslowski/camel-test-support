camel-test-support
==================

A library supporting testing Apache Camel based routes, processors, converters, splitters.

### Add to project

#### Gradle

```
testCompile 'com.gmaslowski:camel-test-support:0.0.4'
```

#### Maven

```
<dependency>
    <groupId>com.gmaslowski</groupId>
    <artifactId>camel-test-support</artifactId>
    <version>0.0.4</version>
    <scope>test</scope>
</dependency>
```

#### sbt

```
libraryDependencies += "com.gmaslowski" % "camel-test-support" % "0.0.4" % "test"
```

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

#### Integration test example
```
public class EventProcessingIntegrationTest extends CamelRouteIntegrationTestBase {

    @Override
    protected CamelRouteTestConfiguration testConfiguration() {
        return CamelRouteTestConfiguration.builder()
                .routeName(PROCESS_EVENT_ROUTE)
                .mockScheme("mongodb")
                .mockFromEndpointName("direct:start")
                .mockToEndpoint("mongodb*", "mock:result")
                .build();
    }

    @EndpointInject(uri = "mock:result")
    private MockEndpoint resultEndpoint;

    @Test
    public void shouldStoreEventIntoMongoDB() throws Exception {
        // given
        Event event = new Event();
        event.setCreated(new Date());

        // expect
        resultEndpoint.expectedMessageCount(1);

        // when
        template.sendBody("direct:start", event);

        // then
        resultEndpoint.assertIsSatisfied();
        Assertions.assertThat(resultEndpoint.getExchanges().get(0).getIn().getBody(Event.class).getProcessed()).isNotNull();
    }


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new EventProcessingRoute(new EventProcessingBean());
    }

}
```
