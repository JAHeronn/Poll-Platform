package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.QuestionAnswerReqDto;
import com.joseph.poll_monolithic_app.dto.QuestionAnswerResDto;
import com.joseph.poll_monolithic_app.model.Question;
import com.joseph.poll_monolithic_app.model.QuestionAnswer;
import com.joseph.poll_monolithic_app.repository.QuestionAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionAnswerService {

    private final QuestionAnswerRepository questionAnswerRepository;

    public QuestionAnswerResDto addAnswer(QuestionAnswerReqDto answerDto, Question question) {
        QuestionAnswer answer = mapToEntity(answerDto, question);
        QuestionAnswer savedAnswer = questionAnswerRepository.save(answer);

        return mapToDto(savedAnswer);
    }

    public QuestionAnswer mapToEntity(QuestionAnswerReqDto answerDto, Question question) {
        QuestionAnswer answer = new QuestionAnswer();
        answer.setAnswer(answerDto.getAnswer());
        answer.setQuestion(question);

        return answer;
    }

    public QuestionAnswerResDto mapToDto(QuestionAnswer answer) {
        QuestionAnswerResDto answerResDto = new QuestionAnswerResDto();
        answerResDto.setId(answer.getId());
        answerResDto.setQuestionId(answer.getQuestion().getId());
        answerResDto.setAnswer(answer.getAnswer());

        return answerResDto;
    }
}
