package com.joseph.poll_monolithic_app.dto;

import com.joseph.poll_monolithic_app.model.enums.PollStatus;
import com.joseph.poll_monolithic_app.model.enums.Visibility;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class PollResponseDto {

    private Long id;

    @NotBlank(message = "title is required")
    private String title;

    private String description;

    private String creatorName;

    private String creatorUsername;

    private String tenantName;

    private Instant createdAt;

    private Visibility visibility;

    private PollStatus pollStatus;
}
