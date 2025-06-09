package com.virtual.lab.backend.repository;

import com.virtual.lab.backend.model.Product;
import com.virtual.lab.backend.model.TestStatus;
import com.virtual.lab.backend.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    /*
    List<Product> findByClient(User client);
    List<Product> findByClientId(Long clientId);




    Optional<Product> findProductById(Long id);

     */
    Optional<Product> findByAccessCode(String accessCode);
    @Query("SELECT COUNT(p) FROM Product p WHERE p.technician.id = :technicianId AND (p.calculatedStatus = 'EN_COURS')")
    Long countActiveProjectsByTechnician(@Param("technicianId") Long technicianId);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.technician.id = :technicianId AND (p.calculatedStatus = 'EN_ATTENTE')")
    Long countNoActiveProjectsByTechnician(@Param("technicianId") Long technicianId);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.technician.id = :technicianId AND (p.calculatedStatus = 'TERMINE')")
    Long countTermineeProjectsByTechnician(@Param("technicianId") Long technicianId);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.client.id = :clientId AND (p.calculatedStatus = 'EN_COURS')")
    Long countActiveProjectsByClient(@Param("clientId") Long clientId);
/*
    @Query("SELECT p FROM Product p WHERE p.technician.id = :technicianId")
    List<Product> findByTechnicianId(@Param("technicianId") Long technicianId);

 */

    @Override
    @EntityGraph(attributePaths = {"testGroups", "testGroups.tests", "client", "technician"})
    Optional<Product> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"testGroups", "testGroups.tests", "client", "technician"})
    List<Product> findAll();

    // For getAllProjectsForClient
    @EntityGraph(attributePaths = {"testGroups", "testGroups.tests", "client", "technician"})
    List<Product> findByClientId(Long clientId);

    // You might also need methods to find products by their calculated status
    List<Product> findByCalculatedStatus(TestStatus status);



    @EntityGraph(attributePaths = {"testGroups", "testGroups.tests"})
    @Query("SELECT p FROM Product p WHERE p.technician.id = :technicianId")
    List<Product> findByTechnicianId(@Param("technicianId") Long technicianId);


    // ProductRepository.java
// This query uses a LEFT JOIN and GROUP BY to get product details AND the count of related tests
    @Query("SELECT p.id, p.nomProduct, p.description, p.calculatedStatus, p.calculatedProgress, COUNT(t.id) " +
            "FROM Product p " +
            "LEFT JOIN p.testGroups tg " + // Assuming Product has a collection of TestGroup (e.g., @OneToMany)
            "LEFT JOIN tg.tests t " +      // Assuming TestGroup has a collection of Test (e.g., @OneToMany)
            "WHERE p.client.id = :clientId " +
            "GROUP BY p.id, p.nomProduct, p.description, p.calculatedStatus, p.calculatedProgress")
    List<Object[]> findProductsWithTestCountsByClientId(@Param("clientId") Long clientId);

// Then in your ProduitService, you'd iterate over List<Object[]> and manually map to ProductResponseDTO,
// populating totalTestsCount from the COUNT(t.id) result.
// This query won't automatically fetch nested testGroups, so you'd either fetch them separately
// (if still needed) or modify the DTO to not include them if they aren't always needed for this view.

}
