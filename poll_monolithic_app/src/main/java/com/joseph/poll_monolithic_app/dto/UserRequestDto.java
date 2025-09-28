package com.joseph.poll_monolithic_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Email address cannot be empty")
    private String email;

    @NotBlank(message = "Username is required")
    private String userName;

    @NotBlank(message = "A full name is required")
    private String fullName;
}
