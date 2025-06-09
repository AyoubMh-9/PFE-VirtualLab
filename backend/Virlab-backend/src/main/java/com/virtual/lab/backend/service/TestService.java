package com.virtual.lab.backend.service;

import com.virtual.lab.backend.dto.TestDto;
import com.virtual.lab.backend.model.Test;
import com.virtual.lab.backend.model.TestGroup;
import com.virtual.lab.backend.model.TestStatus;
import com.virtual.lab.backend.model.User;
import com.virtual.lab.backend.repository.TestGroupRepository;
import com.virtual.lab.backend.repository.TestRepository;
import com.virtual.lab.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestGroupRepository testGroupRepository; // To link tests to groups

    @Autowired
    private UserRepository userRepository;

    // Helper to convert Entity to DTO
    public TestDto convertToDto(Test test) {
        if (test == null) return null;
        TestDto dto = new TestDto();
        dto.setId(test.getId());
        dto.setName(test.getName());
        dto.setConnectors(test.getConnectors());
        dto.setStandardsComments(test.getStandardsComments());
        dto.setTestToDo(test.getTestToDo());
        dto.setTypeOfTest(test.getTypeOfTest());
        if (test.getTestGroup() != null) {
            dto.setTestGroupId(test.getTestGroup().getId());
        }
        return dto;
    }

    // Helper to convert DTO to Entity
    public Test convertToEntity(TestDto dto) {
        Test test = new Test();
        test.setName(dto.getName());
        test.setConnectors(dto.getConnectors());
        test.setStandardsComments(dto.getStandardsComments());
        test.setTestToDo(dto.getTestToDo());
        test.setTypeOfTest(dto.getTypeOfTest());
        // TestGroup needs to be fetched and set
        if (dto.getTestGroupId() != null) {
            TestGroup testGroup = testGroupRepository.findById(dto.getTestGroupId())
                    .orElseThrow(() -> new EntityNotFoundException("TestGroup not found with ID: " + dto.getTestGroupId()));
            test.setTestGroup(testGroup);
        }
        return test;
    }

    // CRUD Operations

    @Transactional
    public TestDto createTest(TestDto testDto) {
        if (testDto.getTestGroupId() == null) {
            throw new IllegalArgumentException("Test must be associated with a TestGroup.");
        }
        TestGroup testGroup = testGroupRepository.findById(testDto.getTestGroupId())
                .orElseThrow(() -> new EntityNotFoundException("TestGroup not found with ID: " + testDto.getTestGroupId()));

        Test test = convertToEntity(testDto);
        test.setTestGroup(testGroup); // Explicitly link

        // Add to test group's list if using bidirectional
        testGroup.addTest(test); // This handles the bidirectional link

        Test savedTest = testRepository.save(test);
        testGroupRepository.save(testGroup); // Save group to persist relationship if cascade isn't full on group side

        return convertToDto(savedTest);
    }

    @Transactional(readOnly = true)
    public Optional<TestDto> getTestById(Long id) {
        return testRepository.findById(id).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<TestDto> getAllTests() {
        return testRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TestDto> getTestsByTestGroupId(Long testGroupId) {
        return testRepository.findByTestGroupId(testGroupId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates the status and optionally the score of a Test, then recalculates
     * the status and progress of its parent TestGroup and Product.
     * The score for the Test is automatically set based on the new status
     * if no explicit score is provided.
     *
     * @param testId The ID of the test to update.
     * @param newStatus The new status for the test.
     * @param score An optional score for the test (can be null).
     * @return The updated Test entity.
     * @throws EntityNotFoundException if the test with the given ID is not found.
     */
    @Transactional
    public Test updateTestStatus(Long testId, TestStatus newStatus, Double score) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new EntityNotFoundException("Test with ID " + testId + " not found."));

        // 1. Update the individual test's status
        test.setStatus(newStatus);

        // 2. Set the test's score based on rules or explicit input
        if (score != null) {
            // If an explicit score is provided, use it
            test.setScore(score);
        } else {
            // Otherwise, set the score automatically based on the status
            switch (newStatus) {
                case EN_ATTENTE:
                    test.setScore(0.0); // No score yet, or default to 0
                    break;
                case EN_COURS:
                    test.setScore(50.0); // In progress, score is 0 until completion
                    break;
                case ÉCHOUÉ:
                    test.setScore(0.0); // Failed, default score is 0
                    break;
                case RÉUSSI:
                    test.setScore(100.0); // Passed, default score is 100 (or whatever max you define)
                    break;
                default:
                    test.setScore(null); // Or keep existing score, or set to 0.0, depending on unhandled statuses
                    break;
            }
        }

        Test updatedTest = testRepository.save(test); // Persist the test's status and score change

        // 3. Recalculate and save the parent TestGroup
        TestGroup testGroup = updatedTest.getTestGroup();
        if (testGroup != null) {
            testGroup.recalculateStatusAndProgress(); // This will also trigger Product's recalculation
            testGroupRepository.save(testGroup); // Persist updated TestGroup
        }

        // The Product's recalculation was triggered by testGroup.recalculateStatusAndProgress()
        // and should be handled by the @Transactional context when testGroup is saved.
        // Explicitly saving the product here might not be necessary if cascades or events are set up correctly,
        // and if TestGroup's save operation properly propagates changes to its parent Product.
        // However, if you observe Product's calculated fields not being updated, you might need to:
        // Product product = testGroup.getProduct();
        // productRepository.save(product);

        return updatedTest;
    }



    @Transactional
    public void deleteTest(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Test not found with ID: " + id));

        // Important: If cascade options in entity are not perfect for bidirectional relationships,
        // manually remove from the parent testGroup's list before deleting
        if (test.getTestGroup() != null) {
            test.getTestGroup().removeTest(test);
            testGroupRepository.save(test.getTestGroup()); // Save parent to reflect removal
        }
        testRepository.delete(test);
    }

/*
    public Long getAllTestsCountForClient(Long clientId) {
        return testRepository.countAllTestsByClient(clientId);
    }
    */


    public Long getAllTestsCountForClient(Long clientId) {
        return testRepository.countAllTestsByClient(clientId);
    }

    // Alternative: If you want to use Authentication directly in the service
    public Long getAllTestsCountForAuthenticatedClient(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return testRepository.countAllTestsByClient(user.getId());
    }

    public Long countSuccessfulTestsForClient(Long clientId) {
        return testRepository.countSuccessfulTestsByClientId(clientId);
    }

}