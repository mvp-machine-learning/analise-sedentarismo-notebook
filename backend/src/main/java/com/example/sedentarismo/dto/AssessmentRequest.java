package com.example.sedentarismo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Dados que o frontend envia. Espelham exatamente o que o modelo precisa
 * (apenas em formato amigável — a conversão para os códigos VIGITEL é feita no backend).
 */
public record AssessmentRequest(
        @NotNull @Min(12) @Max(120) Integer idade,
        @NotNull Sexo sexo,
        @NotNull @Min(1) @Max(7) Integer pesoCategoria,
        @NotNull @Min(1) @Max(8) Integer alturaCategoria,
        @NotNull Boolean temHipertensao,
        @NotNull Boolean temDiabetes,
        @NotNull Boolean temDepressao
) {
}
