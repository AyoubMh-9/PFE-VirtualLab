import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../../../app/core/services/user.service';
import { User } from '../../../../../app/core/models/user.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserUpdateDTO } from '../../../../core/models/userUpdate.model';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-update.component.html',
  styleUrl: './user-update.component.css'
})
export class UserUpdateComponent {
  userId!: number;
  userData: UserUpdateDTO = {
    username: '',
    email: '',
    password: ''
  };

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userId = +this.route.snapshot.paramMap.get('id')!;
    this.userService.getUserById(this.userId).subscribe(data => {
      this.userData.username = data.username;
      this.userData.email = data.email;
      this.userData.password = data.motDePasse;
    });
  }

  updateUser(): void {
    this.userService.updateUser(this.userData).subscribe({
      next: () => {
        alert('Utilisateur mis à jour avec succès !');
        this.router.navigate(['/users']);
      },
      error: err => {
        console.error(err);
        alert('Erreur lors de la mise à jour.');
      }
    });
  }
}
