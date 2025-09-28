package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.UserRequestDto;
import com.joseph.poll_monolithic_app.dto.UserResponseDto;
import com.joseph.poll_monolithic_app.model.Tenant;
import com.joseph.poll_monolithic_app.model.User;
import com.joseph.poll_monolithic_app.model.UserTenant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserService userService;
    private final TenantService tenantService;
    private final MembershipService membershipService;

    // Creates user, creates tenant for user, establishes user role, then saves user
    public UserResponseDto registerUser(UserRequestDto userDto) {
        User user = userService.createUser(userDto);
        Tenant tenant = tenantService.createDefaultTenant(user);
        UserTenant membership = membershipService.assignUser(user, tenant);

        return userService.mapToDto(user);
    }
}
