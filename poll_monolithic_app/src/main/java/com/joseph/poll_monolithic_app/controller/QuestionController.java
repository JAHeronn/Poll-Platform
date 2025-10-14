package com.joseph.poll_monolithic_app.controller;


import com.joseph.poll_monolithic_app.dto.QuestionOptionReqDto;
import com.joseph.poll_monolithic_app.dto.QuestionOptionResDto;
import com.joseph.poll_monolithic_app.dto.QuestionRequestDto;
import com.joseph.poll_monolithic_app.dto.QuestionResponseDto;
import com.joseph.poll_monolithic_app.service.QuestionOptionService;
import com.joseph.poll_monolithic_app.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/polls/{pollId}/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionOptionService questionOptionService;

    @PostMapping
    public ResponseEntity<QuestionResponseDto> addQuestion(@PathVariable Long pollId, @RequestBody QuestionRequestDto questionDto) {
        QuestionResponseDto createdQuestion = questionService.addQuestion(pollId, questionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @PostMapping("/{questionId}/options")
    public ResponseEntity<QuestionOptionResDto> addOption(@PathVariable Long pollId, @PathVariable Long questionId, @RequestBody QuestionOptionReqDto optionReqDto) throws BadRequestException {
        QuestionOptionResDto createdOption = questionOptionService.addOption(pollId, questionId, optionReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOption);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionResponseDto> getQuestion(@PathVariable Long pollId, @PathVariable Long questionId) throws BadRequestException {
        QuestionResponseDto question = questionService.getQuestion(pollId, questionId);
        return ResponseEntity.ok(question);
    }
}
