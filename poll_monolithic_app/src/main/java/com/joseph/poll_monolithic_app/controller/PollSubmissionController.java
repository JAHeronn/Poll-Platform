package com.joseph.poll_monolithic_app.controller;

import com.joseph.poll_monolithic_app.dto.PollSubmissionReqDto;
import com.joseph.poll_monolithic_app.dto.PollSubmissionResDto;
import com.joseph.poll_monolithic_app.model.Poll;
import com.joseph.poll_monolithic_app.model.User;
import com.joseph.poll_monolithic_app.service.PollSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/poll-submissions")
public class PollSubmissionController {

    private final PollSubmissionService pollSubmissionService;

    @PostMapping
    public ResponseEntity<PollSubmissionResDto> submitPoll(@RequestBody PollSubmissionReqDto reqDto, Poll poll, User responder) {
        PollSubmissionResDto submittedPoll = pollSubmissionService.submitPoll(reqDto, poll, responder);
        return ResponseEntity.status(HttpStatus.CREATED).body(submittedPoll);
    }
}
