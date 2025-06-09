export interface User {
  id: number;
  username: string;
  email: string;
}

export interface Produit {
  id: number;
  nomProduct: string;
  accesscode?: string;
  description?: string;
  status: string;
  client?: User;
  technician?: User;
}
export interface ProductDTO {
  id: number;
  nomProduct: string;
  description: string;
  accesscode: string;
  status: string;
  client?: { // Optional, based on your DTO structure
    id: number;
    username: string;
    email: string;
  };
  technician?: { // Optional, based on your DTO structure
    id: number;
    username: string;
    email: string;
  };
  testGroups: TestGroupDTO[];
  // accessCodeUsed?: boolean; // If this field exists and is needed
}
export interface TestGroupDTO {
  id: number;
  groupNumber?: number; // Added groupNumber based on your JSON
  productId?: number; // Added productId
  tests: TestDTO[];
  // You might have other fields like nomGroupe, description, creationDate if needed for display
  nomGroupe?: string; // Added from your backend DTO structure
  description?: string; // Added from your backend DTO structure
  creationDate?: string; // Added from your backend DTO structure
}

export interface TestDTO {
  id: number;
  name: string; // Matches 'name' from your JSON
  connectors: string;
  standardsComments: string;
  testToDo: string;
  typeOfTest: string; // Matches 'typeOfTest' from your JSON
  testGroupId?: number; // Added testGroupId
  result?: TestResultDTO; // Assuming a nested result object if applicable
}

export interface TestResultDTO {
  status: string;
  score: number;
  date: string;
  technicianComments?: string;
}

export interface User {
  id: number;
  username: string;
  email: string;
}

export interface Produit {
  id: number;
  nomProduct: string;
  accesscode?: string;
  description?: string;
  status: string;
  client?: User;
  technician?: User;
}
// src/app/models/product.model.ts (or interfaces)

export enum TestStatus {
  EN_ATTENTE = 'EN_ATTENTE',
  EN_COURS = 'EN_COURS',
  RÉUSSI = 'RÉUSSI',
  ÉCHOUÉ = 'ÉCHOUÉ',
}

export interface TestResponseDTO {
  id: number;
  name: string;
  status: TestStatus;
  score: number;
  connectors: string;
  testToDo: string;
  typeOfTest: string;
  standardsComments: string;
}

export interface TestGroupResponseDTO {
  id: number;
  groupNumber: number;
  calculatedStatus: TestStatus;
  calculatedProgress: number; // Percentage, e.g., 75.0
  tests: TestResponseDTO[]; // Array of nested tests
}

export interface ProductResponseDTO {
  id: number;
  nomProduct: string;
  description: string;
  calculatedStatus: TestStatus;
  calculatedProgress: number; // Percentage, e.g., 50.0
  createdAt: String; // Or string, if your backend returns ISO string
  accessCode: string;
  testGroups: TestGroupResponseDTO[]; // Array of nested test groups
  totalTestsCount : number;
}

// For product creation, if you have a separate DTO for it
export interface ProductCreationRequest {
  nomProduct: string;
  description: string;
  technicienId?: number; // Optional
  clientId?: number; // Optional
  accessCode?: string; // Optional
}

// For product update, if you create a separate DTO for it
export interface ProductUpdateRequest {
  nomProduct?: string;
  description?: string;
  calculatedStatus?: TestStatus;
  technicianId?: number;
  clientId?: number;
  // Don't include calculatedProgress, createdAt, accessCode directly here, as they are usually backend-managed
}
  
 // app/core/models/produit.model.ts (or product.model.ts)

export interface UserData { // A simple interface for client/technician info
  id: number;
  username: string;
  email?: string; // Optional if not always present
}

export interface Produitnew {
  id: number;
  nomProduct: string;
  description: string;
  status: string; // Assuming TestStatus maps directly to a string
  accessCode?: string; // Corrected to accessCode with capital 'C'
  client?: UserData; // Optional, as it might be null
  technician?: UserData; // Optional, as it might be null
  // Add other properties like calculatedProgress, createdAt, testGroups, etc.
  calculatedStatus: string; // Assuming this is also a string
  calculatedProgress?: number;
  createdAt?: string; // Or Date, depending on how you parse it
  testGroups?: any[]; // Define a proper interface for TestGroupResponseDTO if needed
  totalTestsCount?: number;
  totalFilesCount?: number;
}