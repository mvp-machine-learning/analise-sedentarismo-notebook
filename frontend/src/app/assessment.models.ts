export type Sexo = 'FEMININO' | 'MASCULINO' | 'OUTRO';

export interface AssessmentRequest {
  idade: number;
  sexo: Sexo;
  minutosAtividadeSemanal: number;
  horasSentadoDia: number;
  diasAtividadeSemana: number;
  autoavaliacaoSaude: number;
}

export interface AssessmentResponse {
  sedentario: boolean;
  classificacao: string;
  scoreRisco: number;
  mensagem: string;
  recomendacoes: string[];
  modelo: {
    origem: string;
    criterioPrincipal: string;
    observacao: string;
  };
}
