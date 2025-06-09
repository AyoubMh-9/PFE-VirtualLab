// src/app/services/data.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private apiUrl = 'http://localhost:8080/api'; // Adapter selon votre configuration

  constructor(private http: HttpClient) { }

  // Récupérer des données protégées par authentification
  getData(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/products/getAll`);
  }

  // Récupérer les détails d'un élément spécifique
  getDataById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/products/${id}`);
  }

  // Créer une nouvelle entrée
  createData(data: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/products`, data);
  }

  // Mettre à jour une entrée existante
  updateData(id: number, data: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/products/${id}`, data);
  }

  // Supprimer une entrée
  deleteData(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/products/${id}`);
  }
}