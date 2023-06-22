package com.job.publisher.service;

import com.job.publisher.domain.Job;
import com.job.publisher.dto.JobRequestDto;

import java.util.List;

public interface JobService {

    JobRequestDto createJob(JobRequestDto requestDto);

    List<JobRequestDto> getJobList(String title, String desc);

    void publishJob(Long jobId);

    Job findByJobId(Long jobId);
}
