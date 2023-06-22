package com.job.publisher.dto;

import lombok.Data;

@Data
public class MyJobResponseDto {

    private Long id;

    private String jobTitle;

    private String jobDescription;

    private String freelancerUsername;

    private String freelancerEmail;

    private String proposalText;
}
