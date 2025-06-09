import { Component } from '@angular/core';
import { UploadedFileService } from '../../../../core/services/uploaded-file.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-upload-file',
  imports: [FormsModule],
  templateUrl: './upload-file.component.html',
  styleUrl: './upload-file.component.css'
})
export class UploadFileComponent {

  selectedFile!: File;
  userId!: number;
  projectId!: number;

  constructor(private fileUploadService: UploadedFileService) {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  onSubmit() {
    if (this.selectedFile && this.userId && this.projectId) {
      this.fileUploadService.uploadFile(this.selectedFile, this.userId, this.projectId)
        .subscribe(response => {
          console.log('Fichier uploadé avec succès:', response);
          alert('Fichier uploadé avec succès !');
        });
    }
  }

}
