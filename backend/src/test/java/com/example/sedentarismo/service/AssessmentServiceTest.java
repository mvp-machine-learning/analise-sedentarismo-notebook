package com.example.sedentarismo.service;

import com.example.sedentarismo.client.MlModelClient;
import com.example.sedentarismo.dto.AssessmentRequest;
import com.example.sedentarismo.dto.AssessmentResponse;
import com.example.sedentarismo.dto.Sexo;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AssessmentServiceTest {

    @Test
    void shouldPreprocessAndForwardToMlServiceThenReturnPrediction() {
        MlModelClient mlClient = mock(MlModelClient.class);
        when(mlClient.predict(org.mockito.ArgumentMatchers.any()))
                .thenReturn(new MlModelClient.PredictResponse(true, 0.83));

        AssessmentService service = new AssessmentService(mlClient);
        AssessmentRequest request = new AssessmentRequest(35, Sexo.FEMININO, 5, 3, true, false, true);

        AssessmentResponse response = service.assess(request);

        ArgumentCaptor<MlModelClient.PredictRequest> captor =
                ArgumentCaptor.forClass(MlModelClient.PredictRequest.class);
        verify(mlClient).predict(captor.capture());
        MlModelClient.PredictRequest forwarded = captor.getValue();

        // pré-processamento: enum/boolean -> códigos do VIGITEL
        assertThat(forwarded.q6()).isEqualTo(35);
        assertThat(forwarded.q7()).isEqualTo(2);     // FEMININO
        assertThat(forwarded.q8a()).isEqualTo(5);
        assertThat(forwarded.q8b()).isEqualTo(3);
        assertThat(forwarded.indMedHas()).isEqualTo(1.0);
        assertThat(forwarded.indMedDb()).isEqualTo(0.0);
        assertThat(forwarded.indMedDepr()).isEqualTo(1.0);

        // resposta combinada
        assertThat(response.sedentario()).isTrue();
        assertThat(response.classificacao()).isEqualTo("Sedentário");
        assertThat(response.probabilidade()).isEqualTo(0.83);
        assertThat(response.scoreRisco()).isEqualTo(83);
    }

    @Test
    void shouldMapMasculinoToCode1() {
        MlModelClient mlClient = mock(MlModelClient.class);
        when(mlClient.predict(org.mockito.ArgumentMatchers.any()))
                .thenReturn(new MlModelClient.PredictResponse(false, 0.2));

        AssessmentService service = new AssessmentService(mlClient);
        service.assess(new AssessmentRequest(28, Sexo.MASCULINO, 5, 4, false, false, false));

        ArgumentCaptor<MlModelClient.PredictRequest> captor =
                ArgumentCaptor.forClass(MlModelClient.PredictRequest.class);
        verify(mlClient).predict(captor.capture());
        assertThat(captor.getValue().q7()).isEqualTo(1);
    }
}
