package com.joseph.poll_monolithic_app.repository;

import com.joseph.poll_monolithic_app.model.PollSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollSubmissionRepo extends JpaRepository<PollSubmission, Long> {
}
