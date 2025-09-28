package com.joseph.poll_monolithic_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {

    private Long id;

    @NotBlank(message = "Email address cannot be empty")
    private String email;

    @NotBlank(message = "Username is required")
    private String userName;

    @NotBlank(message = "A full name is required")
    private String fullName;

    private Instant createdAt;
}
