import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';

import { Produit } from '../../../core/models/produit.model';
import { ProduitService } from '../../../core/services/product.service';
import { AuthService } from '../../../core/services/auth.service';

// Enum pour les différentes vues
export enum ViewType {
  DASHBOARD = 'dashboard',
  PROJECTS = 'projects',
  TASKS = 'tasks',
  SUBTASKS = 'subtasks',
  TESTS = 'tests',
  REPORTS = 'reports',
  PROFILE = 'profile'
}

@Component({
  selector: 'app-technicien',
  imports: [CommonModule, FormsModule],
  templateUrl: './technicien-dashboard.component.html',
  styleUrls: ['./technicien-dashboard.component.css']
})
export class TechnicienComponent implements OnInit {
  // Variables pour contrôler l'affichage
  currentView: ViewType = ViewType.DASHBOARD;
  ViewType = ViewType; // Pour utiliser l'enum dans le template

  // Variables pour les statistiques du dashboard
  projectCountActif: number = 0;
  projectCountNoActif: number = 0;
  projectCountDone: number = 0;

  // Variables pour les projets
  projects: Produit[] = [];
  projectsLoading = false;
  projectsError: string | null = null;

  // Variables pour les tâches (exemple)
  tasks: any[] = [];
  tasksLoading = false;
  tasksError: string | null = null;

  // Variables pour les tests (exemple)
  tests: any[] = [];
  testsLoading = false;
  testsError: string | null = null;

  // Variables pour les rapports (exemple)
  reports: any[] = [];
  reportsLoading = false;
  reportsError: string | null = null;

  constructor(private projectService: ProduitService, private authService: AuthService) {}

  ngOnInit(): void {
    this.loadDashboardStats();
  }

  // Méthodes de navigation
  showDashboard(): void {
    this.currentView = ViewType.DASHBOARD;
  }

  showProjects(): void {
    this.currentView = ViewType.PROJECTS;
    this.loadProjects();
  }

  showTasks(): void {
    this.currentView = ViewType.TASKS;
    this.loadTasks();
  }

  showSubTasks(): void {
    this.currentView = ViewType.SUBTASKS;
    this.loadSubTasks();
  }

  showTests(): void {
    this.currentView = ViewType.TESTS;
    this.loadTests();
  }

  showReports(): void {
    this.currentView = ViewType.REPORTS;
    this.loadReports();
  }

  showProfile(): void {
    this.currentView = ViewType.PROFILE;
  }

  // Méthodes de chargement des données
  loadDashboardStats(): void {
    // Charger les statistiques depuis votre service
    const technician = this.authService.getCurrentUser(); // Remplace par la méthode qui te donne l'ID du technicien connecté
    const technicianId = technician.id;
    //console.log('id = ' , technicianId);
    this.projectService.getProjectCountByTechnicianAcitf(technicianId).subscribe(
      data => this.projectCountActif = data
    );
    this.projectService.getProjectCountByTechnicianNoActif(technicianId).subscribe(
      data => this.projectCountNoActif = data
    )
    this.projectService.getProjectCountByTechnicianDone(technicianId).subscribe(
      data => this.projectCountDone = data
    )
  }

  loadProjects(): void {
    if (this.projects.length > 0) return; // Éviter de recharger si déjà chargé

    this.projectsLoading = true;
    this.projectsError = null;
    
    this.projectService.getAssignedProjects().subscribe({
      next: (data) => {
        this.projects = data;
        this.projectsLoading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des projets:', err);
        this.projectsError = 'Erreur lors du chargement des projets.';
        this.projectsLoading = false;
      }
    });
  }

  loadTasks(): void {
    if (this.tasks.length > 0) return;

    this.tasksLoading = true;
    this.tasksError = null;
    
    // Simuler le chargement des tâches
    setTimeout(() => {
      this.tasks = [
        { id: 1, title: 'Tâche 1', description: 'Description de la tâche 1', status: 'En cours' },
        { id: 2, title: 'Tâche 2', description: 'Description de la tâche 2', status: 'Terminé' },
        { id: 3, title: 'Tâche 3', description: 'Description de la tâche 3', status: 'En attente' }
      ];
      this.tasksLoading = false;
    }, 1000);
  }

  loadSubTasks(): void {
    // Logique pour charger les sous-tâches
    console.log('Chargement des sous-tâches...');
  }

  loadTests(): void {
    if (this.tests.length > 0) return;

    this.testsLoading = true;
    this.testsError = null;
    
    // Simuler le chargement des tests
    setTimeout(() => {
      this.tests = [
        { id: 1, name: 'Test 1', result: 'Réussi', date: '2024-01-15' },
        { id: 2, name: 'Test 2', result: 'Échoué', date: '2024-01-14' },
        { id: 3, name: 'Test 3', result: 'En cours', date: '2024-01-13' }
      ];
      this.testsLoading = false;
    }, 1000);
  }

  loadReports(): void {
    if (this.reports.length > 0) return;

    this.reportsLoading = true;
    this.reportsError = null;
    
    // Simuler le chargement des rapports
    setTimeout(() => {
      this.reports = [
        { id: 1, name: 'Rapport mensuel', type: 'PDF', date: '2024-01-15' },
        { id: 2, name: 'Rapport technique', type: 'DOCX', date: '2024-01-10' },
        { id: 3, name: 'Analyse des performances', type: 'XLSX', date: '2024-01-05' }
      ];
      this.reportsLoading = false;
    }, 1000);
  }

  // Méthodes utilitaires
  getStatusClass(status: string): string {
    switch (status?.toLowerCase()) {
      case 'actif':
      case 'active':
      case 'en cours':
        return 'status-active';
      case 'en attente':
      case 'pending':
        return 'status-pending';
      case 'terminé':
      case 'completed':
      case 'réussi':
        return 'status-completed';
      case 'échoué':
      case 'failed':
        return 'status-failed';
      default:
        return 'status-inactive';
    }
  }

  // Méthodes d'actions
  viewProjectDetails(project: Produit): void {
    Swal.fire({
      title: project.nomProduct,
      html: `
        <div style="text-align: left;">
          <p><strong>Description:</strong> ${project.description || 'Aucune description'}</p>
          <p><strong>Statut:</strong> ${project.status}</p>
        </div>
      `,
      icon: 'info',
      confirmButtonColor: '#667eea'
    });
  }

  viewTaskDetails(task: any): void {
    Swal.fire({
      title: task.title,
      html: `
        <div style="text-align: left;">
          <p><strong>Description:</strong> ${task.description}</p>
          <p><strong>Statut:</strong> ${task.status}</p>
        </div>
      `,
      icon: 'info',
      confirmButtonColor: '#667eea'
    });
  }

  viewTestDetails(test: any): void {
    Swal.fire({
      title: test.name,
      html: `
        <div style="text-align: left;">
          <p><strong>Résultat:</strong> ${test.result}</p>
          <p><strong>Date:</strong> ${test.date}</p>
        </div>
      `,
      icon: 'info',
      confirmButtonColor: '#667eea'
    });
  }

  downloadReport(report: any): void {
    console.log('This is For downloadReport');
    Swal.fire({
      title: 'Téléchargement',
      text: `Téléchargement de ${report.name}...`,
      icon: 'success',
      timer: 2000,
      showConfirmButton: false
    });
  }

  uploadReport(): void {
    Swal.fire({
      title: 'Upload de rapport',
      text: 'Fonctionnalité d\'upload à implémenter',
      icon: 'info',
      confirmButtonColor: '#667eea'
    });
  }

  // TrackBy functions pour optimiser les performances
  trackByProjectId(index: number, project: Produit): any {
    return project.id || index;
  }

  trackByTaskId(index: number, task: any): any {
    return task.id || index;
  }

  trackByTestId(index: number, test: any): any {
    return test.id || index;
  }

  trackByReportId(index: number, report: any): any {
    return report.id || index;
  }
}




//////////////////////////////////////::
