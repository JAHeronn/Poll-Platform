package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.PollSubmissionReqDto;
import com.joseph.poll_monolithic_app.dto.PollSubmissionResDto;
import com.joseph.poll_monolithic_app.dto.QuestionAnswerResDto;
import com.joseph.poll_monolithic_app.exception.ResourceNotFoundException;
import com.joseph.poll_monolithic_app.model.*;
import com.joseph.poll_monolithic_app.repository.PollSubmissionRepo;
import com.joseph.poll_monolithic_app.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PollSubmissionService {

    private final PollSubmissionRepo pollSubmissionRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public PollSubmissionResDto submitPoll(PollSubmissionReqDto submissionReqDto, Poll poll, User responder) {
        PollSubmission pollSubmission = mapToEntity(submissionReqDto, poll, responder);
        PollSubmission savedPollSubmission = pollSubmissionRepository.save(pollSubmission);

        return mapToDto(savedPollSubmission, poll, responder);
    }

    public PollSubmission mapToEntity(PollSubmissionReqDto submissionReqDto, Poll poll, User responder) {
        PollSubmission pollSubmission = new PollSubmission();
        pollSubmission.setResponder(responder);
        pollSubmission.setPoll(poll);

        if (submissionReqDto.getAnswers() != null) {
            List<QuestionAnswer> answers = submissionReqDto.getAnswers()
                    .stream()
                    .map(answerDto -> {
                        QuestionAnswer answer = new QuestionAnswer();
                        answer.setAnswer(answerDto.getAnswer());

                        Question question = questionRepository.findById(answerDto.getQuestionId())
                                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

                        answer.setQuestion(question);
                        answer.setResponse(pollSubmission);

                        return answer;

                    })
                    .toList();

            pollSubmission.setAnswers(answers);
        }

        return pollSubmission;
    }

    public PollSubmissionResDto mapToDto(PollSubmission pollSubmission, Poll poll, User responder) {
        PollSubmissionResDto submissionResDto = new PollSubmissionResDto();
        submissionResDto.setId(pollSubmission.getId());
        submissionResDto.setPollId(poll.getId());
        submissionResDto.setResponderName(responder.getFullName());
        submissionResDto.setResponderUsername(responder.getUsername());
        submissionResDto.setSubmittedAt(pollSubmission.getSubmittedAt());

        List<QuestionAnswerResDto> answerDtos = pollSubmission.getAnswers()
                .stream()
                .map(answer -> {
                    QuestionAnswerResDto answerResDto = new QuestionAnswerResDto();
                    answerResDto.setId(answer.getId());
                    answerResDto.setQuestionId(answer.getQuestion().getId());
                    answerResDto.setAnswer(answer.getAnswer());
                    return answerResDto;
                })
                .toList();

        submissionResDto.setAnswers(answerDtos);

        return submissionResDto;
    }

}
