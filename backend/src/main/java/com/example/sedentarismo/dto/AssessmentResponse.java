package com.example.sedentarismo.dto;

public record AssessmentResponse(
        boolean sedentario,
        String classificacao,
        double probabilidade,
        int scoreRisco
) {
}
