# Sidecar de Inferência (Python / FastAPI)

Carrega o `modelo_sedentarismo_random_forest.joblib` exportado pelo notebook
(célula 6) e expõe um endpoint HTTP de predição. **Não treina nada** — só serve.

## Fluxo

```
Notebook (treina) ──► modelo_sedentarismo_random_forest.joblib
                                  │
                                  ▼
        Sidecar Python (este projeto) ◄── POST /predict ── Backend Java ◄── Frontend
                                  │                                            │
                                  └────► probabilidade ────────────────────────┘
```

O backend Java é responsável pelo pré-processamento amigável (mapeia `Sexo.FEMININO → 2`,
`true → 1.0` etc.) antes de enviar o payload com os códigos VIGITEL para este sidecar.

## Setup

```powershell
python -m venv .venv
.\.venv\Scripts\activate
pip install -r requirements.txt
```

## Gerar o joblib

Rode o notebook `notsedentarismo.ipynb` até a célula 6 (a que faz `joblib.dump(...)`).
Ela salva o pipeline aqui em `model_service/modelo_sedentarismo_random_forest.joblib`.

## Subir o sidecar

```powershell
# a partir da raiz do projeto
uvicorn model_service.app:app --host 0.0.0.0 --port 8000
```

## Endpoints

| Método | Path     | Descrição |
| ------ | -------- | --------- |
| GET    | /health  | `{status, modelLoaded}` |
| POST   | /predict | recebe features VIGITEL e devolve `{sedentario, probabilidade}` |

Exemplo:

```bash
curl -X POST http://localhost:8000/predict \
  -H "Content-Type: application/json" \
  -d '{"q6":35,"q7":2,"q8a":5,"q8b":3,"ind_med_has":0,"ind_med_db":0,"ind_med_depr":0}'
```
