package com.joseph.poll_monolithic_app.dto;

import com.joseph.poll_monolithic_app.model.QuestionOption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionOptionDto {

    private Long id;

    private String optionText;
}
