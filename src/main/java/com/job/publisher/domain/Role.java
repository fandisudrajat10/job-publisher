package com.job.publisher.domain;

public enum Role {
    EMPLOYER("EMPLOYER"),
    FREELANCER("FREELANCER");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
