package com.example.sedentarismo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TrainingInfo(
        String geradoEm,
        Dataset dataset,
        Split split,
        List<ModelMetrics> modelos,
        String modeloSelecionado,
        List<FeatureImportance> importancias,
        String arquivoModelo
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Dataset(
            String fonte,
            int linhasBrutas,
            int linhasUtilizadas,
            int colunas,
            List<Feature> features,
            TargetDistribution distribuicaoAlvo,
            String criterioRotulo
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Feature(String codigo, String descricao) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TargetDistribution(int sedentarios, int naoSedentarios, double proporcaoSedentarios) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Split(int treino, int teste, String estrategia) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ModelMetrics(
            String nome,
            double accuracy,
            double balancedAccuracy,
            String hiperparametros,
            boolean selecionado
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record FeatureImportance(String feature, String label, double importance) {}
}
