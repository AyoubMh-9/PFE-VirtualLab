import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Produit, Produitnew } from '../../../../../app/core/models/produit.model';
import { ProduitService } from '../../../../core/services/product.service';
import { UserService } from '../../../../core/services/user.service';
import { ChatService } from '../../../../core/services/chat.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { Message } from '../../../../core/models/message.model';
import { MessageSendDto } from '../../../../../app/core/models/messageSend.model';
import { ChatMessage } from '../../../../core/models/chatmessage.model'; 
import { trigger, state, style, animate, transition } from '@angular/animations';

interface ExtendedChatMessage extends ChatMessage {
  id?: string;
  status?: 'sent' | 'delivered' | 'read';
  isFile?: boolean;
  fileName?: string;
  fileSize?: string;
  fileUrl?: string;
  senderAvatarUrl?: string;
}


@Component({
  selector: 'app-project-list',
  imports: [CommonModule,  FormsModule],
  templateUrl: './project-list.component.html',
  styleUrl: './project-list.component.css',
  animations: [ // <-- Add this animations array
    trigger('messageAnimation', [
      transition(':enter', [ // Animation when an element enters the DOM
        style({ opacity: 0, transform: 'translateY(20px)' }),
        animate('0.3s ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ]),
      // You could add :leave for messages disappearing, but usually not needed for chat
    ]),
    // You could also add a trigger for the whole chat panel if not using direct CSS animation
    // trigger('chatPanelAnimation', [
    //   transition(':enter', [
    //     style({ opacity: 0, transform: 'scale(0.95) translateY(20px)' }),
    //     animate('0.3s ease-out', style({ opacity: 1, transform: 'scale(1) translateY(0)' }))
    //   ]),
    //   transition(':leave', [
    //     animate('0.2s ease-in', style({ opacity: 0, transform: 'scale(0.95) translateY(20px)' }))
    //   ])
    // ])
  ]
})
export class ProjectListComponent implements OnInit{

  projects : Produitnew[] = [];
  isLoading = true;
  error = '';
  isChatPanelVisible = false;
  searchTerm: string = '';
  filteredProjects: Produitnew[] = [];

  constructor(private produitService: ProduitService, private userService: UserService, private router: Router,private chatService: ChatService) {}

currentUserId: number | null = null;

currentUserName: string | null = null;

  ngOnInit(): void {
    this.loadProjects();
    this.loadTechniciens();
    //this.filteredProjects = this.projects;
    //this.updatePaginatedProjects();
    const username = this.userService.getCurrentUsername();
  if (username) {
    this.currentUserName = username;
    this.userService.getUserIdByUsername(username).subscribe({
      next: (id) => {
        this.currentUserId = id;
        this.loadChatMessages(); // Charge les messages après avoir l'ID
      },
      error: () => {
        alert("Impossible de récupérer l'utilisateur connecté");
      }
    });
  } else {
    alert("Utilisateur non connecté");
  }
  }

  loadProjects() {
  this.produitService.getProduitsDuClient().subscribe({
    next: (data) => {
      console.log('Projects reçus :', data); 
      this.projects = data;
      this.filteredProjects = data;
      this.updatePaginatedProjects();
      this.isLoading = false;
      console.log('the new listes Project : ', this.projects);
    },
    error: (err) => {
      console.error('Erreur de chargement :', err); 
      this.error = 'Erreur lors du chargement des projets';
      this.isLoading = false;
    }
  });
}


filterProjects() {
  const term = this.searchTerm.toLowerCase();

  this.filteredProjects = this.projects.filter(project =>
    project.nomProduct.toLowerCase().includes(term) ||
    project.accessCode?.toLowerCase().includes(term) ||
    project.description?.toLowerCase().includes(term) ||
    project.client?.username.toLowerCase().includes(term) ||
    project.technician?.username.toLowerCase().includes(term) 
    
  );

  // Revenir à la page 1 à chaque recherche
  this.currentPage = 1;
  this.updatePaginatedProjects();
}

pageSize: number = 5;            // Projets par page
currentPage: number = 1;
paginatedProjects: Produitnew[] = [];

updatePaginatedProjects() {
  const start = (this.currentPage - 1) * this.pageSize;
  const end = start + this.pageSize;
  this.paginatedProjects = this.filteredProjects.slice(start, end);
}

goToPage(page: number) {
  this.currentPage = page;
  this.updatePaginatedProjects();
}

nextPage() {
  if (this.currentPage < this.totalPages) {
    this.currentPage++;
    this.updatePaginatedProjects();
  }
}

 onInputKeydown(event: KeyboardEvent) {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }


prevPage() {
  if (this.currentPage > 1) {
    this.currentPage--;
    this.updatePaginatedProjects();
  }
}

get totalPages(): number {
  return Math.ceil(this.filteredProjects.length / this.pageSize);
}



 // Variables
isEditModalOpen: boolean = false;
selectedProject: any = { technicianId: null }; // Projet à modifier
techniciens: any[] = [];   // Liste des techniciens (charge-la depuis ton backend)
testStatusOptions = [
  { value: 'EN_ATTENTE', label: 'En attente' },
  { value: 'EN_COURS', label: 'En cours' },
  { value: 'RÉUSSI', label: 'Réussi' }, // Add if you want to allow this status
  { value: 'TERMINÉ', label: 'Terminé' }, // <-- CORRECTED: Added accent to 'E'
  { value: 'ÉCHOUÉ', label: 'Échoué' } // Add if you want to allow this status
];


// Méthode pour ouvrir le modal
editProject(project: any) {
  this.selectedProject = { ...project }; // Clone du projet (évite de modifier direct la liste)
  this.isEditModalOpen = true;
}

// Fermer le modal
closeModal() {
  this.isEditModalOpen = false;
}

// Mettre à jour le projet


// In your Angular component's updateProject() method:

updateProject() {
  Swal.fire({
    title: 'Êtes-vous sûr ?',
    text: 'Voulez-vous vraiment mettre à jour ce projet ?',
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: 'Oui, mettre à jour',
    cancelButtonText: 'Annuler',
  }).then((result) => {
    if (result.isConfirmed) {
      // 1. Prepare the base data for update (always sent)
      const updatedData: any = {
        nomProduct: this.selectedProject.nomProduct,
        description: this.selectedProject.description,
        calculatedStatus: this.selectedProject.calculatedStatus,
      };

      // 2. Add optional fields only if they have values or are explicitly being set to null
      //    This matches the backend's `if (updatedProductDetails.get...() != null)` behavior.

      // Optional: calculatedStatus
      // If the user selected a status, send it. If it's the original (unchanged) it's fine.
      // If your UI allows explicitly clearing status, you could send null.
      if (this.selectedProject.calculatedStatus !== undefined && this.selectedProject.calculatedStatus !== null) {
          updatedData.calculatedStatus = this.selectedProject.calculatedStatus;
      }
      // If calculatedStatus is null, the backend will leave it untouched (due to the `if (null != null)` check being false)

      // Optional: accessCode
      // Send accessCode if it exists on the selected project, otherwise omit.
      // If accessCode can be cleared, ensure you send null for it when cleared.
      if (this.selectedProject.accessCode !== undefined) { // Check if it's explicitly undefined or null
          updatedData.accessCode = this.selectedProject.accessCode;
      }
      // If accessCode is null, the backend will leave it untouched (due to the `if (null != null)` check being false)


      // 3. Handle Technician Data (Optional Update)
      // We need to send the technician object if:
      //   a) A new technician was selected (technicianId is set from dropdown)
      //   b) An existing technician should be preserved (selectedProject.technician.id exists and no new selection)
      //   c) The technician is explicitly unassigned (user selected a "None" option, making technicianId null)
      // If 'technician' is just omitted from the UI and you want to preserve the existing, then omit it from payload.
      // However, it's generally clearer to explicitly send the existing ID if the user didn't change it.

      // Logic for `technician` field in payload:
      if (this.selectedProject.technicianId !== undefined && this.selectedProject.technicianId !== null) {
        // User selected a technician from dropdown (new or existing)
        updatedData.technician = { id: this.selectedProject.technicianId };
      } else if (this.selectedProject.technician && this.selectedProject.technician.id) {
        // No new selection, but project already had a technician. Keep existing.
        updatedData.technician = { id: this.selectedProject.technician.id };
      } else if (this.selectedProject.technicianId === null) {
          // Explicitly set to null if the user chose to unassign (e.g., selected "None" option)
          updatedData.technician = null;
      }
      // ELSE (if `selectedProject.technicianId` is undefined and `selectedProject.technician` is null/undefined)
      // The 'technician' field will be omitted from `updatedData`. The backend will then preserve the existing.


      // 4. Client Data (DO NOT SEND)
      // Since the backend method explicitly ignores the client, do NOT include 'client' in this payload.
      // This ensures clean separation of concerns and avoids sending unnecessary data.

      console.log('Sending updated data:', updatedData); // Crucial for debugging

      this.produitService.updateProject(this.selectedProject.id, updatedData).subscribe({
        next: (res) => {
          Swal.fire({
            icon: 'success',
            title: 'Succès',
            text: 'Le projet a été mis à jour avec succès !',
          });
          this.loadProjects(); // Reload projects to reflect changes from backend
          this.closeModal();
        },
        error: (err) => {
          console.error('Erreur lors de la mise à jour :', err);
          Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: "Une erreur est survenue lors de la mise à jour.",
          });
        },
      });
    } else {
      Swal.fire({
        icon: 'info',
        title: 'Annulé',
        text: 'La mise à jour a été annulée.',
      });
    }
  });
}






// Charger la liste des techniciens au démarrage (par exemple dans ngOnInit)

loadTechniciens(): Promise<any> {
  return new Promise((resolve) => {
    this.userService.getAllTechnicians().subscribe((data) => {
      this.techniciens = data;
      resolve(true);
    });
  });
}




deleteProject(project: any) {
  Swal.fire({
    title: '⚠️ Êtes-vous sûr ?',
    text: `Le projet "${project.nomProduct}" sera définitivement supprimé.`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '✅ Oui, supprimer',
    cancelButtonText: '❌ Annuler',
    confirmButtonColor: '#d33',
    cancelButtonColor: '#3085d6',
  }).then((result) => {
    if (result.isConfirmed) {
      this.produitService.deleteProject(project.id).subscribe({
        next: () => {
          Swal.fire('✅ Supprimé !', 'Le projet a été supprimé avec succès.', 'success');
          this.loadProjects();
        },
        error: (err) => {
          console.error('Erreur lors de la suppression', err);
          Swal.fire('❌ Erreur', 'Impossible de supprimer le projet.', 'error');
        }
      });
    }
  });
}
/*
openChat(clientId: number | undefined, productId: number | undefined) {
  if (clientId && productId) {
    console.log(clientId , productId);
    const route = `/admin/chat/${clientId}/${productId}`;
    console.log('Navigation vers:', route);
    this.router.navigate(['/admin/chat', clientId, productId]);
  } else {
    alert("Ce projet n'a pas de client assigné.");
  }
}
*/
// Variables pour chat modal
isChatModalOpen = false;
chatClientId: number | null = null;
chatProjectId: number | null = null;
chatClientName: string | null = null;

//chatMessages: { senderName: string; text: string; timestamp?: string; senderAvatarUrl?: string; }[] = [];
chatMessages: ChatMessage[] = [];
newMessage = '';
noClientAssociated: boolean = false;

openChat(clientId: number | undefined| null, projectId: number) {
  if (!clientId) {
    //alert("Aucun client associé au projet !");
    this.noClientAssociated = true;

  // Afficher une alerte avec SweetAlert2
  Swal.fire({
    icon: 'warning',
    title: 'Aucun client associé',
    text: 'Veuillez sélectionner un client avant de continuer.',
    confirmButtonText: 'OK'
  });
    return;
  }

  this.chatClientId = clientId;
  this.chatProjectId = projectId;
  this.chatClientName = 'Client ' + clientId; // Tu peux récupérer le nom réel via API si besoin
  this.isChatModalOpen = true;

  this.isChatPanelVisible = true;
  this.loadChatMessages();
}

closeChat() {
  //this.isChatModalOpen = false;
  this.isChatPanelVisible = false;
  this.chatMessages = [];
  this.newMessage = '';

}

loadChatMessages() {
  if (this.chatClientId && this.chatProjectId) {
    this.chatService.getMessagesByProduct(this.chatProjectId, this.chatClientId).subscribe({
      next: (messages) => {
        console.log('Messages reçus:', messages);
        this.chatMessages = messages.map(m => ({
          senderName: `${m.senderUsername} (${m.senderRole})`,
          text: m.content,
          timestamp: m.timestamp,
          read: m.read
        }));
      },
      error: () => {
        this.chatMessages = [{ senderName: 'System', text: 'Erreur lors du chargement des messages' }];
      }
    });
  }
}






sendMessage() {
  if (!this.newMessage.trim()) return;

  if (!this.chatClientId || !this.chatProjectId) {
    alert("Impossible d'envoyer le message : destinataire ou projet manquant.");
    return;
  }

  const username = this.userService.getCurrentUsername();
  if (!username) {
    alert("Impossible d'envoyer le message : utilisateur non connecté.");
    return;
  }

  // Récupérer l'ID à partir du username
  this.userService.getUserIdByUsername(username).subscribe({
    next: (userId) => {
      const messageToSend: MessageSendDto = {
        content: this.newMessage.trim(),
        senderId: userId,
        receiverId: this.chatClientId!,
        productId: this.chatProjectId!
      };

      this.chatService.sendMessage(messageToSend).subscribe({
        next: (savedMessage) => {
          this.chatMessages.push({
            senderName: `${savedMessage.senderUsername} (${savedMessage.senderRole})`,
            text: savedMessage.content,
            timestamp: savedMessage.timestamp
          });
          this.newMessage = '';
        },
        error: () => {
          alert("Erreur lors de l'envoi du message");
        }
      });
    },
    error: () => {
      alert("Impossible de récupérer l'ID de l'utilisateur");
    }
  });
}



startAudioCall() {
  Swal.fire({
    title: 'Appel Vocal',
    text: 'Fonctionnalité en cours de développement.',
    icon: 'info',
    confirmButtonText: 'OK'
  }).then(result => {
    if (result.isConfirmed) {
      // Logique réelle d’appel vocal ici (WebRTC, signalisation, etc)
      console.log('Lancement de l’appel vocal');
      // Exemple : ouvrir une fenêtre / un composant dédié pour l'appel audio
    }
  });
}

startVideoCall() {
  Swal.fire({
    title: 'Appel Vidéo',
    text: 'Fonctionnalité en cours de développement.',
    icon: 'info',
    confirmButtonText: 'OK'
  }).then(result => {
    if (result.isConfirmed) {
      // Logique d’appel vidéo ici (WebRTC, signalisation, etc)
      console.log('Lancement de l’appel vidéo');
      // Ouvrir un composant dédié ou une modale pour la vidéo
    }
  });
}

onFileSelected(event: any) {
  const file: File = event.target.files[0];
  if (file) {
    // Simuler un upload de fichier (à remplacer par vrai upload HTTP)
    this.uploadFile(file).then(url => {
      // Ajouter un message avec un lien vers le fichier uploadé
      this.chatMessages.push({
        senderName: this.currentUserName!,
        text: `Fichier envoyé : <a href="${url}" target="_blank">${file.name}</a>`,
        timestamp: new Date().toISOString(),
        senderAvatarUrl: 'https://example.com/chemin-de-ton-avatar.jpg',
        isFile: true,
        fileUrl: url
      });
    }).catch(err => {
      Swal.fire('Erreur', 'Échec de l’envoi du fichier', 'error');
    });
  }
}

// Méthode mock d’upload de fichier
uploadFile(file: File): Promise<string> {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      // Simuler URL upload
      const fakeUrl = 'https://example.com/uploads/' + encodeURIComponent(file.name);
      resolve(fakeUrl);
    }, 1000);
  });
}

  
/*
onFileSelected(event: any){
  console.log(event);
}
*/

showMessaging: boolean = false; // false = on affiche les projets par défaut

  toggleMessaging() {
    this.showMessaging = !this.showMessaging;
  }


  getStatusClass(status: string) {
  if (!status) {
    return 'status-unknown';
  }
  switch (status.toUpperCase()) {
    case 'TERMINÉ':
      return 'status-done';
    case 'EN_COURS':
      return 'status-inprogress';
    case 'EN_ATTENTE':
      return 'status-pending';
    default:
      return 'status-unknown';
  }
}
chatMessagess: ExtendedChatMessage[] = [];
  newMessages = '';

trackByMessageId(index: number, message: ExtendedChatMessage): string {
    return message.id || index.toString();
  }

  getDefaultAvatar(senderName: string): string {
    // Générer un avatar par défaut basé sur le nom
    const colors = ['#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', '#FFEAA7', '#DDA0DD'];
    const colorIndex = senderName.length % colors.length;
    const initial = senderName.charAt(0).toUpperCase();
    
    // Retourner une URL d'avatar généré ou une image par défaut
    return `https://ui-avatars.com/api/?name=${initial}&background=${colors[colorIndex].substring(1)}&color=fff&size=45`;
  }

  onAvatarError(event: any) {
    event.target.src = 'https://ui-avatars.com/api/?name=U&background=667eea&color=fff&size=45';
  }

  getStatusClassmessage(status?: string): string {
  switch (status) {
    case 'sent': return 'status-sent';
    case 'delivered': return 'status-delivered';
    case 'read': return 'status-read';
    default: return 'status-sent'; // classe par défaut
  }
}

getStatusIcon(status?: string): string {
  switch (status) {
    case 'sent': return '✓';
    case 'delivered': return '✓✓';
    case 'read': return '✓✓';
    default: return '✓'; // icône par défaut
  }
}

getStatusTitle(status?: string): string {
  switch (status) {
    case 'sent': return 'Envoyé';
    case 'delivered': return 'Livré';
    case 'read': return 'Lu';
    default: return 'Envoyé'; // titre par défaut
  }
}

isTyping = false;
  typingUser = '';
  typingTimeout: any;

  onTyping() {
    if (!this.isTyping) {
      this.isTyping = true;
      // Ici tu peux envoyer un signal via WebSocket que l'utilisateur tape
      // this.chatService.sendTypingSignal(this.chatClientId, this.currentUserId);
    }


  }

  scrollToBottom(): void {
    try {
      this.chatMessagesContainer.nativeElement.scrollTop = this.chatMessagesContainer.nativeElement.scrollHeight;
    } catch (err) {
      console.error('Error scrolling to bottom:', err);
    }
  }
@ViewChild('chatMessagesContainer') private chatMessagesContainer!: ElementRef;

  // Add properties for avatars (you'll need to set these from your data)
  chatClientAvatarUrl: string = 'assets/default-avatar.png'; // Placeholder
  currentUserAvatarUrl: string = 'assets/my-default-avatar.png'; // Placeholder
  ngAfterViewChecked() {
    this.scrollToBottom(); // Scroll to bottom after view updates
  }

}



