package com.joseph.poll_monolithic_app.controller;

import com.joseph.poll_monolithic_app.dto.PollRequestDto;
import com.joseph.poll_monolithic_app.dto.PollResponseDto;
import com.joseph.poll_monolithic_app.dto.QuestionRequestDto;
import com.joseph.poll_monolithic_app.dto.QuestionResponseDto;
import com.joseph.poll_monolithic_app.service.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/polls")
public class PollController {

    private final PollService pollService;

    @PostMapping
    public ResponseEntity<PollResponseDto> createPoll(@RequestBody PollRequestDto pollRequestDto) {
        PollResponseDto createdPoll = pollService.createPoll(pollRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPoll);
    }

    @GetMapping
    public List<PollResponseDto> getAllPolls() {
        return pollService.getAllPolls();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PollResponseDto> getPoll(@PathVariable Long id) {
        PollResponseDto poll = pollService.getPoll(id);
        return ResponseEntity.ok(poll);
    }

}
