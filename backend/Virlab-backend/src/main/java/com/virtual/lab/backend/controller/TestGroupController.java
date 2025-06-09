package com.virtual.lab.backend.controller;

import com.virtual.lab.backend.dto.TestGroupDTO;
import com.virtual.lab.backend.service.TestGroupService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-groups")
public class TestGroupController {

    @Autowired
    private TestGroupService testGroupService;

    // CREATE
    @PostMapping("/createGroup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TestGroupDTO> createTestGroup(@RequestBody TestGroupDTO testGroupDto) {
        TestGroupDTO createdTestGroup = testGroupService.createTestGroup(testGroupDto);
        return new ResponseEntity<>(createdTestGroup, HttpStatus.CREATED);
    }

    // READ (Get by ID)
    @GetMapping("/getTestGroupById/{id}")
    public ResponseEntity<TestGroupDTO> getTestGroupById(@PathVariable Long id) {
        return testGroupService.getTestGroupById(id)
                .map(testGroupDto -> new ResponseEntity<>(testGroupDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // READ (Get all)
    @GetMapping("/getAllTestGroups")
    public ResponseEntity<List<TestGroupDTO>> getAllTestGroups() {
        List<TestGroupDTO> testGroups = testGroupService.getAllTestGroups();
        return new ResponseEntity<>(testGroups, HttpStatus.OK);
    }

    // READ (Get by Product ID) - Useful for nested retrieval
    @GetMapping("/by-product/{productId}")
    public ResponseEntity<List<TestGroupDTO>> getTestGroupsByProductId(@PathVariable Long productId) {
        List<TestGroupDTO> testGroups = testGroupService.getTestGroupsByProductId(productId);
        return new ResponseEntity<>(testGroups, HttpStatus.OK);
    }

    // UPDATE
    @PutMapping("/updateGroup/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TestGroupDTO> updateTestGroup(@PathVariable Long id, @RequestBody TestGroupDTO testGroupDto) {
        try {
            TestGroupDTO updatedTestGroup = testGroupService.updateTestGroup(id, testGroupDto);
            return new ResponseEntity<>(updatedTestGroup, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE
    @DeleteMapping("/deleteGroup/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTestGroup(@PathVariable Long id) {
        try {
            testGroupService.deleteTestGroup(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
