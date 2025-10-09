package com.joseph.poll_monolithic_app.controller;

import com.joseph.poll_monolithic_app.dto.PollRequestDto;
import com.joseph.poll_monolithic_app.dto.PollResponseDto;
import com.joseph.poll_monolithic_app.dto.QuestionRequestDto;
import com.joseph.poll_monolithic_app.dto.QuestionResponseDto;
import com.joseph.poll_monolithic_app.exception.ResourceNotFoundException;
import com.joseph.poll_monolithic_app.model.Tenant;
import com.joseph.poll_monolithic_app.model.User;
import com.joseph.poll_monolithic_app.service.PollService;
import com.joseph.poll_monolithic_app.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/polls")
public class PollController {

    private final PollService pollService;
    private final QuestionService questionService;

    @PostMapping("/{pollId}/questions")
    public ResponseEntity<QuestionResponseDto> addQuestion(@PathVariable Long pollId, @RequestBody QuestionRequestDto questionDto) {
        QuestionResponseDto createdQuestion = questionService.addQuestion(pollId, questionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @PostMapping
    public ResponseEntity<PollResponseDto> createPoll(@RequestBody PollRequestDto pollRequestDto) {
        PollResponseDto createdPoll = pollService.createPoll(pollRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPoll);
    }

}
