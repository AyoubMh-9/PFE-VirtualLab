import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProduitService } from '../../../../../app/core/services/product.service';
import { UserService } from '../../../../../app/core/services/user.service';
import { User } from '../../../../../app/core/models/user.model';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-project-add',
  imports: [CommonModule, FormsModule],
  templateUrl: './project-add.component.html',
  styleUrl: './project-add.component.css'
})

export class ProjectAddComponent implements OnInit {

  newProject: any = {
    nomProduct: '',
    description: '',
    technicienId: 0
  };

  isLoading = false;

  constructor(private produitService: ProduitService, private userService: UserService, private router: Router) {}

  technicians: User[] = [];

  ngOnInit(): void {
    this.userService.getAllTechnicians().subscribe(
      (data) => {
        this.technicians = data;
      },
      (error) => {
        console.error('Erreur lors de la récupération des techniciens', error);
      }
    );
  }

  addProject() {
    this.isLoading = true;

    this.produitService.createProject(this.newProject).subscribe(
      (response) => {
        this.isLoading = false;

        Swal.fire({
          position: 'top-end',
          icon: 'success',
          title: response.message || 'Projet créé avec succès 🎉',
          showConfirmButton: false,
          timer: 2000,
          timerProgressBar: true
        });

        // Reset form
        this.newProject = {
          nomProduct: '',
          description: '',
          technicienId: 0
        };

        setTimeout(() => {
          this.router.navigate(['/admin/projects']);
        }, 1500);
      },
      (error) => {
        this.isLoading = false;

        Swal.fire({
          icon: 'error',
          title: 'Erreur 😥',
          text: error.error || 'Une erreur est survenue lors de la création du projet.',
          confirmButtonColor: '#d33'
        });
      }
    );
  }
}



  


