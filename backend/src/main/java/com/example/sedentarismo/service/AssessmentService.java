package com.example.sedentarismo.service;

import com.example.sedentarismo.dto.AssessmentRequest;
import com.example.sedentarismo.dto.AssessmentResponse;
import com.example.sedentarismo.dto.ModelInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssessmentService {

    private static final int MINUTOS_ATIVIDADE_RECOMENDADOS = 150;

    public AssessmentResponse assess(AssessmentRequest request) {
        int score = calculateRiskScore(request);
        boolean sedentary = request.minutosAtividadeSemanal() < MINUTOS_ATIVIDADE_RECOMENDADOS || score >= 65;
        String classification = sedentary ? "Sedentário" : "Não sedentário";
        String message = sedentary
                ? "Seu perfil indica risco de sedentarismo. Pequenas metas semanais podem melhorar esse resultado."
                : "Seu perfil indica boa rotina de atividade física. Continue monitorando seus hábitos.";

        return new AssessmentResponse(
                sedentary,
                classification,
                score,
                message,
                buildRecommendations(request, sedentary),
                new ModelInfo(
                        "Notebook de Classificação de Sedentarismo com dados VIGITEL 2023",
                        "Indivíduos com menos de 150 minutos de atividade física semanal são classificados como sedentários.",
                        "Esta API usa uma regra explicável inspirada no notebook; o modelo Random Forest pode ser serializado e plugado em uma evolução futura."
                )
        );
    }

    private int calculateRiskScore(AssessmentRequest request) {
        int score = 0;

        score += Math.max(0, MINUTOS_ATIVIDADE_RECOMENDADOS - request.minutosAtividadeSemanal()) * 45 / MINUTOS_ATIVIDADE_RECOMENDADOS;
        score += request.horasSentadoDia() >= 8 ? 20 : Math.round(request.horasSentadoDia().floatValue() * 2);
        score += request.diasAtividadeSemana() <= 1 ? 15 : Math.max(0, 7 - request.diasAtividadeSemana());
        score += request.autoavaliacaoSaude() <= 4 ? 15 : Math.max(0, 8 - request.autoavaliacaoSaude());
        score += request.idade() >= 60 ? 5 : 0;

        return Math.min(100, score);
    }

    private List<String> buildRecommendations(AssessmentRequest request, boolean sedentary) {
        List<String> recommendations = new ArrayList<>();

        if (request.minutosAtividadeSemanal() < MINUTOS_ATIVIDADE_RECOMENDADOS) {
            int faltam = MINUTOS_ATIVIDADE_RECOMENDADOS - request.minutosAtividadeSemanal();
            recommendations.add("Acrescente cerca de " + faltam + " minutos de atividade física moderada na semana para atingir 150 minutos.");
        }
        if (request.horasSentadoDia() >= 6) {
            recommendations.add("Faça pausas ativas de 3 a 5 minutos a cada hora sentado.");
        }
        if (request.diasAtividadeSemana() < 3) {
            recommendations.add("Distribua a atividade em pelo menos 3 dias da semana para criar consistência.");
        }
        if (request.autoavaliacaoSaude() <= 5) {
            recommendations.add("Considere conversar com um profissional de saúde antes de aumentar a intensidade dos exercícios.");
        }
        if (!sedentary) {
            recommendations.add("Mantenha sua rotina e acompanhe seus indicadores semanalmente.");
        }

        return recommendations;
    }
}
