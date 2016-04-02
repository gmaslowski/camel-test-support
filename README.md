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
