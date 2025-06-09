import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';
import { jwtDecode } from 'jwt-decode';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api'; // Ajustez selon votre API
  private currentUserSubject = new BehaviorSubject<any>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  private tokenExpirationTimer: any;
  private isBrowser: boolean;
  private currentUser: User | null = null;

  constructor(
    private http: HttpClient, 
    private router: Router,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
    if (this.isBrowser) {
      this.loadStoredUser();
    }
  }
  /*
  login(credentials: { username: string, password: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials);
  }
    */
   login(credentials: { username: string, password: string }): Observable<any> {
  return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
    tap(response => {
      // Stocker le token
      localStorage.setItem('authToken', response.token);

      // Stocker les infos de l'utilisateur
      localStorage.setItem('currentUser', JSON.stringify(response.user));
    })
  );
}


  


  // Nouvelle méthode isAuthenticated pour remplacer isLoggedIn
  /*
  isAuthenticated(): boolean {
  const token = this.getToken(); // ou localStorage.getItem('token');
  return !!token && this.isTokenValid();
}
  */
 // Ajoutez ces logs dans votre auth.service.ts

isAuthenticated(): boolean {
  console.log('🔍 AuthService.isAuthenticated() appelé');

  if (typeof window === 'undefined' || !window.localStorage) {
    // Nous sommes côté serveur, donc on retourne false
    console.log('🌐 Exécution côté serveur : pas de localStorage disponible');
    return false;
  }

  const token = localStorage.getItem('token');
  console.log('Token présent:', !!token);
  console.log('Token length:', token?.length);

  if (!token) {
    console.log('❌ Pas de token - non authentifié');
    return false;
  }

  try {
    // Décoder le token
    const parts = token.split('.');
    console.log('Token parts:', parts.length);

    if (parts.length !== 3) {
      console.log('❌ Token mal formé');
      return false;
    }

    const payload = JSON.parse(atob(parts[1]));
    console.log('Token payload:', payload);

    // Vérifier expiration
    const now = Math.floor(Date.now() / 1000);
    console.log('Token expires at:', new Date(payload.exp * 1000));
    console.log('Current time:', new Date());
    console.log('Token expiré?', payload.exp < now);

    if (payload.exp && payload.exp < now) {
      console.log('❌ Token expiré - suppression');
      localStorage.removeItem('token');
      return false;
    }

    console.log('✅ Token valide');
    return true;

  } catch (error) {
    console.error('❌ Erreur parsing token:', error);
    localStorage.removeItem('token');
    return false;
  }
}




  // Méthode existante isLoggedIn
  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(redirect = true): void {
  if (this.isBrowser) {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('expirationDate');
  }

  this.currentUserSubject.next(null);

  if (this.tokenExpirationTimer) {
    clearTimeout(this.tokenExpirationTimer);
  }
  this.tokenExpirationTimer = null;

  if (redirect) {
    this.router.navigate(['/login'], { replaceUrl: true });
  }
}


  getToken(): string | null {
    if (!this.isBrowser) {
      return null;
    }
    return localStorage.getItem('token');
  }

  public handleAuthentication(token: string, username: string, role: string): void {
    try {
      // Décoder le token (attention : partie 1 = header, 2 = payload, 3 = signature)
      const payload = JSON.parse(atob(token.split('.')[1]));

      // Vérifier que exp existe et est un nombre
      if (!payload.exp || isNaN(payload.exp)) {
        console.error('Token expiration date is invalid:', payload.exp);
        return;
      }

      const expirationDate = new Date(payload.exp * 1000); // conversion secondes -> ms
      
      if (this.isBrowser) {
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify({ username, role }));
        localStorage.setItem('expirationDate', expirationDate.toISOString());
      }
      
      // Mettre à jour le BehaviorSubject
      this.currentUserSubject.next({ username, role, expirationDate });
      
      // Configurer la déconnexion automatique
      const expirationDuration = expirationDate.getTime() - new Date().getTime();
      this.autoLogout(expirationDuration);
    } catch (error) {
      console.error('Erreur lors du décodage du token :', error);
    }
  }

  private loadStoredUser() {
    if (!this.isBrowser) return;
    
    const token = localStorage.getItem('token');
    const userString = localStorage.getItem('user');
    const expirationDateString = localStorage.getItem('expirationDate');
    
    if (!token || !userString || !expirationDateString) {
      return;
    }
    
    try {
      const userData = JSON.parse(userString);
      const expirationDate = new Date(expirationDateString);
      
      // Vérifier si le token est expiré
      if (expirationDate <= new Date()) {
        this.logout();
        return;
      }
      
      // Mettre à jour le BehaviorSubject
      this.currentUserSubject.next({
        ...userData,
        expirationDate
      });
      
      // Configurer la déconnexion automatique
      const expirationDuration = expirationDate.getTime() - new Date().getTime();
      this.autoLogout(expirationDuration);
    } catch (error) {
      console.error('Erreur lors du chargement des données utilisateur :', error);
      this.logout();
    }
  }

  private autoLogout(expirationDuration: number) {
    if (this.tokenExpirationTimer) {
      clearTimeout(this.tokenExpirationTimer);
    }
    
    this.tokenExpirationTimer = setTimeout(() => {
      this.logout();
    }, expirationDuration);
  }

  getUserRole(): string {
    if (!this.isBrowser) {
      return '';
    }
      
    
    const userString = localStorage.getItem('user');
    if (userString) {
      try {
        const userData = JSON.parse(userString);
        return userData.role || '';
      } catch (error) {
        console.error('Erreur lors de la récupération du rôle utilisateur :', error);
        return '';
      }
    }
    return '';
  }
    
    


  isTokenValid(): boolean {
  const token = this.getToken(); // ou localStorage.getItem('token');
  if (!token) return false;

  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const exp = payload.exp;
    const now = Math.floor(Date.now() / 1000);
    return now < exp;
  } catch (error) {
    console.error('Erreur lors de la validation du token', error);
    return false;
  }
}


getCurrentUser(): any {
  const userJson = localStorage.getItem('currentUser');
  if (!userJson) return null;
  return JSON.parse(userJson);
}




}