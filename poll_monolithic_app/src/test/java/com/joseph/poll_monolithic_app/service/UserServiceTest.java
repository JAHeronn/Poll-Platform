package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.UserRequestDto;
import com.joseph.poll_monolithic_app.model.User;
import com.joseph.poll_monolithic_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ShouldSaveUser() {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setUserName("JohnT");
        requestDto.setFullName("John Turner");
        requestDto.setEmail("John@example.com");

        User savedUser = User.builder().id(1L)
                .username(requestDto.getUserName())
                .fullName(requestDto.getFullName())
                .email(requestDto.getEmail())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser(requestDto);

        // Assert repository input - checking the saved data is as expected
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User capturedUser = captor.getValue();
        assertEquals("JohnT", capturedUser.getUsername());
        assertEquals("John Turner", capturedUser.getFullName());
        assertEquals("John@example.com", capturedUser.getEmail());

        // Assert service output - checking the output is as expected
        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals(savedUser.getUsername(), result.getUsername());
        assertEquals(savedUser.getFullName(), result.getFullName());
        assertEquals(savedUser.getEmail(), result.getEmail());
    }
}