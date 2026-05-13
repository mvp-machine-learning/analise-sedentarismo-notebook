import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import { AssessmentRequest, AssessmentResponse, Sexo } from './assessment.models';
import { AssessmentService } from './assessment.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  protected loading = false;
  protected error = '';
  protected result?: AssessmentResponse;

  protected readonly form = this.fb.nonNullable.group({
    idade: [32, [Validators.required, Validators.min(12), Validators.max(120)]],
    sexo: ['FEMININO' as Sexo, [Validators.required]],
    minutosAtividadeSemanal: [90, [Validators.required, Validators.min(0), Validators.max(3000)]],
    horasSentadoDia: [8, [Validators.required, Validators.min(0), Validators.max(24)]],
    diasAtividadeSemana: [2, [Validators.required, Validators.min(0), Validators.max(7)]],
    autoavaliacaoSaude: [6, [Validators.required, Validators.min(0), Validators.max(10)]]
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly assessmentService: AssessmentService
  ) {}

  protected submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.error = '';
    this.result = undefined;

    this.assessmentService.assess(this.form.getRawValue() as AssessmentRequest)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (response) => this.result = response,
        error: () => this.error = 'Não foi possível conectar à API. Verifique se o backend Java está em execução na porta 8080.'
      });
  }

  protected fillHealthyExample(): void {
    this.form.patchValue({
      idade: 28,
      sexo: 'MASCULINO',
      minutosAtividadeSemanal: 210,
      horasSentadoDia: 4,
      diasAtividadeSemana: 5,
      autoavaliacaoSaude: 8
    });
  }
}
