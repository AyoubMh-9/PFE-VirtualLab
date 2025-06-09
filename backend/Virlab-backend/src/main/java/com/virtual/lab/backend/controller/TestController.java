package com.virtual.lab.backend.controller;

import com.virtual.lab.backend.dto.TestDto;
import com.virtual.lab.backend.dto.TestResponseDTO;
import com.virtual.lab.backend.mapper.TestMapper;
import com.virtual.lab.backend.model.Test;
import com.virtual.lab.backend.model.TestStatus;
import com.virtual.lab.backend.model.User;
import com.virtual.lab.backend.service.TestService;
import com.virtual.lab.backend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests") // Or /api/test-groups/{testGroupId}/tests for nested resource
public class TestController {

    @Autowired
    private TestService testService;
    @Autowired
    private TestMapper testMapper;
    @Autowired
    private  UserService userService;

    // CREATE
    @PostMapping("/createTest")
    public ResponseEntity<TestDto> createTest(@RequestBody TestDto testDto) {
        TestDto createdTest = testService.createTest(testDto);
        return new ResponseEntity<>(createdTest, HttpStatus.CREATED);
    }

    // READ (Get by ID)
    @GetMapping("/{id}")
    public ResponseEntity<TestDto> getTestById(@PathVariable Long id) {
        return testService.getTestById(id)
                .map(testDto -> new ResponseEntity<>(testDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // READ (Get all)
    @GetMapping
    public ResponseEntity<List<TestDto>> getAllTests() {
        List<TestDto> tests = testService.getAllTests();
        return new ResponseEntity<>(tests, HttpStatus.OK);
    }

    // READ (Get by TestGroup ID) - Useful for nested retrieval
    @GetMapping("/by-test-group/{testGroupId}")
    public ResponseEntity<List<TestDto>> getTestsByTestGroupId(@PathVariable Long testGroupId) {
        List<TestDto> tests = testService.getTestsByTestGroupId(testGroupId);
        return new ResponseEntity<>(tests, HttpStatus.OK);
    }


    // UPDATE
    /**
     * Endpoint to update a test's status and trigger recalculations.
     * Example JSON Body:
     * {
     * "newStatus": "RÉUSSI",
     * "score": 95.5
     * }
     */
    @PutMapping("/{testId}/status")
    public ResponseEntity<TestResponseDTO> updateTestStatus( // Return DTO
                                                             @PathVariable Long testId,
                                                             @RequestBody TestStatusUpdateRequest request) {
        try {
            Test updatedTest = testService.updateTestStatus(
                    testId,
                    request.getNewStatus(),
                    request.getScore()
            );
            // Map the entity to a DTO before returning
            TestResponseDTO responseDto = testMapper.toDto(updatedTest);
            return ResponseEntity.ok(responseDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Your DTO for the request body remains the same
    public static class TestStatusUpdateRequest {
        private TestStatus newStatus;
        private Double score;

        public TestStatus getNewStatus() { return newStatus; }
        public void setNewStatus(TestStatus newStatus) { this.newStatus = newStatus; }
        public Double getScore() { return score; }
        public void setScore(Double score) { this.score = score; }
    }



    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        try {
            testService.deleteTest(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/count/all")
    public ResponseEntity<Long> getAllTestsCount(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));
        Long count = testService.getAllTestsCountForClient(user.getId());
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/successful")
    public ResponseEntity<Long> getSuccessfulTestsCountForClient(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));

        Long count = testService.countSuccessfulTestsForClient(user.getId());
        return ResponseEntity.ok(count);
    }

}