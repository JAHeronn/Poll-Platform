package com.joseph.poll_monolithic_app.dto;

import com.joseph.poll_monolithic_app.model.enums.QuestionType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionRequestDto {
    @NotBlank(message = "Question cannot be blank")
    private String questionText;

    private QuestionType type;

    private List<QuestionOptionResDto> options = new ArrayList<>();
}
