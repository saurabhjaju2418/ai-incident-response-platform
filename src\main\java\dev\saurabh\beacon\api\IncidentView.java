package dev.saurabh.beacon.api;

import dev.saurabh.beacon.domain.Incident;
import dev.saurabh.beacon.domain.IncidentStatus;
import dev.saurabh.beacon.domain.Severity;
import java.time.Instant;
import java.util.UUID;

public record IncidentView(UUID id, String title, Severity severity, IncidentStatus status,
                           Instant openedAt, int evidenceCount) {
    public static IncidentView of(Incident i, int count) {
        return new IncidentView(i.id, i.title, i.severity, i.status, i.openedAt, count);
    }
}

