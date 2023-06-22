package com.job.publisher.controller;

import com.job.publisher.dto.MyJobResponseDto;
import com.job.publisher.dto.MyProposalResponseDto;
import com.job.publisher.dto.SubmitJobRequestDto;
import com.job.publisher.dto.WebResponse;
import com.job.publisher.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/proposal")
public class ProposalController {

    @Autowired
    private ProposalService proposalService;

    @PostMapping(value = "")
    public WebResponse<String> submitProposal(@RequestBody SubmitJobRequestDto submitJobRequestDto){
        proposalService.submitProposal(submitJobRequestDto);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(value = "/my-proposal")
    public WebResponse<MyProposalResponseDto> getMyProposal(){
        MyProposalResponseDto myProposalResponseDto = proposalService.getMyProposal();
        return WebResponse.<MyProposalResponseDto>builder().data(myProposalResponseDto).build();
    }

    @GetMapping(value = "/my-jobs")
    public WebResponse<List<MyJobResponseDto>> getMyJob(){
        List<MyJobResponseDto> myJobResponseDtos = proposalService.getMyJob();
        return WebResponse.<List<MyJobResponseDto>>builder().data(myJobResponseDtos).build();
    }
}
