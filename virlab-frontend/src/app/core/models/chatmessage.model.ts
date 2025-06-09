export interface ChatMessage {
  senderName: string;
  text: string;
  timestamp?: string;
  senderAvatarUrl?: string;
  isFile?: boolean;
  fileUrl?: string;
}
