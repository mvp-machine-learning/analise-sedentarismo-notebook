package com.example.sedentarismo.service;

import com.example.sedentarismo.dto.TrainingInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/** Lê o JSON de métricas gerado pela célula 7 do notebook (apenas leitura). */
@Service
public class TrainingInfoService {

    private static final String RESOURCE = "training-metrics.json";

    private final ObjectMapper objectMapper;
    private TrainingInfo cached;

    public TrainingInfoService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void load() {
        ClassPathResource resource = new ClassPathResource(RESOURCE);
        if (!resource.exists()) {
            throw new IllegalStateException(
                    "Arquivo " + RESOURCE + " não encontrado. Execute a célula 7 do notebook."
            );
        }
        try (InputStream in = resource.getInputStream()) {
            this.cached = objectMapper.readValue(in, TrainingInfo.class);
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao carregar " + RESOURCE, e);
        }
    }

    public TrainingInfo get() {
        return cached;
    }
}
