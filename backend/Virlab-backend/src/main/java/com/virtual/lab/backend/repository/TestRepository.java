package com.virtual.lab.backend.repository;

import com.virtual.lab.backend.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    // Custom queries if needed, e.g., find by test group
    List<Test> findByTestGroupId(Long testGroupId);

    //@Query("SELECT COUNT(t) FROM Test t JOIN t.testGroup tg JOIN tg.product p WHERE p.client.id = :clientId")
    //Long countAllTestsByClient(@Param("clientId") Long clientId);


    /**
     * Counts the total number of Test entities associated with a specific Product (project).
     *
     * This query navigates from the Test entity through its TestGroup to the Product entity.
     * It assumes the following relationships in your JPA entities:
     * - Test entity has a many-to-one relationship to TestGroup (e.g., `private TestGroup testGroup;`)
     * - TestGroup entity has a many-to-one relationship to Product (e.g., `private Product product;`)
     *
     * @param productId The ID of the Product (project) for which to count the tests.
     * @return The total count of tests for the given project ID.
     */
    @Query("SELECT COUNT(t) FROM Test t JOIN t.testGroup tg JOIN tg.product p WHERE p.id = :productId")
    Long countTestsByProductId(@Param("productId") Long productId);

    /**
     * Counts the total number of Test entities associated with projects
     * belonging to a specific client.
     *
     * @param clientId The ID of the client whose projects' tests should be counted.
     * @return The total count of tests for the given client's projects.
     */
    @Query("SELECT COUNT(t) FROM Test t JOIN t.testGroup tg JOIN tg.product p JOIN p.client c WHERE c.id = :clientId")
    Long countAllTestsByClient(@Param("clientId") Long clientId);

    /**
     * Counts the total number of successful Test entities associated with projects
     * belonging to a specific client.
     *
     * @param clientId The ID of the client whose successful tests should be counted.
     * @return The total count of successful tests for the given client's projects.
     */
    @Query("SELECT COUNT(t) FROM Test t JOIN t.testGroup tg JOIN tg.product p JOIN p.client c WHERE c.id = :clientId AND t.status = 'RÉUSSI'") // Assuming status is 'RÉUSSI' (SUCCESS)
    Long countSuccessfulTestsByClientId(@Param("clientId") Long clientId);


}