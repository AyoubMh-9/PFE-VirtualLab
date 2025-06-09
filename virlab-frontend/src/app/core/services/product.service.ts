import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Produit , ProductResponseDTO, Produitnew } from '../models/produit.model';
import { NewProject } from '../models/projectCreate.model';


@Injectable({
  providedIn: 'root'
})
export class ProduitService {
  private apiUrl = 'http://localhost:8080/api/products';

  constructor(private http: HttpClient) {}

  getProduitsDuClient(): Observable<Produitnew[]> {
    return this.http.get<Produitnew[]>(`${this.apiUrl}/getAll`);
  }

  createProject(newproject: NewProject): Observable<any>{
    return this.http.post(`${this.apiUrl}/create`, newproject);
  }

  // ProduitService
  updateProject(id: number, updatedProject: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/update/${id}`, updatedProject);
  }

  deleteProject(id: number){
    return this.http.delete(`${this.apiUrl}/delete/${id}`);
  }

  getProjectCountByTechnicianAcitf(technicianId: number): Observable<number>{
    return this.http.get<number>(`${this.apiUrl}/technician/${technicianId}/active-projects/count`);
  }

  getProjectCountByTechnicianNoActif(technicianId: number): Observable<number>{
    return this.http.get<number>(`${this.apiUrl}/technician/${technicianId}/no-active-projects/count`);
  }

  getProjectCountByTechnicianDone(technicianId: number): Observable<number>{
    return this.http.get<number>(`${this.apiUrl}/technician/${technicianId}/done-active-projects/count`);
  }

  getAssignedProjects(): Observable<Produit[]>{
    return this.http.get<Produit[]>(`${this.apiUrl}/technician/assigned-projects`);
  }

  getProjectForClient(): Observable<ProductResponseDTO[]>{
    return this.http.get<ProductResponseDTO[]>(`${this.apiUrl}/client/getAll`);
  }

  getProjectActifByClient(): Observable<number>{
    return this.http.get<number>(`${this.apiUrl}/client/active-projects/count`);
  }

  getAllTestsCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/client/tests/count/all`);
  }

  joinProjectForClient(accessCode: string): Observable<any>{
    return this.http.get<any>(`${this.apiUrl}/join/${accessCode}`);
  }

  /**
   * Calls the backend to join a product using an access code.
   * The access code is sent as a path variable.
   * Backend endpoint: POST /api/client/products/join/{accessCode}
   *
   * @param accessCode The unique access code for the product.
   * @returns An Observable of the backend response (e.g., success message).
   */
  joinProductByAccessCode(accessCode: string): Observable<string> { 
    return this.http.post(`${this.apiUrl}/join/${accessCode}`, {}, { responseType: 'text' });
  }


}