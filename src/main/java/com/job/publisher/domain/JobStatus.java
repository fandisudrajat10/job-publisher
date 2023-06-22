package com.job.publisher.domain;

public enum JobStatus {
    DRAFT("DRAFT"),
    PUBLISHED("PUBLISHED");

    private final String displayName;

    JobStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
