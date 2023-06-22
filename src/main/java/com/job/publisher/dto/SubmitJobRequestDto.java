package com.job.publisher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmitJobRequestDto {

    @NotBlank
    private Long jobId;

    @NotBlank
    private String proposalText;
}
