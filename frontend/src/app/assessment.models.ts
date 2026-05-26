export type Sexo = 'FEMININO' | 'MASCULINO' | 'OUTRO';

export interface AssessmentRequest {
  idade: number;
  sexo: Sexo;
  pesoCategoria: number;
  alturaCategoria: number;
  temHipertensao: boolean;
  temDiabetes: boolean;
  temDepressao: boolean;
}

export interface AssessmentResponse {
  sedentario: boolean;
  classificacao: string;
  probabilidade: number;
  scoreRisco: number;
}

export interface TrainingInfo {
  geradoEm: string;
  dataset: {
    fonte: string;
    linhasBrutas: number;
    linhasUtilizadas: number;
    colunas: number;
    features: { codigo: string; descricao: string }[];
    distribuicaoAlvo: {
      sedentarios: number;
      naoSedentarios: number;
      proporcaoSedentarios: number;
    };
    criterioRotulo: string;
  };
  split: { treino: number; teste: number; estrategia: string };
  modelos: {
    nome: string;
    accuracy: number;
    balancedAccuracy: number;
    hiperparametros: string;
    selecionado: boolean;
  }[];
  modeloSelecionado: string;
  importancias: { feature: string; label: string; importance: number }[];
  arquivoModelo: string;
}
