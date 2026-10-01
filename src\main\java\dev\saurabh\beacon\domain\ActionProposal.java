package dev.saurabh.beacon.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name = "action_proposals")
public class ActionProposal {
    @Id public UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "incident_id", nullable = false) public Incident incident;
    @Column(nullable = false, length = 240) public String action;
    @Column(nullable = false, length = 1000) public String rationale;
    @Enumerated(EnumType.STRING) @Column(name = "approval_state", nullable = false, length = 16) public ApprovalState approvalState = ApprovalState.PENDING;
    @Column(name = "created_at", nullable = false) public Instant createdAt;
    @Column(name = "reviewed_at") public Instant reviewedAt;
    protected ActionProposal() {}
    public ActionProposal(Incident incident, String action, String rationale, Instant now) {
        this.id = UUID.randomUUID(); this.incident = incident; this.action = action;
        this.rationale = rationale; this.createdAt = now;
    }
}

