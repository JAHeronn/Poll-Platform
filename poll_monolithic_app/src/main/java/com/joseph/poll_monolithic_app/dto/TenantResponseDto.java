package com.joseph.poll_monolithic_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class TenantResponseDto {

    private Long id;

    @NotBlank(message = "Tenant name is required")
    private String name;

    private Instant createdAt;
}
