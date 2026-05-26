from __future__ import annotations

from pathlib import Path
from typing import Optional

import joblib
import pandas as pd
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field

MODEL_PATH = Path(__file__).resolve().parent / "modelo_sedentarismo_random_forest.joblib"
FEATURES = ["q6", "q7", "q8a", "q8b", "ind_med_has", "ind_med_db", "ind_med_depr"]
CATEGORICAL = ["q7", "q8a", "q8b"]

app = FastAPI(title="Sedentarismo ML Service", version="1.0.0")

_model = None


def _load_model():
    """Carrega o pipeline preprocessor+RandomForest exportado pelo notebook."""
    global _model
    if _model is None and MODEL_PATH.exists():
        _model = joblib.load(MODEL_PATH)
    return _model


@app.on_event("startup")
def _startup() -> None:
    _load_model()


class PredictRequest(BaseModel):
    q6: int = Field(..., ge=12, le=120, description="Idade")
    q7: Optional[int] = Field(None, description="Sexo (1=masc, 2=fem)")
    q8a: Optional[int] = Field(None, description="Faixa de peso (código VIGITEL)")
    q8b: Optional[int] = Field(None, description="Faixa de altura (código VIGITEL)")
    ind_med_has: Optional[float] = Field(None)
    ind_med_db: Optional[float] = Field(None)
    ind_med_depr: Optional[float] = Field(None)


class PredictResponse(BaseModel):
    sedentario: bool
    probabilidade: float


@app.get("/health")
def health() -> dict:
    return {"status": "UP", "modelLoaded": _load_model() is not None}


@app.post("/predict", response_model=PredictResponse)
def predict(body: PredictRequest) -> PredictResponse:
    model = _load_model()
    if model is None:
        raise HTTPException(
            status_code=503,
            detail=(
                f"Joblib não encontrado em {MODEL_PATH.name}. "
                "Rode a célula 6 do notebook para gerá-lo."
            ),
        )

    row = {f: getattr(body, f) for f in FEATURES}
    df = pd.DataFrame([row])
    for col in CATEGORICAL:
        df[col] = df[col].astype("category")

    prob = float(model.predict_proba(df)[0][1])
    return PredictResponse(sedentario=prob >= 0.5, probabilidade=round(prob, 4))
