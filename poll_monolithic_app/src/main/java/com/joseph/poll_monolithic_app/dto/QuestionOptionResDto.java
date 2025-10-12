package com.joseph.poll_monolithic_app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionOptionResDto {

    private Long id;

    private String optionText;
}
