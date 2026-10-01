package dev.saurabh.beacon.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name = "evidence", indexes = @Index(name = "idx_evidence_incident_time", columnList = "incident_id, observed_at"))
public class Evidence {
    @Id public UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "incident_id", nullable = false) public Incident incident;
    @Column(nullable = false, length = 80) public String source;
    @Column(name = "observed_at", nullable = false) public Instant observedAt;
    @Column(nullable = false, length = 4000) public String summary;
    protected Evidence() {}
    public Evidence(Incident incident, String source, Instant observedAt, String summary) {
        this.id = UUID.randomUUID(); this.incident = incident; this.source = source;
        this.observedAt = observedAt; this.summary = summary;
    }
}

