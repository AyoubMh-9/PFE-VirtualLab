import { Component, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../../../../../app/core/services/user.service';
import { NewUser } from '../../../../../app/core/models/userCreate.module';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-add',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-add.component.html',
  styleUrls: ['./user-add.component.css']  // <-- corrige ici
})
export class UserAddComponent {
  
  @ViewChild('userForm') form!: NgForm;  // référence au formulaire

  user: NewUser = {
    username: '',
    email: '',
    password: '',
    dtype: ''
  };

  message: string = '';

  constructor(private userService: UserService, private router: Router) {}

  onSubmit() {
    if (this.form.valid) {
      this.userService.createUser(this.user).subscribe({
        next: () => {
          this.router.navigate(['/admin/users'], {
            state: { message: 'Utilisateur créé avec succès !' }
          });
        },
        error: () => {
          this.message = '❌ Une erreur est survenue lors de la création.';
        }
      });
    } else {
      this.message = '⚠️ Veuillez remplir correctement le formulaire.';
    }
  }
}
