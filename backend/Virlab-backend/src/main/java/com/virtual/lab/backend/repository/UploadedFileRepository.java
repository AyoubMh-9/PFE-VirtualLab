package com.virtual.lab.backend.repository;

import com.virtual.lab.backend.model.UploadedFile;
import com.virtual.lab.backend.model.Product;
import com.virtual.lab.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {

    // Trouver les fichiers par utilisateur
    List<UploadedFile> findByUploader(User uploader);

    // Trouver les fichiers par projet
    List<UploadedFile> findByProject(Product project);

    // Trouver les fichiers par utilisateur et projet
    List<UploadedFile> findByUploaderAndProject(User uploader, Product project);



    List<UploadedFile> findAll();

    /**
     * Counts the total number of UploadedFile entities associated with projects
     * belonging to a specific client.
     *
     * This query navigates from UploadedFile through its Product to the User (client).
     * It assumes the following relationships in your JPA entities:
     * - UploadedFile entity has a many-to-one relationship to Product (e.g., `private Product project;`)
     * - Product entity has a many-to-one relationship to User (Client) (e.g., `private User client;`)
     *
     * @param clientId The ID of the client whose projects' uploaded files should be counted.
     * @return The total count of uploaded files for the given client's projects.
     */
    @Query("SELECT COUNT(uf) FROM UploadedFile uf JOIN uf.project p JOIN p.client c WHERE c.id = :clientId")
    Long countAllUploadedFilesByClientId(@Param("clientId") Long clientId);


}
