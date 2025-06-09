package com.virtual.lab.backend.mapper;

import com.virtual.lab.backend.model.TestGroup;
import com.virtual.lab.backend.model.Test;
import com.virtual.lab.backend.dto.TestGroupResponseDTO;
import com.virtual.lab.backend.dto.TestResponseDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TestGroupMapper {

    private final TestMapper testMapper; // Inject TestMapper

    public TestGroupMapper(TestMapper testMapper) {
        this.testMapper = testMapper;
    }

    public TestGroupResponseDTO toDto(TestGroup testGroup) {
        if (testGroup == null) {
            return null;
        }
        TestGroupResponseDTO dto = new TestGroupResponseDTO();
        dto.setId(testGroup.getId());
        dto.setGroupNumber(testGroup.getGroupNumber());
        dto.setCalculatedStatus(testGroup.getCalculatedStatus());
        dto.setCalculatedProgress(testGroup.getCalculatedProgress());

        if (testGroup.getTests() != null) {
            dto.setTests(testGroup.getTests().stream()
                    .map(testMapper::toDto) // Use injected TestMapper
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
