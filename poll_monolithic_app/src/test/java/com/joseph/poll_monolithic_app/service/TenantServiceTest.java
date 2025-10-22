package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.TenantRequestDto;
import com.joseph.poll_monolithic_app.dto.TenantResponseDto;
import com.joseph.poll_monolithic_app.model.Tenant;
import com.joseph.poll_monolithic_app.model.User;
import com.joseph.poll_monolithic_app.model.enums.Role;
import com.joseph.poll_monolithic_app.repository.TenantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantServiceTest {
    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private MembershipService membershipService;

    @InjectMocks
    private TenantService tenantService;

    @Test
    void createTenant_ShouldSaveTenantAndAssignMembership() {
        // Arrange
        TenantRequestDto requestDto = new TenantRequestDto();
        requestDto.setName("New Tenant");

        User user = User.builder().id(1L).username("John").build();

        Tenant savedTenant = Tenant.builder().id(1L).name(requestDto.getName()).build();

        when(tenantRepository.save(any(Tenant.class))).thenReturn(savedTenant);

        // Act
        TenantResponseDto result = tenantService.createTenant(requestDto, user, Role.ADMIN);

        // Assert
        ArgumentCaptor<Tenant> tenantCaptor = ArgumentCaptor.forClass(Tenant.class);
        verify(tenantRepository).save(tenantCaptor.capture());
        assertEquals("New Tenant", tenantCaptor.getValue().getName());

        verify(membershipService).assignUserToTenant(user, savedTenant, Role.ADMIN);

        assertNotNull(result);
        assertEquals(savedTenant.getId(), result.getId());
        assertEquals(savedTenant.getName(), result.getName());
    }

    @Test
    void createDefaultTenant_ShouldCreateTenantWithUsernameSpace() {
        User user = User.builder().username("John")
                .createdAt(Instant.now())
                .build();

        Tenant savedTenant = Tenant.builder().id(1L)
                .name("John's Space")
                .createdAt(user.getCreatedAt())
                .build();

        when(tenantRepository.save(any(Tenant.class))).thenReturn(savedTenant);

        Tenant result = tenantService.createDefaultTenant(user);

        ArgumentCaptor<Tenant> tenantCaptor = ArgumentCaptor.forClass(Tenant.class);
        verify(tenantRepository).save(tenantCaptor.capture());
        assertEquals("John's Space", tenantCaptor.getValue().getName());
        assertEquals(user.getCreatedAt(), tenantCaptor.getValue().getCreatedAt());

        assertNotNull(result);
        assertEquals(savedTenant.getId(), result.getId());
        assertEquals(savedTenant.getName(), result.getName());
        assertEquals(savedTenant.getCreatedAt(), result.getCreatedAt());
    }
}