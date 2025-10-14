package com.joseph.poll_monolithic_app.controller;

import com.joseph.poll_monolithic_app.dto.PollSubmissionReqDto;
import com.joseph.poll_monolithic_app.dto.PollSubmissionResDto;
import com.joseph.poll_monolithic_app.dto.QuestionAnswerReqDto;
import com.joseph.poll_monolithic_app.dto.QuestionAnswerResDto;
import com.joseph.poll_monolithic_app.exception.ResourceNotFoundException;
import com.joseph.poll_monolithic_app.model.Poll;
import com.joseph.poll_monolithic_app.model.User;
import com.joseph.poll_monolithic_app.repository.UserRepository;
import com.joseph.poll_monolithic_app.service.PollSubmissionService;
import com.joseph.poll_monolithic_app.service.QuestionAnswerService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/polls/{pollId}/poll-submissions")
public class PollSubmissionController {

    private final PollSubmissionService pollSubmissionService;
    private final QuestionAnswerService questionAnswerService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<PollSubmissionResDto> submitPoll(@PathVariable Long pollId, @RequestBody PollSubmissionReqDto reqDto) {
        // this mockUser is temporary until auth is sorted (as controller shouldn't call repo)
        User mockUser = userRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Mock user not found"));

        PollSubmissionResDto submittedPoll = pollSubmissionService.submitPoll(reqDto, pollId, mockUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(submittedPoll);
    }

    @PostMapping("/{submissionId}/answers")
    public ResponseEntity<QuestionAnswerResDto> addAnswer(@PathVariable Long pollId, @PathVariable Long submissionId, @RequestBody QuestionAnswerReqDto answerReqDto) throws BadRequestException {
        QuestionAnswerResDto submittedAnswer = questionAnswerService.addAnswer(pollId, submissionId, answerReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(submittedAnswer);
    }
}
