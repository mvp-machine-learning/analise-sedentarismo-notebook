package com.example.sedentarismo.service;

import com.example.sedentarismo.client.MlModelClient;
import com.example.sedentarismo.dto.AssessmentRequest;
import com.example.sedentarismo.dto.AssessmentResponse;
import com.example.sedentarismo.dto.Sexo;
import org.springframework.stereotype.Service;

/**
 * Fluxo:
 *   1. recebe os dados do front em {@link AssessmentRequest};
 *   2. pré-processa (mapeia enums e booleanos para os códigos numéricos do VIGITEL);
 *   3. envia ao sidecar Python que carrega o joblib treinado no notebook;
 *   4. devolve a predição para o front.
 */
@Service
public class AssessmentService {

    private final MlModelClient mlClient;

    public AssessmentService(MlModelClient mlClient) {
        this.mlClient = mlClient;
    }

    public AssessmentResponse assess(AssessmentRequest request) {
        MlModelClient.PredictRequest body = preprocess(request);
        MlModelClient.PredictResponse prediction = mlClient.predict(body);

        return new AssessmentResponse(
                prediction.sedentario(),
                prediction.sedentario() ? "Sedentário" : "Não sedentário",
                prediction.probabilidade(),
                (int) Math.round(prediction.probabilidade() * 100)
        );
    }

    /** Converte os dados amigáveis do front para o esquema esperado pelo modelo. */
    private MlModelClient.PredictRequest preprocess(AssessmentRequest r) {
        return new MlModelClient.PredictRequest(
                r.idade(),
                sexoToCode(r.sexo()),
                r.pesoCategoria(),
                r.alturaCategoria(),
                boolToDouble(r.temHipertensao()),
                boolToDouble(r.temDiabetes()),
                boolToDouble(r.temDepressao())
        );
    }

    private Integer sexoToCode(Sexo sexo) {
        return switch (sexo) {
            case MASCULINO -> 1;
            case FEMININO -> 2;
            case OUTRO -> null; // imputer do pipeline trata o ausente
        };
    }

    private double boolToDouble(boolean value) {
        return value ? 1.0 : 0.0;
    }
}
