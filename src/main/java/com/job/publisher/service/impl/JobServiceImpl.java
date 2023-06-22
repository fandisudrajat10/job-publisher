package com.job.publisher.service.impl;

import com.job.publisher.domain.Job;
import com.job.publisher.domain.JobStatus;
import com.job.publisher.domain.Role;
import com.job.publisher.domain.User;
import com.job.publisher.dto.JobRequestDto;
import com.job.publisher.repository.JobRepository;
import com.job.publisher.service.JobService;
import com.job.publisher.service.UserService;
import com.job.publisher.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public JobRequestDto createJob(JobRequestDto requestDto) {

        validationService.validateRequest(requestDto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        Job job = new Job();
        job.setTitle(requestDto.getTitle());
        job.setDescription(requestDto.getDescription());
        job.setEmployer(user);
        job.setStatus(JobStatus.valueOf(requestDto.getStatus()));
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.save(job);
        requestDto.setId(job.getId());
        return requestDto;
    }

    @Override
    public List<JobRequestDto> getJobList(String title, String desc) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());

        Specification<Job> jobSpecification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (Objects.nonNull(title)) {
                predicates.add(builder.like(root.get("title"), "%" + title + "%"));
            }
            if (Objects.nonNull(desc)) {
                predicates.add(builder.like(root.get("description"), "%" + desc + "%"));
            }
            if (user.getRole().equals(Role.FREELANCER)){
                predicates.add(builder.equal(root.get("status"), JobStatus.PUBLISHED));
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
        List<Job> jobList = jobRepository.findAll(jobSpecification);
        return jobList.stream().map(JobServiceImpl::toDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void publishJob(Long jobId) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isPresent()){
            Job job = jobOptional.get();
            if (job.getStatus().equals(JobStatus.DRAFT)){

                job.setStatus(JobStatus.PUBLISHED);
                job.setUpdatedAt(LocalDateTime.now());
                jobRepository.save(job);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job already published");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job doesn't exist");
        }
    }

    @Override
    public Job findByJobId(Long jobId) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isPresent()){
            return jobOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job doesn't exist");
        }
    }

    private static JobRequestDto toDto(Job job) {
        JobRequestDto requestDto = new JobRequestDto();
        requestDto.setId(job.getId());
        requestDto.setTitle(job.getTitle());
        requestDto.setDescription(job.getDescription());
        requestDto.setStatus(job.getStatus().getDisplayName());
        return requestDto;
    }
}
