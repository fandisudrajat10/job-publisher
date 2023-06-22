package com.job.publisher.service;

import com.job.publisher.dto.MyJobResponseDto;
import com.job.publisher.dto.MyProposalResponseDto;
import com.job.publisher.dto.SubmitJobRequestDto;

import java.util.List;

public interface ProposalService {
    void submitProposal(SubmitJobRequestDto submitJobRequestDto);

    MyProposalResponseDto getMyProposal();

    List<MyJobResponseDto> getMyJob();
}
