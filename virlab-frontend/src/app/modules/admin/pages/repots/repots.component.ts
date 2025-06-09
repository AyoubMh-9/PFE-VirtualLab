import { Component, OnInit} from '@angular/core';
import { UploadedFileService } from '../../../../core/services/uploaded-file.service';
import { UploadedFileDTO } from '../../../../core/models/UploadedFileDTO.model'; 
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { User } from '../../../../core/models/user.model';
import { Produit, Produitnew } from '../../../../core/models/produit.model'; 
import { UserService } from '../../../../core/services/user.service';
import { ProduitService } from '../../../../core/services/product.service'

@Component({
  selector: 'app-repots',
  imports: [CommonModule, FormsModule],
  templateUrl: './repots.component.html',
  styleUrl: './repots.component.css'
})
export class RepotsComponent implements OnInit{


  users: User[] = [];
  projects: Produitnew[] = [];
  allFiles: UploadedFileDTO[] = [];
  displayedFiles: UploadedFileDTO[] = [];

  selectedFile: File | null = null;
  selectedUserId!: number;
  selectedProjectId!: number;

  filterUserId: number | null = null;
filterProjectId: number | null = null;

  constructor(
    private fileService: UploadedFileService,
    private userService: UserService,
    private projectService: ProduitService
  ) {}

  ngOnInit() {
    //this.getUsers();
    //this.getProjects();
    //this.loadFiles();
    this.loadData();
  }

  
  loadFiles() {
    this.fileService.getAllFiles().subscribe(files => {
      this.allFiles = files;
      this.displayedFiles = files;
    });
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  onUploadFile() {
  if (!this.selectedFile || !this.selectedUserId || !this.selectedProjectId) return;

  this.fileService.uploadFile(this.selectedFile, this.selectedUserId, this.selectedProjectId)
    .subscribe(() => {
      alert('Fichier uploadé avec succès !');
      this.loadFiles();
      this.selectedFile = null;
    });
}


 applyFilter() {
  //console.log("Valeurs sélectionnées:", "UserID:", this.filterUserId, "ProjectID:", this.filterProjectId);

  this.displayedFiles = this.allFiles.filter(file => {
    const userMatch = this.filterUserId ? file.uploaderId === this.filterUserId : true;
    const projectMatch = this.filterProjectId ? file.projectId === this.filterProjectId : true;
    return userMatch && projectMatch;
  });

  //console.log("Résultat filtré:", this.displayedFiles);
}

/*
getUsers() {
  this.userService.getUsers().subscribe(
    data => {
      this.users = data;
      console.log("Liste des utilisateurs chargée:", this.users);
    },
    error => {
      console.error("Erreur lors du chargement des utilisateurs", error);
    }
  );
}

getProjects() {
  this.projectService.getProduitsDuClient().subscribe(
    data => {
      this.projects = data;
      console.log("Liste des projets chargée:", this.projects);
    },
    error => {
      console.error("Erreur lors du chargement des projets", error);
    }
  );
}
  */


  getUserNameById(id: number): string {
    const user = this.users.find(u => u.id === id);
    return user ? user.username : '';
  }

  getProjectNameById(id: number): string {
    const project = this.projects.find(p => p.id === id);
    return project ? project.nomProduct : '';
  }

  downloadFile(fileId: number, fileName: string) {
    this.fileService.downloadFile(fileId).subscribe(blob => {
      const a = document.createElement('a');
      const url = window.URL.createObjectURL(blob);
      a.href = url;
      a.download = fileName;
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }




loadData() {
  // Charger les fichiers
  this.fileService.getAllFiles().subscribe(files => {
    this.allFiles = files;
    this.displayedFiles = files;
  });

  // Charger les utilisateurs
  this.userService.getUsers().subscribe(users => {
    this.users = users;
    //console.log("Utilisateurs chargés:", this.users);
  });

  // Charger les projets
  this.projectService.getProduitsDuClient().subscribe(projects => {
    this.projects = projects;
    //console.log("Projets chargés:", this.projects);
  });
}




}
