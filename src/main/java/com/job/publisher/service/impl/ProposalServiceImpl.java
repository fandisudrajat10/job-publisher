package com.job.publisher.service.impl;

import com.job.publisher.domain.Job;
import com.job.publisher.domain.JobStatus;
import com.job.publisher.domain.Proposal;
import com.job.publisher.domain.User;
import com.job.publisher.dto.MyJobResponseDto;
import com.job.publisher.dto.MyProposalResponseDto;
import com.job.publisher.dto.SubmitJobRequestDto;
import com.job.publisher.repository.ProposalRepository;
import com.job.publisher.service.JobService;
import com.job.publisher.service.ProposalService;
import com.job.publisher.service.UserService;
import com.job.publisher.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProposalServiceImpl implements ProposalService {

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JobService jobService;

    @Autowired
    private ValidationService validationService;

    @Transactional
    @Override
    public void submitProposal(SubmitJobRequestDto requestDto) {
        Job job = jobService.findByJobId(requestDto.getJobId());
        if (job.getStatus().equals(JobStatus.DRAFT)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid job");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        List<Proposal> proposalList = proposalRepository.findByFreelancer(user);
        if (!proposalList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, user.getUsername() + " already apply for job " + proposalList.get(0).getJob().getTitle());
        } else {
            Proposal proposal = new Proposal();
            proposal.setProposalText(requestDto.getProposalText());
            proposal.setJob(job);
            proposal.setFreelancer(user);
            proposal.setCreatedAt(LocalDateTime.now());
            proposalRepository.save(proposal);
        }

    }

    @Override
    public MyProposalResponseDto getMyProposal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        List<Proposal> proposalList = proposalRepository.findByFreelancer(user);
        if (!proposalList.isEmpty()) {
            Proposal proposal = proposalList.get(0);
            MyProposalResponseDto myProposalResponseDto = new MyProposalResponseDto();
            myProposalResponseDto.setId(proposal.getId());
            myProposalResponseDto.setJobTitle(proposal.getJob().getTitle());
            myProposalResponseDto.setJobDescription(proposal.getJob().getDescription());
            myProposalResponseDto.setProposalText(proposal.getProposalText());
            myProposalResponseDto.setEmployerUsername(proposal.getJob().getEmployer().getUsername());
            return myProposalResponseDto;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, user.getUsername() + " doesn't have any proposal");
        }
    }

    @Override
    public List<MyJobResponseDto> getMyJob() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        List<Proposal> proposalList = proposalRepository.findByJobEmployer(user);
        return proposalList.stream().map(proposal -> {
            MyJobResponseDto myJobResponseDto = new MyJobResponseDto();
            myJobResponseDto.setId(proposal.getId());
            myJobResponseDto.setJobTitle(proposal.getJob().getTitle());
            myJobResponseDto.setJobDescription(proposal.getJob().getDescription());
            myJobResponseDto.setFreelancerUsername(proposal.getFreelancer().getUsername());
            myJobResponseDto.setFreelancerEmail(proposal.getFreelancer().getEmail());
            myJobResponseDto.setProposalText(proposal.getProposalText());
            return myJobResponseDto;
        }).collect(Collectors.toList());
    }
}
