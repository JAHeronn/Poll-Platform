package com.joseph.poll_monolithic_app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PollSubmissionDto {

    private Long id;

    private Long pollId;

    private List<QuestionAnswerDto> answers = new ArrayList<>();

    private Instant submittedAt;
}
