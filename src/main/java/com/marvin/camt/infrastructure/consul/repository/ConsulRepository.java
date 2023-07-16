package com.marvin.camt.infrastructure.consul.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marvin.camt.infrastructure.consul.model.ConsulKeyValueDTO;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class ConsulRepository {

    private static final TypeReference<List<ConsulKeyValueDTO>> TYPE_REFERENCE = new TypeReference<>() {
    };

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ConsulRepository(ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = objectMapper;
    }

    public List<ConsulKeyValueDTO> getPropertiesRecursively(String keyPrefix) throws Exception {

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://localhost:8500/v1/kv/" + keyPrefix + "?recurse=true"))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        return objectMapper.readValue(httpResponse.body(), TYPE_REFERENCE);
    }
}
