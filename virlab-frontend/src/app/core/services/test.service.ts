// test.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TestService {
  private readonly apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getHello() {
    return this.http.get(`${this.apiUrl}/hello`, { 
      responseType: 'text',
      withCredentials: true // Important pour les sessions
    });
  }

  getAllTestsCount(): Observable<number> {
  return this.http.get<number>(`${this.apiUrl}/tests/count/all`);
}

getSuccessfulTestsCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/tests/count/successful`);
  }

}