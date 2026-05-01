package com.techsalary.voteservice.repository;

import com.techsalary.voteservice.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findBySubmissionId(UUID submissionId);
    boolean existsBySubmissionIdAndUserId(UUID submissionId, Long userId);
    long countBySubmissionIdAndUpvote(UUID submissionId, Boolean upvote);
}