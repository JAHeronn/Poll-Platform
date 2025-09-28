package com.joseph.poll_monolithic_app.controller;

import com.joseph.poll_monolithic_app.dto.UserRequestDto;
import com.joseph.poll_monolithic_app.dto.UserResponseDto;
import com.joseph.poll_monolithic_app.service.UserRegistrationService;
import com.joseph.poll_monolithic_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRegistrationService registrationService;

    @PostMapping
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto userDto) {
        UserResponseDto response = registrationService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
