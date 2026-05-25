import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, shareReplay } from 'rxjs';
import { environment } from '../environments/environment';
import { AssessmentRequest, AssessmentResponse, TrainingInfo } from './assessment.models';

@Injectable({ providedIn: 'root' })
export class AssessmentService {
  private trainingInfo$?: Observable<TrainingInfo>;

  constructor(private readonly http: HttpClient) {}

  assess(payload: AssessmentRequest): Observable<AssessmentResponse> {
    return this.http.post<AssessmentResponse>(`${environment.apiUrl}/assessments`, payload);
  }

  getTrainingInfo(): Observable<TrainingInfo> {
    if (!this.trainingInfo$) {
      this.trainingInfo$ = this.http
        .get<TrainingInfo>(`${environment.apiUrl}/training-info`)
        .pipe(shareReplay(1));
    }
    return this.trainingInfo$;
  }
}
