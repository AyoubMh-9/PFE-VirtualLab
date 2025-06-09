import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root' // Important pour l'injection de dépendances
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api'; // Adaptez l'URL

  constructor(private http: HttpClient) { }

  getData() {
    return this.http.get(`${this.apiUrl}/endpoint`, { responseType: 'text' });
  }
}