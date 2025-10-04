package com.joseph.poll_monolithic_app.repository;

import com.joseph.poll_monolithic_app.model.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionAnswerRepo extends JpaRepository<QuestionAnswer, Long> {
}
