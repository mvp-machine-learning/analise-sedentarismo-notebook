# Aplicação Web para Análise de Sedentarismo

Este documento descreve a aplicação web adicionada ao projeto para transformar o notebook de análise de sedentarismo em um MVP com backend Java e frontend Angular.

## Visão geral

A aplicação possui duas camadas:

- **Backend Java/Spring Boot** em `backend/`, responsável por expor a API REST de classificação.
- **Frontend Angular** em `frontend/`, responsável por coletar os dados do usuário e apresentar a classificação, o score de risco e recomendações.

A regra inicial do MVP segue o critério explicável usado no notebook: indivíduos com menos de **150 minutos de atividade física semanal** são classificados com risco de sedentarismo. O backend também calcula um score complementar usando horas sentado por dia, dias ativos na semana, autoavaliação de saúde e idade.

## Estrutura adicionada

```text
backend/
├── pom.xml
├── src/main/java/com/example/sedentarismo/
│   ├── SedentarismoApplication.java
│   ├── controller/AssessmentController.java
│   ├── dto/
│   └── service/AssessmentService.java
└── src/test/java/com/example/sedentarismo/service/AssessmentServiceTest.java

frontend/
├── angular.json
├── package.json
├── tsconfig.json
└── src/
    ├── app/
    ├── environments/environment.ts
    ├── index.html
    ├── main.ts
    └── styles.css
```

## Backend

### Endpoints

- `GET /api/health`: verifica se a API está no ar.
- `POST /api/assessments`: recebe os dados do usuário e retorna a avaliação.

### Exemplo de payload

```json
{
  "idade": 35,
  "sexo": "FEMININO",
  "minutosAtividadeSemanal": 90,
  "horasSentadoDia": 8,
  "diasAtividadeSemana": 2,
  "autoavaliacaoSaude": 6
}
```

### Executar

```bash
cd backend
mvn spring-boot:run
```

A API será iniciada em `http://localhost:8080`.

## Frontend

### Recursos da tela

- Seção inicial com contexto do ODS 3 e chamada para avaliação.
- Formulário reativo com validação de idade, sexo, minutos ativos, horas sentado, dias ativos e autoavaliação de saúde.
- Integração com `POST /api/assessments`.
- Card de resultado com classificação, score, mensagem, recomendações e explicação do critério.
- Layout responsivo para desktop e mobile.

### Executar

```bash
cd frontend
npm install
npm start
```

A aplicação será iniciada em `http://localhost:4200`.

## Evoluções recomendadas

1. Exportar o modelo Random Forest treinado no notebook para um formato interoperável, como ONNX, PMML ou outro artefato versionado.
2. Substituir a regra explicável inicial por inferência real do modelo treinado.
3. Adicionar persistência para histórico de avaliações.
4. Implementar autenticação e consentimento caso sejam armazenados dados pessoais ou sensíveis.
