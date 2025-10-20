package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.dto.TenantRequestDto;
import com.joseph.poll_monolithic_app.dto.TenantResponseDto;
import com.joseph.poll_monolithic_app.exception.ResourceNotFoundException;
import com.joseph.poll_monolithic_app.model.Tenant;
import com.joseph.poll_monolithic_app.model.User;
import com.joseph.poll_monolithic_app.model.enums.Role;
import com.joseph.poll_monolithic_app.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final MembershipService membershipService;

    public Tenant createDefaultTenant(User user) {
        Tenant tenant = new Tenant();
        tenant.setName(user.getUsername() + "'s Space");
        tenant.setCreatedAt(user.getCreatedAt());
        return tenantRepository.save(tenant);
    }

    public TenantResponseDto createTenant(TenantRequestDto tenantRequestDto, User user) {
        return createTenant(tenantRequestDto, user, Role.ADMIN);
    }

    public TenantResponseDto createTenant(TenantRequestDto tenantRequestDto, User user, Role role) {
        Tenant tenant = mapToEntity(tenantRequestDto);
        Tenant savedTenant = tenantRepository.save(tenant);
        membershipService.assignUserToTenant(user, savedTenant, role);
        return mapToDto(savedTenant);
    }

    public Tenant mapToEntity(TenantRequestDto tenantRequestDto) {
        Tenant tenant = new Tenant();
        tenant.setName(tenantRequestDto.getName());
        return tenant;
    }

    public TenantResponseDto mapToDto(Tenant tenant) {
        TenantResponseDto tenantDto = new TenantResponseDto();
        tenantDto.setName(tenant.getName());
        tenantDto.setId(tenant.getId());
        tenantDto.setCreatedAt(tenant.getCreatedAt());
        return tenantDto;
    }
}
