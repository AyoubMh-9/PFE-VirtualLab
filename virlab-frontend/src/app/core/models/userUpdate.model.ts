export interface UserUpdateDTO {
  id: number;           // Indispensable pour update
  username: string;
  email: string;
  password?: string;    // optionnel (peut être vide pour ne pas modifier)
}
