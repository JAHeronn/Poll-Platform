package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.PollSubmissionReqDto;
import com.joseph.poll_monolithic_app.dto.PollSubmissionResDto;
import com.joseph.poll_monolithic_app.exception.ResourceNotFoundException;
import com.joseph.poll_monolithic_app.model.*;
import com.joseph.poll_monolithic_app.repository.PollRepository;
import com.joseph.poll_monolithic_app.repository.PollSubmissionRepository;
import com.joseph.poll_monolithic_app.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PollSubmissionService {

    private final PollSubmissionRepository pollSubmissionRepository;
    private final PollRepository pollRepository;
    private final QuestionRepository questionRepository;

    // public PollSubmissionResDto submitPoll()

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

//    public PollSubmissionResDto mapToDto(PollSubmission pollSubmission) {
//
//    }
}
