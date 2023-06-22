package com.job.publisher.repository;

import com.job.publisher.domain.Proposal;
import com.job.publisher.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {

    List<Proposal> findByFreelancer(User user);

    List<Proposal> findByJobEmployer(User user);
}
