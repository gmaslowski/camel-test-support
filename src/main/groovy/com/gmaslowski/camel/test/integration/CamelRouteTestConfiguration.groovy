package com.gmaslowski.camel.test.integration

import static java.util.Collections.emptyList
import static java.util.Collections.emptyMap

class CamelRouteTestConfiguration {

    String routeName
    List<String> mockSchemes = emptyList()
    String mockFromEndpointName
    Map<String, String> mockToEndpoints = emptyMap()
}
