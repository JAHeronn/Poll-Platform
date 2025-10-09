package com.joseph.poll_monolithic_app.controller;

import com.joseph.poll_monolithic_app.dto.QuestionRequestDto;
import com.joseph.poll_monolithic_app.dto.QuestionResponseDto;
import com.joseph.poll_monolithic_app.exception.ResourceNotFoundException;
import com.joseph.poll_monolithic_app.model.Tenant;
import com.joseph.poll_monolithic_app.model.User;
import com.joseph.poll_monolithic_app.service.PollService;
import com.joseph.poll_monolithic_app.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/polls")
public class PollController {

    private final PollService pollService;
    private final QuestionService questionService;

    @PostMapping("/{pollId}/questions")
    public QuestionResponseDto addQuestion(
            @PathVariable Long pollId,
            @RequestBody QuestionRequestDto questionDto) {
        return questionService.addQuestion(pollId, questionDto);
    }

//    User mockUser = userRepository.findById(1L)
//            .orElseThrow(() -> new ResourceNotFoundException("Mock user not found"));
//    Tenant mockTenant = tenantRepository.findById(1L)
//            .orElseThrow(() -> new ResourceNotFoundException("Mock tenant not found"));

}
