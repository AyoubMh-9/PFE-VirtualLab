import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Message } from '../../../app/core/models/message.model'
import { MessageSendDto } from '../../../app/core/models/messageSend.model';

@Injectable({ providedIn: 'root' })
export class ChatService {
  private apiUrl = 'http://localhost:8080/api/messages'; // change si besoin

  constructor(private http: HttpClient) { }

  sendMessage(message: MessageSendDto):  Observable<Message> {
    return this.http.post<Message>(this.apiUrl, message);
  }

  getMessagesByProduct(productId: number, userId: number): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.apiUrl}/product/${productId}`, {
      params: new HttpParams().set('userId', userId.toString())
    });
  }

  getConversation(userId1: number, userId2: number, productId: number): Observable<Message[]> {
    const params = new HttpParams()
      .set('userId1', userId1.toString())
      .set('userId2', userId2.toString())
      .set('productId', productId.toString());

    return this.http.get<Message[]>(`${this.apiUrl}/conversation`, { params });
  }

  markAsRead(messageId: number): Observable<Message> {
    return this.http.put<Message>(`${this.apiUrl}/${messageId}/read`, {});
  }

  countUnreadMessages(userId: number): Observable<{ count: number }> {
    return this.http.get<{ count: number }>(`${this.apiUrl}/unread/count`, {
      params: new HttpParams().set('userId', userId.toString())
    });
  }
}
