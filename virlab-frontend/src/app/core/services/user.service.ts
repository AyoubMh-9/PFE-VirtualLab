import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';
import { NewUser } from '../models/userCreate.module';
import { UserUpdateDTO } from '../models/userUpdate.model';
import {jwtDecode} from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8080/api'; // Change selon ton backend

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/admin/all`);
  }

  createUser(newuser: NewUser): Observable<any> {
    return this.http.post(`${this.baseUrl}/user/register`, newuser); // par exemple: POST /api/users/create
  }
  /*
  updateUser(user: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/admin/updateUser/`, user);
  }
  */
  updateUser(userDto: UserUpdateDTO): Observable<any> {
    console.log('User DTO envoyé:', userDto);
  return this.http.put(`${this.baseUrl}/admin/updateUser`, userDto);
}
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/admin/users`);
  }

  getUserById(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/admin/find/${id}`);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/admin/deleteUser/${id}`);
  }

  getAllTechnicians(): Observable<any>{
    return this.http.get(`${this.baseUrl}/admin/getAllTechnicians`);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getCurrentUsername(): string | null {
    const token = this.getToken();
    if (!token) return null;

    const decodedToken: any = jwtDecode(token);
    return decodedToken.sub; // c'est bien le username (Ayoub Mouhib)
  }
  getUserIdByUsername(username: string): Observable<number> {
  return this.http.get<number>(`http://localhost:8080/api/id/${username}`);
}

  


  
}
