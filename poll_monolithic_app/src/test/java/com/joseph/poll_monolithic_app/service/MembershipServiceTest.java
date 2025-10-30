package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.model.Tenant;
import com.joseph.poll_monolithic_app.model.User;
import com.joseph.poll_monolithic_app.model.UserTenant;
import com.joseph.poll_monolithic_app.model.enums.Role;
import com.joseph.poll_monolithic_app.repository.UserTenantRepo;
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
class MembershipServiceTest {
    @Mock
    private UserTenantRepo userTenantRepository;

    @InjectMocks
    private MembershipService membershipService;

    @Test
    void assignUserToTenant_ShouldSaveMembership() {
        Tenant tenant = Tenant.builder().id(1L).name("John's Space").build();
        User creator = User.builder().id(1L)
                .username("JTurner")
                .fullName("John Turner")
                .email("john@example.com")
                .build();

        UserTenant savedMembership = UserTenant.builder().id(1L)
                .user(creator)
                .tenant(tenant)
                .role(Role.ADMIN)
                .build();

        when(userTenantRepository.save(any(UserTenant.class))).thenReturn(savedMembership);

        UserTenant result = membershipService.assignUserToTenant(creator, tenant, savedMembership.getRole());

        ArgumentCaptor<UserTenant> captor = ArgumentCaptor.forClass(UserTenant.class);
        verify(userTenantRepository).save(captor.capture());
        assertEquals("John Turner", captor.getValue().getUser().getFullName());
        assertEquals("John's Space", captor.getValue().getTenant().getName());
        assertEquals(Role.ADMIN, captor.getValue().getRole());

        assertNotNull(result);
        assertEquals(savedMembership.getId(), result.getId());
        assertEquals(savedMembership.getUser().getId(), result.getUser().getId());
        assertEquals(savedMembership.getTenant().getId(), result.getTenant().getId());
        assertEquals(savedMembership.getRole(), result.getRole());
    }
}