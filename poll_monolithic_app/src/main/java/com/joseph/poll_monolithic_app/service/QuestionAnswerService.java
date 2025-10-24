package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.QuestionAnswerReqDto;
import com.joseph.poll_monolithic_app.dto.QuestionAnswerResDto;
import com.joseph.poll_monolithic_app.exception.ResourceNotFoundException;
import com.joseph.poll_monolithic_app.model.PollSubmission;
import com.joseph.poll_monolithic_app.model.Question;
import com.joseph.poll_monolithic_app.model.QuestionAnswer;
import com.joseph.poll_monolithic_app.repository.PollSubmissionRepo;
import com.joseph.poll_monolithic_app.repository.QuestionAnswerRepo;
import com.joseph.poll_monolithic_app.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionAnswerService {

    private final QuestionAnswerRepo questionAnswerRepository;
    private final QuestionRepository questionRepository;
    private final PollSubmissionRepo pollSubmissionRepo;

    @Transactional
    public QuestionAnswerResDto addAnswer(Long pollId, Long submissionId, QuestionAnswerReqDto answerDto) throws BadRequestException {

        PollSubmission submission = pollSubmissionRepo.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found"));

        Question question = questionRepository.findById(answerDto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        if (!submission.getPoll().getId().equals(pollId)) {
            throw new BadRequestException("Submission does not belong to this poll");
        }

        if (!question.getPoll().getId().equals(pollId)) {
            throw new BadRequestException("Question does not belong to this poll");
        }

        QuestionAnswer answer = mapToEntity(answerDto, question);
        answer.setResponse(submission);
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
