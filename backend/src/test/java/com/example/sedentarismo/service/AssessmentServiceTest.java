package com.example.sedentarismo.service;

import com.example.sedentarismo.dto.AssessmentRequest;
import com.example.sedentarismo.dto.AssessmentResponse;
import com.example.sedentarismo.dto.Sexo;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AssessmentServiceTest {

    private final AssessmentService service = new AssessmentService();

    @Test
    void shouldClassifyAsSedentaryWhenActivityIsBelow150Minutes() {
        AssessmentRequest request = new AssessmentRequest(35, Sexo.FEMININO, 60, 9.0, 1, 5);

        AssessmentResponse response = service.assess(request);

        assertThat(response.sedentario()).isTrue();
        assertThat(response.classificacao()).isEqualTo("Sedentário");
        assertThat(response.recomendacoes()).isNotEmpty();
    }

    @Test
    void shouldClassifyAsNonSedentaryWhenActivityRoutineIsHealthy() {
        AssessmentRequest request = new AssessmentRequest(29, Sexo.MASCULINO, 180, 4.0, 4, 8);

        AssessmentResponse response = service.assess(request);

        assertThat(response.sedentario()).isFalse();
        assertThat(response.classificacao()).isEqualTo("Não sedentário");
        assertThat(response.scoreRisco()).isLessThan(65);
    }
}
