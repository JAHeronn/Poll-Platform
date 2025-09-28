package com.joseph.poll_monolithic_app.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PollRequestDto {

    @NotBlank(message = "title is required")
    private String title;

    private String description;
}
