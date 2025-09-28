package com.joseph.poll_monolithic_app.controller;

import com.joseph.poll_monolithic_app.dto.QuestionRequestDto;
import com.joseph.poll_monolithic_app.dto.QuestionResponseDto;
import com.joseph.poll_monolithic_app.service.PollService;
import com.joseph.poll_monolithic_app.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PollController {

    private final PollService pollService;
    private final QuestionService questionService;

    @PostMapping("/polls/{pollId}/questions")
    public QuestionResponseDto addQuestion(
            @PathVariable Long pollId,
            @RequestBody QuestionRequestDto questionDto) {
        return questionService.addQuestion(pollId, questionDto);
    }

}
