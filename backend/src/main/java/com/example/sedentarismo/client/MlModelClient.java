package com.example.sedentarismo.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class MlModelClient {

    private final RestClient client;

    public MlModelClient(
            @Value("${model.service.base-url:http://localhost:8000}") String baseUrl,
            @Value("${model.service.timeout-ms:3000}") int timeoutMs
    ) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeoutMs);
        factory.setReadTimeout(timeoutMs);
        this.client = RestClient.builder().baseUrl(baseUrl).requestFactory(factory).build();
    }

    public PredictResponse predict(PredictRequest body) {
        return client.post()
                .uri("/predict")
                .body(body)
                .retrieve()
                .body(PredictResponse.class);
    }

    /** Payload no formato exato esperado pelo sidecar (mesmos nomes do dataset VIGITEL). */
    public record PredictRequest(
            int q6,
            Integer q7,
            Integer q8a,
            Integer q8b,
            @JsonProperty("ind_med_has") double indMedHas,
            @JsonProperty("ind_med_db") double indMedDb,
            @JsonProperty("ind_med_depr") double indMedDepr
    ) {}

    public record PredictResponse(boolean sedentario, double probabilidade) {}
}
