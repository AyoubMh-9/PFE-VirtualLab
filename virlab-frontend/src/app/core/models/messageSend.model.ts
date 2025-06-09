export interface MessageSendDto {
  content: string;
  senderId: number;
  receiverId?: number;
  productId: number;
}
