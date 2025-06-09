import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UploadedFileDTO } from '../models/UploadedFileDTO.model'; 

@Injectable({
  providedIn: 'root'
})
export class UploadedFileService {

  private apiUrl = 'http://localhost:8080/api/files';

  constructor(private http: HttpClient) {}

  getFilesByUser(userId: number): Observable<UploadedFileDTO[]> {
    return this.http.get<UploadedFileDTO[]>(`${this.apiUrl}/user/${userId}`);
  }

  getAllFiles(): Observable<UploadedFileDTO[]> {
    return this.http.get<UploadedFileDTO[]>(`${this.apiUrl}/all`);
  }

  downloadFile(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/download/${id}`, { responseType: 'blob' });
  }

  uploadFile(file: File, userId: number, projectId: number): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('userId', userId.toString());
    formData.append('projectId', projectId.toString());
    return this.http.post(`${this.apiUrl}/upload`, formData);
  }

  getFilesByProject(projectId: number): Observable<any> {
  return this.http.get(`${this.apiUrl}/project/${projectId}`);
}

getNumberOfFileByClient(): Observable<number>{
  return this.http.get<number>(`${this.apiUrl}/client/reports/count/all`);
}

getRapportsForProjects(projectId: number): Observable<UploadedFileDTO[]>{
  return this.http.get<UploadedFileDTO[]>(`${this.apiUrl}/client/project/${projectId}`);
}

exportProjectReports(projectId: number): Observable<Blob> {
    // responseType: 'blob' is crucial for downloading files
    return this.http.get(`${this.apiUrl}/products/${projectId}/export-reports`, { responseType: 'blob' });
  }






}
