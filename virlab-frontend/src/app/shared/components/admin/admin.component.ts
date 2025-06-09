// src/app/components/admin/admin.component.ts
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { RouterModule } from '@angular/router';
import { OnInit } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service'
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-admin',
  standalone: true, // 👈 obligatoire si tu veux qu’il soit autonome
  imports: [RouterOutlet, RouterModule, CommonModule],
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit  {

  username: string = '';

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    
  const user = this.authService.getCurrentUser();
  if (user) {
    this.username = user.username;
  } else {
    this.username = 'Admin'; // par défaut si rien en localStorage
  }
  this.checkScreenSize();
     window.addEventListener('resize', () => this.checkScreenSize());
}



 menuActive = false;
usersMenuOpen = false;
afficherMenuOpen = false;
projectMenuOpen = false;
afficheProjectOpen = false;

toggleMenu() {
  this.menuActive = !this.menuActive;
}

toggleUsersMenu() {
  this.usersMenuOpen = !this.usersMenuOpen;
  if (this.usersMenuOpen) {
    this.projectMenuOpen = false;
  }
}

toggleProjectsMenu() {
  this.projectMenuOpen = !this.projectMenuOpen;
  if (this.projectMenuOpen) {
    this.usersMenuOpen = false;
  }
}


toggleAfficherMenu() {
  this.afficherMenuOpen = !this.afficherMenuOpen;
}

selectMenu() {
  this.menuActive = false;
  this.usersMenuOpen = false;
  this.afficherMenuOpen = false;
}

closeMenu() {
  this.menuActive = false;
  this.usersMenuOpen = false;
  this.afficherMenuOpen = false;
}

isLargeScreen = false;
checkScreenSize() {
  this.isLargeScreen = window.innerWidth >= 1024; // Tailwind 'lg'
  if (this.isLargeScreen) {
    this.menuActive = true; // Force l'affichage en grand écran
  }
}


}
