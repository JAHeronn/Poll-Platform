package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.UserRequestDto;
import com.joseph.poll_monolithic_app.dto.UserResponseDto;
import com.joseph.poll_monolithic_app.model.Tenant;
import com.joseph.poll_monolithic_app.model.User;
import com.joseph.poll_monolithic_app.model.UserTenant;
import com.joseph.poll_monolithic_app.model.enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private TenantService tenantService;

    @Mock
    private MembershipService membershipService;

    @InjectMocks
    private UserRegistrationService userRegistrationService;

    @Test
    void registerUser_ShouldOrchestrateUserCreationWithAdminRole() {
        UserRequestDto requestDto = new UserRequestDto();
        User creator = new User();
        Tenant tenant = new Tenant();
        UserTenant membership = new UserTenant();
        UserResponseDto responseDto = new UserResponseDto();

        when(userService.createUser(requestDto)).thenReturn(creator);
        when(tenantService.createDefaultTenant(creator)).thenReturn(tenant);
        when(membershipService.assignUserToTenant(creator, tenant, Role.ADMIN))
                .thenReturn(membership);

        when(userService.mapToDto(creator)).thenReturn(responseDto);

        UserResponseDto result = userRegistrationService.registerUser(requestDto);

        assertEquals(responseDto, result);

        InOrder inOrder = inOrder(userService, tenantService, membershipService);
        inOrder.verify(userService).createUser(requestDto);
        inOrder.verify(tenantService).createDefaultTenant(creator);
        inOrder.verify(membershipService).assignUserToTenant(creator, tenant, Role.ADMIN);
        inOrder.verify(userService).mapToDto(creator);
    }
}