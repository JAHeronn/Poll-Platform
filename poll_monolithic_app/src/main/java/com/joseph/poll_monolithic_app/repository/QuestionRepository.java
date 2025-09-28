package com.joseph.poll_monolithic_app.repository;

import com.joseph.poll_monolithic_app.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
