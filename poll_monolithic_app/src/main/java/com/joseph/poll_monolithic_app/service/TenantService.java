package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.model.Tenant;
import com.joseph.poll_monolithic_app.model.User;
import com.joseph.poll_monolithic_app.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;

    public Tenant createDefaultTenant(User user) {
        Tenant tenant = new Tenant();
        tenant.setName(user.getUserName() + "'s Space");
        tenant.setCreatedAt(user.getCreatedAt());
        return tenantRepository.save(tenant);
    }
}
