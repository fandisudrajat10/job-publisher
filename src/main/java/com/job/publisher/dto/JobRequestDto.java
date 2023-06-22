package com.job.publisher.dto;

import com.job.publisher.validator.ValidStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestDto {

    private Long id;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    @ValidStatus
    private String status;
}
