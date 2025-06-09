package com.virtual.lab.backend.dto;

import com.virtual.lab.backend.model.TestStatus;

public class TestStatusUpdateRequest {

    private TestStatus status;
    private Long technicienId;

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    public Long getTechnicienId() {
        return technicienId;
    }

    public void setTechnicienId(Long technicienId) {
        this.technicienId = technicienId;
    }
}
