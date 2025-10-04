package com.joseph.poll_monolithic_app.service;

import com.joseph.poll_monolithic_app.model.Tenant;
import com.joseph.poll_monolithic_app.model.User;
import com.joseph.poll_monolithic_app.model.UserTenant;
import com.joseph.poll_monolithic_app.model.enums.Role;
import com.joseph.poll_monolithic_app.repository.UserTenantRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final UserTenantRepo userTenantRepository;

    public UserTenant assignUser(User user, Tenant tenant) {
        UserTenant membership = new UserTenant();
        membership.setUser(user);
        membership.setTenant(tenant);
        membership.setRole(Role.ADMIN);

        return userTenantRepository.save(membership);
    }
}
