package com.example.sedentarismo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AssessmentRequest(
        @NotNull @Min(12) @Max(120) Integer idade,
        @NotNull Sexo sexo,
        @NotNull @Min(0) @Max(3000) Integer minutosAtividadeSemanal,
        @NotNull @Min(0) @Max(24) Double horasSentadoDia,
        @NotNull @Min(0) @Max(7) Integer diasAtividadeSemana,
        @NotNull @Min(0) @Max(10) Integer autoavaliacaoSaude
) {
}
