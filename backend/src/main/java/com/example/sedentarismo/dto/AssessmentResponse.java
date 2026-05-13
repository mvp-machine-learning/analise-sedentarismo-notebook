package com.example.sedentarismo.dto;

import java.util.List;

public record AssessmentResponse(
        boolean sedentario,
        String classificacao,
        int scoreRisco,
        String mensagem,
        List<String> recomendacoes,
        ModelInfo modelo
) {
}
