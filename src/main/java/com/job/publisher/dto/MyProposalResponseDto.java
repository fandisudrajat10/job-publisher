package com.job.publisher.dto;

import lombok.Data;

@Data
public class MyProposalResponseDto {

    private Long id;

    private String jobTitle;

    private String jobDescription;

    private String proposalText;

    private String employerUsername;
}
