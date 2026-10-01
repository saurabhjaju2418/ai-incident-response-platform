package dev.saurabh.beacon.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name = "incidents", indexes = @Index(name = "idx_incident_status_opened", columnList = "status, opened_at"))
public class Incident {
    @Id public UUID id;
    @Column(nullable = false, unique = true, length = 64) public String fingerprint;
    @Column(nullable = false, length = 180) public String title;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 16) public Severity severity;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 16) public IncidentStatus status = IncidentStatus.OPEN;
    @Column(name = "opened_at", nullable = false) public Instant openedAt;
    @Column(name = "updated_at", nullable = false) public Instant updatedAt;

    protected Incident() {}
    public Incident(String fingerprint, String title, Severity severity, Instant now) {
        this.id = UUID.randomUUID(); this.fingerprint = fingerprint; this.title = title;
        this.severity = severity; this.openedAt = now; this.updatedAt = now;
    }
}

