package com.gmaslowski.camel.test.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@Data
@Builder
@AllArgsConstructor
public class CamelRouteIntegrationTestConfiguration {

    private String routeId;
    @Singular
    private List<String> mockSchemes = emptyList();
    private String mockFromEndpointName;
    @Singular
    private Map<String, String> mockToEndpoints = emptyMap();
}
