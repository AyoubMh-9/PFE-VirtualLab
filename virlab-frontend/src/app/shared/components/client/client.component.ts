// client.component.ts
import { Component, OnInit, OnDestroy, ElementRef, ViewChild  } from '@angular/core';
import { BaseChartDirective } from 'ng2-charts'; 
import { ChartConfiguration, ChartOptions, ChartType } from 'chart.js';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { Produit , ProductResponseDTO,ProductDTO, TestGroupDTO,  TestDTO, TestResponseDTO, TestGroupResponseDTO} from '../../../core/models/produit.model';
import { ProduitService } from '../../../core/services/product.service';
import { TestService } from '../../../core/services/test.service';
import { AuthService } from '../../../core/services/auth.service';
import { ProjectPerformance } from '../../../core/models/ProjectPerformance.model';
import { UploadFileComponent } from '../../../modules/admin/pages/upload-file/upload-file.component';
import { UploadedFileService } from '../../../core/services/uploaded-file.service';
import { Html5QrcodeScanner } from 'html5-qrcode';
import { UploadedFileDTO } from '../../../core/models/UploadedFileDTO.model';


interface Project {
  id: number;
  name: string;
  status: string;
  progress: number;
  startDate: string;
  description: string;
}

interface Test {
  id: number;
  testName: string;
  status: string;
  score: number;
  date: string;
}

interface Stat {
  value: number;
  unit: string;
  label: string;
  trend: string;
}

interface Message {
  id: number;
  sender: string;
  message: string;
  isFromTechnician: boolean;
  timestamp: string;
}

interface Report {
  id: number;
  title: string;
  description: string;
  date: string;
}

export interface ChatMessage {
  id: string; // Unique ID for the message
  senderId: number; // ID of the sender (client, technician, admin)
  senderUsername: string; // Username of the sender
  receiverId?: number; // ID of the receiver (e.g., technicianId or adminId)
  projectId?: number; // Project ID if it's a project-specific chat
  isAdminChat: boolean; // True if it's a chat with admin
  content: string; // The message text
  timestamp: string; // When the message was sent (ISO string)
}

@Component({
  selector: 'app-client',
  imports: [CommonModule, FormsModule],
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.css']
})
export class ClientComponent implements OnInit, OnDestroy  {
  sidebarCollapsed: boolean = false;
  currentSection: string = 'projects';
  showUserMenu: boolean = false;
  newMessage: string = '';
  projectCode: string = '';
  //qrScanMessage: string = '';
  //showQrScanner: boolean = false;

  private html5QrCodeScanner: Html5QrcodeScanner | null = null;

  // Data properties
  projects: ProductResponseDTO[] = [];
  projectsLoading: boolean = false;
  projectsError: string | null = null;

  // New state variables for navigation
  selectedProject: ProductResponseDTO | null = null;
  selectedTestGroup: TestGroupResponseDTO | null = null;
  selectedTest: TestResponseDTO | null = null; // To view individual test details/results


  // Sample data (replace with actual service calls)
  sampleTests = [ /* ... your existing sample tests ... */ ];
  sampleStats: Stat[] = [
    { value: 0, unit: "", label: "Projets Actifs", trend: "up" }, // Initialize with 0
    { value: 0, unit: "%", label: "Taux de Réussite", trend: "up" }, // Initialize with 0
    { value: 0, unit: "", label: "Tests Effectués", trend: "stable" },
    { value: 0, unit: "", label: "Rapports Générés", trend: "down" }
  ];
   sampleMessages: Message[] = [
    { id: 1, sender: "Technicien Support", message: "Bonjour, comment puis-je vous aider aujourd'hui ?", isFromTechnician: true, timestamp: "10:30" },
    { id: 2, sender: "Vous", message: "J'ai un problème avec mon projet e-commerce", isFromTechnician: false, timestamp: "10:32" },
    { id: 3, sender: "Technicien Support", message: "Pouvez-vous me donner plus de détails sur le problème ?", isFromTechnician: true, timestamp: "10:35" }
  ];

  sampleReports: Report[] = [
    { id: 1, title: "Rapport Mensuel - Janvier", description: "Résumé des activités et performances du mois", date: "2024-01-31" },
    { id: 2, title: "Analyse de Performance", description: "Analyse détaillée des métriques de performance", date: "2024-02-10" },
    { id: 3, title: "Rapport de Sécurité", description: "Évaluation de la sécurité des applications", date: "2024-02-05" }
  ];

  chartHeights = [60, 80, 45, 90, 70, 85, 55];

  


  constructor(private productService: ProduitService, private testService: TestService, private uploadedFile: UploadedFileService, private authService: AuthService) { } // Inject your ProductService
projectCountActif: number = 0;
currentUserId: number | null = null; // To store the user's ID
  currentUserUsername: string | null = null; // To store the user's username (optional, but useful)
  ngOnInit(): void {

    const currentUser = this.authService.getCurrentUser(); // Use your new method

    if (currentUser && currentUser.id) { // Assuming the stored user object has an 'id' property
      this.currentUserId = currentUser.id;
      console.log('Current authenticated User ID from localStorage:', this.currentUserId);
    } else {
      console.warn('Could not get user ID from localStorage. User might not be logged in or data is incomplete.');
    }

    if (currentUser && currentUser.username) { // Assuming the stored user object has a 'username' property
      this.currentUserUsername = currentUser.username;
      console.log('Current authenticated Username from localStorage:', this.currentUserUsername);
    }


  //this.loadProjects(); // This fetches projects and their individual counts, and also the global stats
  if (this.currentSection === 'projects' || this.currentSection === 'statistiques') {
        this.loadProjects(); // Load initial stats
    }
    this.loadClientProjectsForReports();
}

numberOfSuccess: number = 0;
numberOfTests: number = 0;




loadProjects(): void {
  this.projectsLoading = true;
  this.projectsError = null;

  

  this.productService.getProjectForClient().subscribe({
    next: (data: ProductResponseDTO[]) => {
      this.projects = data;
      console.log('the Project is : ', this.projects);
      this.projectsLoading = false;
      this.loadProjectPerformanceGraphData(); // This is correctly placed here as it depends on `this.projects`
    },
    error: (err) => {
      console.error('Error loading projects:', err);
      this.projectsError = 'Échec du chargement des projets. Veuillez réessayer.';
      this.projectsLoading = false;
    }
  });

  // Fetch Active Projects Count
  this.productService.getProjectActifByClient().subscribe({
    next: (data: number) => {
      this.projectCountActif = data;
      const activeProjectsStat = this.sampleStats.find(s => s.label === 'Projets Actifs');
      if (activeProjectsStat) {
        activeProjectsStat.value = this.projectCountActif;
        activeProjectsStat.trend = 'up';
      }
    },
    error: (err) => {
      console.error('Error loading active project count:', err);
    }
  });

  // Fetch Total Tests Count
  // Ensure productService has getAllTestsCount() or you have a TestService injected
  this.productService.getAllTestsCount().subscribe({
    next: (count: number) => {
      const totalTestsStat = this.sampleStats.find(s => s.label === 'Tests Effectués');
      this.numberOfTests = count;
      if (totalTestsStat) {
        totalTestsStat.value = count;
        console.log('count = ' , count ,'nbr of tests : ', totalTestsStat.value);
        totalTestsStat.trend = 'stable';
      }
    },
    error: (err) => {
      console.error('Error loading total tests count:', err);
    }
  });


  this.testService.getSuccessfulTestsCount().subscribe({
      next: (count: number) => {
        this.numberOfSuccess = count;
        console.log(this.numberOfSuccess);
        const successTestStat = this.sampleStats.find(s => s.label === 'Taux de Réussite');
        if(successTestStat){
          //successTestStat.value = this.calculateSuccessRate();
          if(this.numberOfTests > 0){
            const rate = (this.numberOfSuccess / this.numberOfTests) * 100;
            successTestStat.value = parseFloat(rate.toFixed(1));
            successTestStat.trend = 'up';
          }else{
            successTestStat.value = 0;
            successTestStat.trend = 'stable';
          }
          console.log('Success Rate calculated:', successTestStat.value);
        }
      },
      error: (err) => {
        console.error('Error loading successful tests count:', err);
      }
    });

  // Fetch Total Reports (Uploaded Files) Count
  // Ensure `uploadedFile` is an injected service (e.g., UploadedFileService)
  // And it has the getNumberOfFileByClient() method.
  this.uploadedFile.getNumberOfFileByClient().subscribe({
    next: (count: number) => {
      const totalRapport = this.sampleStats.find(s => s.label === 'Rapports Générés');
      if(totalRapport) {
        totalRapport.value = count;
        console.log('count = ' , count ,'nbr of Rapports : ', totalRapport.value);
        // You might want to set a trend for reports too (up/down/stable)
        totalRapport.trend = 'stable'; // Example
      }
    },
    error: (err) => {
      console.error('Error loading total rapports count:', err);
    }
  });
}
  /*
loadStats(): void {
    this.productService.getProjectActifByClient().subscribe({
      next: (count: number) => {
        const activeProjectsStat = this.sampleStats.find(s => s.label === 'Projets Actifs');
        if (activeProjectsStat) {
          activeProjectsStat.value = count;
          activeProjectsStat.trend = 'up'; // Or determine trend based on historical data
          console.log('The nulber is : ',count);
        }
      },
      error: (err) => {
        console.error('Error loading active project count:', err);
        // Optionally set an error state for the stat card
      }
    });

    // For "Taux de Réussite", "Tests Effectués", "Rapports Générés",
    // you will need to implement similar service calls to your backend endpoints
    // that provide these aggregated statistics.
    // For now, they remain static or use mock calculations from previously loaded data.
    this.updateSuccessRateStat(); // If based on `this.projects`
  }
*/
/*
projectCountActif: number = 0;
loadStats(): void {
  this.productService.getProjectActifByClient().subscribe(
      data => this.projectCountActif = data
    );
}
    */
  updateSuccessRateStat(): void {
    // This is a simplified calculation based on `calculatedProgress` from loaded projects.
    // For a more accurate "Taux de Réussite", you'd typically need aggregated test results.
    if (this.projects.length === 0) return;

    const totalProgressSum = this.projects.reduce((sum, project) => sum + (project.calculatedProgress || 0), 0);
    const averageProgress = Math.round(totalProgressSum / this.projects.length);

    const successRateStat = this.sampleStats.find(s => s.label === 'Taux de Réussite');
    if (successRateStat) {
      successRateStat.value = averageProgress; // Using average progress as a proxy for success rate
      // Determine trend:
      // You would need to compare current averageProgress with a previous period's average.
      // For now, it stays 'up' as initialized or based on your logic.
    }
  }



  // --- Navigation and View Management ---
  setActiveSection(section: string): void {
    this.currentSection = section;
  }

  getSectionTitle(): string {
    switch (this.currentSection) {
      case 'projects':
        return 'Mes Projets';
      case 'tests':
        return 'Tests & Résultats';
      case 'statistics':
        return 'Statistiques';
      case 'chat':
        return 'Chat Technicien';
      case 'joinProject':
        return 'Rejoindre un Projet';
      case 'reports':
        return 'Rapports';
      default:
        return 'Dashboard';
    }
  }

  toggleSidebar(): void {
    this.sidebarCollapsed = !this.sidebarCollapsed;
  }

  toggleUserMenu(): void {
    this.showUserMenu = !this.showUserMenu;
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'EN_COURS':
        return 'status-in-progress';
      case 'COMPLETE':
        return 'status-completed';
      case 'SUSPENDU':
        return 'status-suspended';
      case 'PASSED': // For test results
        return 'status-passed';
      case 'FAILED': // For test results
        return 'status-failed';
      default:
        return 'status-unknown';
    }
  }

  // Clear selections when navigating to a new main section
  clearSelections(): void {
    this.selectedProject = null;
    this.selectedTestGroup = null;
    this.selectedTest = null;
  }

  // --- Project Specific Actions ---
  editProject(projectId: number): void {
    console.log('Edit project:', projectId);
    // Implement navigation to edit form or modal
  }

  viewProjectDetails(project: ProductResponseDTO): void {
    this.selectedProject = project;
    console.log("Selected Project : ", this.selectedProject);
    this.selectedTestGroup = null; // Clear any previously selected test group
    this.selectedTest = null; // Clear any previously selected test
    // No need to fetch again, as testGroups are already nested in ProductDTO
  }

  backToProjects(): void {
    this.selectedProject = null;
    this.selectedTestGroup = null;
    this.selectedTest = null;
  }

  // --- Test Group Specific Actions ---
  viewTestGroupDetails(group: TestGroupResponseDTO): void {
    this.selectedTestGroup = group;
    this.selectedTest = null; // Clear any previously selected test
    // No need to fetch again, as tests are already nested in TestGroupDTO
  }

  backToTestGroups(): void {
    this.selectedTestGroup = null;
    this.selectedTest = null;
  }

  // --- Test Specific Actions ---
  viewTestResultDetails(test: TestResponseDTO): void {
    this.selectedTest = test;
    // If test.result is not nested, you might need an additional service call here:
    // this.testService.getTestResult(test.id).subscribe(result => this.selectedTest.result = result);
  }

  backToTestsList(): void {
    this.selectedTest = null;
  }

  // --- Other Section Actions (keep as is) ---
  runNewTest(): void {
    console.log('Run new test');
  }
/*
  joinProject(): void {
    console.log('Join project with code:', this.projectCode);
    this.productService.joinProjectForClient(this.projectCode)
    // Implement API call to join project
  }
*/


 joinProjectMessage: string | null = null; // Message for success/error
  isJoiningProject: boolean = false; // To show loading state
private successMessageTimeout: any; // To hold the timeout reference
 joinProject(): void {
    // Clear any existing timeout for messages
    if (this.successMessageTimeout) {
      clearTimeout(this.successMessageTimeout);
    }

    this.joinProjectMessage = null; // Clear previous messages
    this.isJoiningProject = true; // Set loading state

    if (!this.projectCode.trim()) {
      Swal.fire({
        icon: 'warning',
        title: 'Attention',
        text: 'Veuillez entrer un code de projet.',
        confirmButtonText: 'OK',
        customClass: {
          confirmButton: 'swal-button-primary' // Custom class for styling
        }
      });
      this.isJoiningProject = false;
      return;
    }

    console.log('Attempting to join product with access code:', this.projectCode);
    this.productService.joinProductByAccessCode(this.projectCode).subscribe({
      next: (response) => {
        // 1. Display success message
        this.joinProjectMessage = response; // Backend returns the success string
        console.log('Product join successful:', response);
        this.isJoiningProject = false;
        this.projectCode = ''; // Clear the input field on success

       
        Swal.fire({
          icon: 'success',
          title: 'Succès !',
          text: response || 'Client ajouté au produit avec succès.', // Use backend message or default
          showConfirmButton: false, // No "OK" button
          timer: 2500, // Close after 2.5 seconds
          timerProgressBar: true,
          customClass: {
            popup: 'swal-custom-popup',
            title: 'swal-custom-title',
            htmlContainer: 'swal-custom-content'
          },
          didClose: () => { // Callback when the Swal closes
            // Redirect to "Mes Projets" after Swal closes
            this.setActiveSection('projects');
            this.loadProjects(); // Ensure projects are reloaded
          }
        });
      },
      error: (err) => {
        this.isJoiningProject = false;
        let errorMessage = 'Échec de la jointure du projet.';
        if (err.error) {
          errorMessage = err.error.message || err.error;
        }
        this.joinProjectMessage = errorMessage;
        console.error('Product join error:', err);

        // Optional: Clear error message after a delay if desired
        Swal.fire({
          icon: 'error',
          title: 'Erreur !',
          text: errorMessage,
          confirmButtonText: 'OK',
          customClass: {
            confirmButton: 'swal-button-danger' // Custom class for styling
          }
        });
      }
    });
  }

  // Optional: Implement ngOnDestroy to clear timeout if component is destroyed
  // This prevents memory leaks if the user navigates away before the timeout fires.
  /*
  ngOnDestroy(): void {
    if (this.successMessageTimeout) {
      clearTimeout(this.successMessageTimeout);
    }
  }
    */
/*
  scanQRCode(): void {
    this.showQrScanner = true;
    this.qrScanMessage = 'Activating camera...';
    // Integrate a QR scanner library here, e.g., html5-qrcode
    // On scan success: this.projectCode = scannedCode; this.joinProject();
    // On scan error: this.qrScanMessage = 'QR scan failed.';
  }
    */

  // QR Code Scanning (Placeholder)
  // Properties for QR code scanning
  showQrScanner: boolean = false;
  qrScanMessage: string | null = null;
  scanQRCode(): void {
    this.qrScanMessage = null; // Clear previous messages
    this.showQrScanner = !this.showQrScanner; // Toggle scanner visibility

    if (this.showQrScanner) {
      this.qrScanMessage = 'Activating camera for QR scan...';

      // Use setTimeout to ensure the DOM element is rendered before initializing the scanner
      setTimeout(() => {
        // Initialize scanner only if it's not already initialized
        if (!this.html5QrCodeScanner) {
          this.html5QrCodeScanner = new Html5QrcodeScanner(
            "qr-reader", // This ID must match the div in HTML
            {
              fps: 10,
              qrbox: { width: 250, height: 250 },
              disableFlip: false,
            },
            /* verbose= */ false
          );
        }

        // Render the scanner
        this.html5QrCodeScanner.render(
          (decodedText, decodedResult) => {
            console.log(`QR Code scanned: ${decodedText}`, decodedResult);
            this.qrScanMessage = `QR Code scanné: ${decodedText}`;
            this.projectCode = decodedText;

            this.html5QrCodeScanner?.clear().then(() => {
              this.showQrScanner = false;
              this.qrScanMessage = 'Scan terminé.';
              this.joinProject(); // Auto-join
            }).catch((err) => {
              console.error('Failed to clear html5QrCodeScanner after successful scan:', err);
            });
          },
          (errorMessage) => {
            // Error callback for continuous scanning (can be noisy)
          }
        );
      }, 0); // <--- Add a small delay (0ms is enough to defer)
    } else {
      // If scanner was active and button clicked to hide, stop it
      if (this.html5QrCodeScanner) {
        this.html5QrCodeScanner.clear().then(() => {
          console.log("html5QrCodeScanner cleared.");
          this.qrScanMessage = null;
        }).catch((err) => {
          console.error("Failed to clear html5QrCodeScanner:", err);
          this.qrScanMessage = 'Erreur lors de l\'arrêt du scanner.';
        });
      }
    }
  }

  // Important: Clean up the scanner when the component is destroyed
  ngOnDestroy(): void {
    if (this.html5QrCodeScanner) {
      this.html5QrCodeScanner.clear().then(() => {
        console.log("Scanner cleared on component destroy.");
      }).catch((err) => {
        console.error("Error clearing scanner on destroy:", err);
      });
    }
    // Also clear any other timeouts/subscriptions you might have
  }
  generateReport(): void {
    console.log('Generate report');
  }

clientProjects: ProductResponseDTO[] = [];

loadClientProjectsForReports(): void {
    this.productService.getProjectForClient().subscribe({
      next: (projects) => {
        this.clientProjects = projects;
        // Optionally pre-select the first project if available
        if (this.clientProjects.length > 0) {
          this.selectedProjectId = this.clientProjects[0].id;
          this.loadReportsForSelectedProject(); // Load reports for the first project automatically
        }
      },
      error: (err) => {
        console.error('Error loading client projects for reports:', err);
        Swal.fire('Erreur', 'Impossible de charger la liste des projets.', 'error');
      }
    });
  }
onProjectSelectChange(): void {
    // This method is called when the user selects a different project from the dropdown.
    // It simply triggers the loading of reports for the newly selected project.
    this.loadReportsForSelectedProject();
  }

  get selectedProjectName(): string {
    const selectedProject = this.clientProjects.find(p => p.id === this.selectedProjectId);
    return selectedProject ? selectedProject.nomProduct : 'Projet sélectionné'; // Provide a default/fallback
  }

projectReports: UploadedFileDTO[] = []; 
//clientProjects: ProductResponseDTO[] = [];
  selectedProjectId: number | null = null;
  isLoadingReports: boolean = false;
  isDownloadingFile: boolean = false;
  loadReportsForSelectedProject(): void {
    if (this.selectedProjectId) {
      this.isLoadingReports = true;
      this.projectReports = []; // Clear previous reports
      // Your service call will now receive the data matching the DTO
      this.uploadedFile.getRapportsForProjects(this.selectedProjectId).subscribe({
        next: (reports) => {
          this.projectReports = reports; // The incoming reports array will match the DTO structure
          this.isLoadingReports = false;
        },
        error: (err) => {
          console.error('Error loading reports for project:', this.selectedProjectId, err);
          this.isLoadingReports = false;
          Swal.fire('Erreur', 'Impossible de charger les rapports pour ce projet.', 'error');
        }
      });
    } else {
      this.projectReports = [];
    }
  }

isExporting: boolean = false;
  exportReports(): void {
    console.log('Export reports triggered.');

    if (this.selectedProjectId) {
      this.isExporting = true;
      this.uploadedFile.exportProjectReports(this.selectedProjectId).subscribe({
        next: (data: Blob) => {
          const downloadUrl = window.URL.createObjectURL(data);
          const link = document.createElement('a');
          link.href = downloadUrl;
          link.download = `reports_export_project_${this.selectedProjectId}.csv`; // Dynamic filename
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
          window.URL.revokeObjectURL(downloadUrl);
          this.isExporting = false;
          Swal.fire('Succès', 'Les rapports ont été exportés avec succès.', 'success');
        },
        error: (err) => {
          console.error('Error exporting reports:', err);
          this.isExporting = false;
          Swal.fire('Erreur', 'Impossible d\'exporter les rapports.', 'error');
        }
      });
    } else {
      Swal.fire('Attention', 'Veuillez sélectionner un projet pour exporter les rapports.', 'warning');
    }
  }

  downloadReport(file: UploadedFileDTO): void {
    this.isDownloadingFile = true;
    this.uploadedFile.downloadFile(file.id).subscribe({
      next: (data: Blob) => {
        const downloadUrl = window.URL.createObjectURL(data);
        const link = document.createElement('a');
        link.href = downloadUrl;
        link.download = file.fileName;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(downloadUrl);
        this.isDownloadingFile = false;
        Swal.fire('Téléchargement', `${file.fileName} a été téléchargé avec succès.`, 'success');
      },
      error: (err) => {
        console.error('Error downloading file:', err);
        this.isDownloadingFile = false;
        Swal.fire('Erreur', 'Impossible de télécharger le fichier.', 'error');
      }
    });
  }

   viewReport(file: UploadedFileDTO): void {
    // For 'view', we trigger download, browser usually handles it
    this.downloadReport(file);
  }
/*
  sendMessage(){
    console.log('View message:');
  }
    */


  // Inside your ClientComponent class
// Inside your ClientComponent class

getProgressBarGradientClass(progress: number): string {
  if (progress < 25) {
    return 'low-progress'; // Applies --low-progress-gradient
  } else if (progress < 75) {
    return 'medium-progress'; // Applies --medium-progress-gradient
  } else {
    return 'high-progress'; // Applies --high-progress-gradient
  }
}

calculateMockSuccessRate(): number {
    if (this.projects.length === 0) return 0;
    const totalScore = this.projects.reduce((sum, project) => sum + (project.calculatedProgress || 0), 0);
    return Math.round(totalScore / this.projects.length); // Average the mock scores
  }

  

// New data structure for the project performance graph
  projectPerformanceData: ProjectPerformance[] = [];
  // Inside your loadProjectPerformanceGraphData() method
loadProjectPerformanceGraphData(): void {
  if (this.projects && this.projects.length > 0) {
    this.projectPerformanceData = this.projects.map(project => {
      let score = project.calculatedProgress || 0; // Get the raw score
        console.log(score);
      // Option A: Ensure 100% fills up to, say, 95% of the chart height
      // and apply a base minimum value for visibility.
      // This will map a score from 0-100 to a height between MIN_HEIGHT_PERCENT and MAX_HEIGHT_PERCENT
      const MIN_HEIGHT_PERCENT = 5;  // Smallest bar is 5% of chart height
      const MAX_HEIGHT_PERCENT = 95; // Tallest bar is 95% of chart height
      const scaledHeight = MIN_HEIGHT_PERCENT + (score / 100) * (MAX_HEIGHT_PERCENT - MIN_HEIGHT_PERCENT);
      console.log('the scaledHeight = ', scaledHeight);
      return {
        projectName: project.nomProduct,
        performanceScore: scaledHeight // Use the scaled score for height
      };
    });
  } else {
    this.projectPerformanceData = [];
    console.warn("Projects not loaded yet for performance graph or no projects found.");
  }
}

get selectedChatProjectName(): string {
    const selectedProject = this.clientProjects.find(p => p.id === this.selectedChatProjectId);
    return selectedProject ? selectedProject.nomProduct : 'N/A';
  }

  onChatProjectSelectChange(): void {
    this.isChattingWithAdmin = false; // Stop admin chat if project selected
    if (this.selectedChatProjectId) {
      // Logic to load existing messages for this project chat
      this.loadChatMessagesForProject(this.selectedChatProjectId);
    } else {
      this.chatMessages = []; // Clear messages if no project selected
    }
  }

  startAdminChat(): void {
    this.selectedChatProjectId = null; // Clear project selection if starting admin chat
    this.isChattingWithAdmin = true;
    // Logic to load existing messages for admin chat
    this.loadChatMessagesForAdmin();
  }

  loadChatMessagesForProject(projectId: number): void {
    this.isLoadingChatMessages = true;
    this.chatMessages = []; // Clear previous messages
    // This will be an API call to your backend to fetch messages for this project
    // Example: this.chatService.getProjectMessages(projectId).subscribe(...)
    // For now, simulate:
    setTimeout(() => {
      this.chatMessages = [
        { id: '1', senderId: 999, senderUsername: 'Technicien-1', projectId: projectId, isAdminChat: false, content: 'Bonjour! Comment puis-je vous aider avec ce projet?', timestamp: new Date().toISOString() },
        { id: '2', senderId: this.currentUserId!, senderUsername: this.currentUserUsername!, projectId: projectId, isAdminChat: false, content: 'J\'ai un problème avec la configuration du capteur.', timestamp: new Date().toISOString() },
        { id: '3', senderId: 999, senderUsername: 'Technicien-1', projectId: projectId, isAdminChat: false, content: 'Pouvez-vous me donner plus de détails sur le modèle de capteur?', timestamp: new Date().toISOString() },
      ];
      this.isLoadingChatMessages = false;
      this.scrollToBottom();
    }, 1000);
  }

  loadChatMessagesForAdmin(): void {
    this.isLoadingChatMessages = true;
    this.chatMessages = []; // Clear previous messages
    // This will be an API call to your backend to fetch messages for admin chat
    // Example: this.chatService.getAdminMessages(this.currentUserId).subscribe(...)
    // For now, simulate:
    setTimeout(() => {
      this.chatMessages = [
        { id: 'a1', senderId: 1000, senderUsername: 'Admin', isAdminChat: true, content: 'Bonjour! Comment puis-je vous aider?', timestamp: new Date().toISOString() },
        { id: 'a2', senderId: this.currentUserId!, senderUsername: this.currentUserUsername!, isAdminChat: true, content: 'J\'ai une question sur ma facturation.', timestamp: new Date().toISOString() },
      ];
      this.isLoadingChatMessages = false;
      this.scrollToBottom();
    }, 1000);
  }


  selectedChatProjectId: number | null = null;
  isChattingWithAdmin: boolean = false;
  chatMessages: ChatMessage[] = [];
  newMessagee: string = '';
  isLoadingChatMessages: boolean = false;

  // For scrolling to bottom of chat
  @ViewChild('chatScrollContainer') private chatScrollContainer!: ElementRef;

  sendMessage(): void {
    if (!this.newMessagee.trim() || (!this.selectedChatProjectId && !this.isChattingWithAdmin)) {
      return; // Don't send empty messages or if no chat selected
    }

    const message: ChatMessage = {
      id: 'msg-' + Date.now(), // Temporary ID for display
      senderId: this.currentUserId!,
      senderUsername: this.currentUserUsername!,
      projectId: this.selectedChatProjectId || undefined, // undefined if admin chat
      isAdminChat: this.isChattingWithAdmin,
      content: this.newMessagee.trim(),
      timestamp: new Date().toISOString()
    };

    this.chatMessages.push(message);
    this.newMessagee = ''; // Clear input field
    this.scrollToBottom();

    // In a real application, you'd send this message via a WebSocket service:
    // this.chatService.sendMessage(message);
    console.log('Message sent:', message);
  }

  private scrollToBottom(): void {
    try {
      // Use setTimeout to ensure DOM is updated before scrolling
      setTimeout(() => {
        if (this.chatScrollContainer && this.chatScrollContainer.nativeElement) {
          this.chatScrollContainer.nativeElement.scrollTop = this.chatScrollContainer.nativeElement.scrollHeight;
        }
      }, 0);
    } catch (err) {
      console.error('Could not scroll to bottom:', err);
    }
  }



}