package com.joseph.poll_monolithic_app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PollSubmissionReqDto {

    private List<QuestionAnswerReqDto> answers = new ArrayList<>();
}
