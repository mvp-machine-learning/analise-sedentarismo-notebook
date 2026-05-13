import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { AssessmentRequest, AssessmentResponse } from './assessment.models';

@Injectable({ providedIn: 'root' })
export class AssessmentService {
  constructor(private readonly http: HttpClient) {}

  assess(payload: AssessmentRequest): Observable<AssessmentResponse> {
    return this.http.post<AssessmentResponse>(`${environment.apiUrl}/assessments`, payload);
  }
}
