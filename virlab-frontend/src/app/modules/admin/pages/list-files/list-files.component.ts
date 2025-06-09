import { Component, OnInit } from '@angular/core';
import { UploadedFileService } from '../../../../core/services/uploaded-file.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-list-files',
  imports: [CommonModule, FormsModule],
  templateUrl: './list-files.component.html',
  styleUrl: './list-files.component.css'
})
export class ListFilesComponent implements OnInit {


  projectId!: number;
  files: any[] = [];

  constructor(private fileUploadService: UploadedFileService) {}

  ngOnInit(): void {}

  fetchFiles() {
    this.fileUploadService.getFilesByProject(this.projectId).subscribe(data => {
      this.files = data;
    });
  }


}
