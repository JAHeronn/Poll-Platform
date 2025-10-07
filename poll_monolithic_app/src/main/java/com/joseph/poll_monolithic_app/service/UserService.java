package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.UserRequestDto;
import com.joseph.poll_monolithic_app.dto.UserResponseDto;
import com.joseph.poll_monolithic_app.model.User;
import com.joseph.poll_monolithic_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(UserRequestDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUserName());
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());

        return userRepository.save(user);
    }

    public UserResponseDto mapToDto(User user) {
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setUserName(user.getUsername());
        userDto.setCreatedAt(user.getCreatedAt());

        return userDto;
    }
}