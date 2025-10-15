package com.joseph.poll_monolithic_app.repository;

import com.joseph.poll_monolithic_app.model.Poll;
import com.joseph.poll_monolithic_app.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findByTenant(Tenant tenant);
}
