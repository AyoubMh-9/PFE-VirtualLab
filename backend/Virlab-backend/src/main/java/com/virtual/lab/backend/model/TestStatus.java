package com.virtual.lab.backend.model;

public enum TestStatus {
    EN_ATTENTE, // Pending
    EN_COURS,   // In Progress
    RÉUSSI,     // Passed (for individual tests)
    ÉCHOUÉ,     // Failed (for individual tests)
    TERMINÉ     // Completed (for groups/products, implies all sub-items are RÉUSSI/ÉCHOUÉ)
}
