export interface UploadedFileDTO {
  id: number;
  fileName: string;
  fileType: string;
  uploadTime: string; // ou Date si tu convertis
  uploaderUsername: string;
  uploaderId: number;      // Ajouté
  projectName: string;
  projectId: number;       // Ajouté
}
