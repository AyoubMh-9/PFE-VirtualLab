export interface Message {
  id: number;
  content: string;
  senderUsername: string;
  senderRole: string;
  receiverUsername: string;
  productName: string;
  timestamp: string;
  read: boolean;
  senderAvatarUrl?: string;
}
