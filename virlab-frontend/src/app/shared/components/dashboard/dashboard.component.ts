// src/app/components/dashboard/dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import { DataService } from '../../../core/services/data.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="dashboard">
      <header>
        <h1>Tableau de bord</h1>
        <div class="user-info">
          <span>Bienvenue, {{ username }}</span>
          <button (click)="logout()">Déconnexion</button>
        </div>
      </header>
      
      <div class="content">
        <div *ngIf="isLoading">Chargement des données...</div>
        <div *ngIf="error" class="error">{{ error }}</div>
        
        <div *ngIf="!isLoading && !error">
          <!-- Afficher vos données ici -->
          <div *ngFor="let item of data">
            {{ item.name }} - {{ item.description }}
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .dashboard {
      padding: 20px;
    }
    header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      padding-bottom: 10px;
      border-bottom: 1px solid #ccc;
    }
    .user-info {
      display: flex;
      align-items: center;
      gap: 10px;
    }
    .error {
      color: red;
      padding: 10px;
      background-color: #ffeeee;
      border-radius: 4px;
    }
  `]
})
export class DashboardComponent implements OnInit {
  username: string = '';
  isLoading: boolean = false;
  error: string | null = null;
  data: any[] = [];

  constructor(
    private authService: AuthService,
    private dataService: DataService
  ) {}

  ngOnInit(): void {
    // Récupérer le nom d'utilisateur
    this.authService.currentUser$.subscribe(user => {
      if (user) {
        this.username = user.username;
      }
    });
    
    // Charger les données
    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;
    this.error = null;
    
    this.dataService.getData().subscribe({
      next: (data) => {
        this.data = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Impossible de charger les données. Veuillez réessayer.';
        this.isLoading = false;
        console.error('Erreur lors du chargement des données', err);
      }
    });
  }

  logout(): void {
    this.authService.logout();
  }
}