import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import { AssessmentRequest, AssessmentResponse, Sexo, TrainingInfo } from './assessment.models';
import { AssessmentService } from './assessment.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  protected loading = false;
  protected error = '';
  protected result?: AssessmentResponse;

  protected training?: TrainingInfo;
  protected trainingError = '';
  protected maxImportance = 1;

  protected readonly form;

  constructor(
    private readonly fb: FormBuilder,
    private readonly assessmentService: AssessmentService
  ) {
    this.form = this.fb.nonNullable.group({
      idade: [35, [Validators.required, Validators.min(12), Validators.max(120)]],
      sexo: ['FEMININO' as Sexo, [Validators.required]],
      pesoCategoria: [5, [Validators.required, Validators.min(1), Validators.max(7)]],
      alturaCategoria: [3, [Validators.required, Validators.min(1), Validators.max(8)]],
      temHipertensao: [false, [Validators.required]],
      temDiabetes: [false, [Validators.required]],
      temDepressao: [false, [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.assessmentService.getTrainingInfo().subscribe({
      next: (info) => {
        this.training = info;
        this.maxImportance = Math.max(...info.importancias.map((i) => i.importance), 0.0001);
      },
      error: () => {
        this.trainingError = 'Não foi possível carregar as métricas de treino do backend.';
      }
    });
  }

  protected submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.error = '';
    this.result = undefined;

    this.assessmentService.assess(this.form.getRawValue() as AssessmentRequest)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (response) => (this.result = response),
        error: (err) => {
          this.error = err?.error?.detail
            ? `Falha do modelo: ${err.error.detail}`
            : 'Não foi possível obter a predição. Verifique se o backend e o sidecar Python estão ativos.';
        }
      });
  }

  protected fillExample(): void {
    this.form.patchValue({
      idade: 28,
      sexo: 'MASCULINO',
      pesoCategoria: 4,
      alturaCategoria: 5,
      temHipertensao: false,
      temDiabetes: false,
      temDepressao: false
    });
  }

  protected importanceWidth(value: number): string {
    return `${Math.round((value / this.maxImportance) * 100)}%`;
  }

  protected formatPercent(value: number, fractionDigits = 1): string {
    return `${(value * 100).toFixed(fractionDigits)}%`;
  }
}
