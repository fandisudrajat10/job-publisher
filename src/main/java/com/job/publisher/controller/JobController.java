package com.job.publisher.controller;

import com.job.publisher.dto.JobRequestDto;
import com.job.publisher.dto.WebResponse;
import com.job.publisher.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping(value = "")
    public WebResponse<JobRequestDto> createJob(@RequestBody JobRequestDto requestDto) {
        JobRequestDto job = jobService.createJob(requestDto);
        return WebResponse.<JobRequestDto>builder().data(job).build();
    }

    @GetMapping(value = "/list")
    public WebResponse<List<JobRequestDto>> getJobList(@RequestParam(value = "title", required = false) String title,
                                                       @RequestParam(value = "description", required = false) String description) {
        List<JobRequestDto> jobList = jobService.getJobList(title, description);
        return WebResponse.<List<JobRequestDto>>builder().data(jobList).build();
    }

    @PatchMapping(value = "/publish/{id}")
    public WebResponse<String> getJobList(@PathVariable Long id) {
        jobService.publishJob(id);
        return WebResponse.<String>builder().data("OK").build();
    }
}
