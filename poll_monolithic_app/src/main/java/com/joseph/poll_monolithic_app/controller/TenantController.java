package com.joseph.poll_monolithic_app.controller;

import com.joseph.poll_monolithic_app.dto.TenantRequestDto;
import com.joseph.poll_monolithic_app.dto.TenantResponseDto;
import com.joseph.poll_monolithic_app.model.User;
import com.joseph.poll_monolithic_app.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<TenantResponseDto> createTenant(@RequestBody TenantRequestDto tenantDto, User user) {
        TenantResponseDto createdTenant = tenantService.createTenant(tenantDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTenant);
    }
}
