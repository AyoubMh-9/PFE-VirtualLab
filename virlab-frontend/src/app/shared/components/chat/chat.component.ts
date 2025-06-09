import { Component, OnInit , Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService } from '../../../core/services/chat.service';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.css'
})
export class ChatComponent implements OnInit {

  @Input() clientId!: number;
  @Input() productId!: number;
  @Input() currentUserId!: number;
  messages: any[] = [];
  newMessage = '';

  constructor(private messageService: ChatService, private route: ActivatedRoute) {}

  getUserIdFromToken(): number | null {
    console.log('=== getUserIdFromToken ===');
    const token = localStorage.getItem('token');
    console.log('Token présent:', !!token);
    
    if (!token) {
      console.log('Aucun token');
      return null;
    }

    try {
      const payloadBase64 = token.split('.')[1];
      if (!payloadBase64) {
        console.log('Payload manquant');
        return null;
      }

      const payloadJson = atob(payloadBase64);
      const payload = JSON.parse(payloadJson);
      console.log('Payload token:', payload);

      const userId = payload.sub || payload.userId;
      console.log('User ID extrait:', userId);
      
      return userId || null;
    } catch (error) {
      console.error('Erreur parsing token:', error);
      return null;
    }
  }

  ngOnInit() {
    console.log('=== ChatComponent ngOnInit ===');
    
    this.clientId = Number(this.route.snapshot.paramMap.get('clientId'));
    this.productId = Number(this.route.snapshot.paramMap.get('productId'));
    console.log('Paramètres URL:', { clientId: this.clientId, productId: this.productId });

    this.currentUserId = this.getUserIdFromToken() ?? 0;
    console.log('Current User ID:', this.currentUserId);

    if (!this.clientId || !this.productId) {
      console.error('ClientId ou ProductId manquant dans l\'URL');
      return;
    }

    if (!this.currentUserId) {
      console.error('User non connecté');
      return;
    }

    console.log('Appel loadMessages...');
    this.loadMessages();
  }

  loadMessages() {
    console.log('=== loadMessages ===');
    console.log('Paramètres API:', {
      currentUserId: this.currentUserId,
      clientId: this.clientId,
      productId: this.productId
    });

    this.messageService.getConversation(this.currentUserId, this.clientId, this.productId)
      .subscribe({
        next: (data) => {
          console.log('Messages reçus:', data);
          this.messages = data;
        },
        error: (error) => {
          console.error('Erreur API getConversation:', error);
          console.log('Status de l\'erreur:', error.status);
          console.log('Message d\'erreur:', error.message);
          
          if (error.status === 401) {
            console.log('🚨 ERREUR 401 - C\'est probablement ici que la redirection se produit !');
          }
        }
      });
  }
/*
  sendMessage() {
    if (this.newMessage.trim() !== '') {
      const message = {
        content: this.newMessage,
        senderId: this.currentUserId,
        receiverId: this.clientId,
        productId: this.productId
      };
      this.messageService.sendMessage(message).subscribe(() => {
        this.newMessage = '';
        this.loadMessages();
      });
    }
  }
*/
  markAsRead(messageId: number) {
    this.messageService.markAsRead(messageId).subscribe(() => {
      this.loadMessages();
    });
  }
}