package dev.saurabh.beacon.api;

import dev.saurabh.beacon.domain.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public record AlertRequest(@NotBlank @Size(max = 180) String title,
                           @NotBlank @Size(max = 80) String source,
                           @NotBlank @Size(max = 4000) String summary,
                           @NotNull Severity severity,
                           @NotNull Instant occurredAt) {}

