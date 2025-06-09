import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root' // Important pour le mode standalone
})
export class ApiService {
  private apiUrl = '/api';

  constructor(private http: HttpClient) { }

  // Exemple de méthode GET
  getData() {
    return this.http.get(`${this.apiUrl}/hello`);
  }
}