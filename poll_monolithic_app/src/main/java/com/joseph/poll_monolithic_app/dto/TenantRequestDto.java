package com.joseph.poll_monolithic_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TenantRequestDto {
    @NotBlank(message = "Tenant name is required")
    private String name;
}
