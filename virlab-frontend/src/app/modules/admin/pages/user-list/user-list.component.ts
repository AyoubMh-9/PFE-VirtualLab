import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../../../../app/core/services/user.service';
import { User } from '../../../../../app/core/models/user.model';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
users: User[] = [];
  searchTerm = '';
  pageIndex = 0;
  itemsPerPage = 5;
  isLoading = true;
  error = '';
  successMessage: string = '';
  selectedUser: User | null = null; // Pour afficher le formulaire

  selectedRole: string = 'ADMIN'; // ou 'CLIENT', ou 'TECHNICIEN'
  filterUsers: User[] = [];


  constructor(private userService: UserService, private router: Router) {
    const state = history.state as { message?: string };
    if (state?.message) {
      this.successMessage = state.message;
      // Optionnel : nettoyage après 3s
      setTimeout(() => {
        this.successMessage = '';
      }, 3000);
    }
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  

  get currentFilteredUsers(): User[] {
    const term = this.searchTerm.toLowerCase();
    return this.users.filter(user => {
      const roleMatches = user.role === this.selectedRole;
      const searchMatches = user.username.toLowerCase().includes(term) ||
                            user.email.toLowerCase().includes(term);
      return roleMatches && searchMatches;
    });
  }

   onSearchChange(): void {
    this.pageIndex = 0; // Reset to the first page for new search results
    // The `currentFilteredUsers` getter will automatically re-evaluate,
    // and the HTML's `*ngFor` will update accordingly.
    console.log('Users filtered (via getter): ', this.currentFilteredUsers); // Log the result of the getter
  }

  pageSize: number = 5;            // Projets par page
  //currentPage: number = 0;
  paginatedUserss: User[] = [];
  
  updatePaginatedUsers() {
    const start = (this.pageIndex) * this.pageSize;
    const end = start + this.pageSize;
    this.paginatedUserss = this.filterUsers.slice(start, end);
  }

  get filteredAdmins(): User[] {
    return this.users.filter(u => u.role === 'ADMIN' && this.matchesSearch(u));
  }

  get filteredClients(): User[] {
    return this.users.filter(u => u.role === 'CLIENT' && this.matchesSearch(u));
  }

  get filteredTechniciens(): User[] {
    return this.users.filter(u => u.role === 'TECHNICIEN' && this.matchesSearch(u));
  }

  matchesSearch(user: User): boolean {
    const term = this.searchTerm.toLowerCase();
    return user.username.toLowerCase().includes(term) || user.email.toLowerCase().includes(term);
  }

  paginatedUsers(usersToPaginate: User[]): User[] {
    const start = this.pageIndex * this.itemsPerPage; // Corrected calculation
    const end = start + this.itemsPerPage;
    return usersToPaginate.slice(start, end);
  }


  nextPage() {
    this.pageIndex++;
  }

  previousPage() {
    if (this.pageIndex > 0) this.pageIndex--;
  }
  /*
  hasNextPage(): boolean {
    return (
      this.filteredAdmins.length + this.filteredClients.length + this.filteredTechniciens.length >
      (this.pageIndex + 1) * this.itemsPerPage
    );
  }
    */
/*
  editUser(user: User) {
    alert(`TODO: Naviguer vers le formulaire d'édition pour ${user.username}`);
  }
    */

  get usersByRole(): User[] {
  if (this.selectedRole === 'ADMIN') return this.filteredAdmins;
  if (this.selectedRole === 'CLIENT') return this.filteredClients;
  if (this.selectedRole === 'TECHNICIEN') return this.filteredTechniciens;
  return [];
}

// Nouvelle méthode pour changer le rôle sélectionné
selectRole(role: string) {
  this.selectedRole = role;
  this.pageIndex = 0; // Réinitialise la pagination
}

// Corrige hasNextPage pour le rôle sélectionné uniquement
hasNextPage(): boolean {
  return this.usersByRole.length > (this.pageIndex + 1) * this.itemsPerPage;
}



editUser(user: User) {
  this.selectedUser = { ...user }; // clone l'utilisateur
}


cancelEdit() {
  this.selectedUser = null;
}

onUpdateUser() {
  if (!this.selectedUser) return;

  this.userService.updateUser(this.selectedUser).subscribe({
    next: response => {
      this.successMessage = 'Utilisateur mis à jour avec succès';
      this.cancelEdit();  // pour fermer le formulaire
      this.loadUsers();   // recharger la liste
    },
    error: err => {
      console.error('Erreur lors de la mise à jour', err);
    }
  });
}



loadUsers() {
  this.userService.getUsers().subscribe({
    next: (data) => {
      //console.log('Utilisateurs reçus :', data); 
      this.users = data;
      this.filterUsers = data;
      this.isLoading = false;
    },
    error: (err) => {
      //console.error('Erreur de chargement :', err); 
      this.error = 'Erreur lors du chargement des utilisateurs';
      this.isLoading = false;
    }
  });
}

  deleteUser(user: User){
    if (confirm(`Êtes-vous sûr de vouloir supprimer l'utilisateur ${user.username} ?`)) {
    this.userService.deleteUser(user.id).subscribe({
      next: () => {
        // Mise à jour locale de la liste sans cet utilisateur
        this.users = this.users.filter(u => u.id !== user.id);
        //console.log(user.id);
         //Optionnel : mettre à jour usersByRole si tu en as une variable séparée
        //this.filterUsersByRole();

        this.successMessage = `Utilisateur ${user.username} supprimé avec succès.`;
      },
      error: () => {
        alert('Erreur lors de la suppression de l\'utilisateur.');
      }
    });
  }
  }

  



}
