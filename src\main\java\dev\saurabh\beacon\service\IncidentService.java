package dev.saurabh.beacon.service;

import dev.saurabh.beacon.api.AlertRequest;
import dev.saurabh.beacon.api.IncidentView;
import dev.saurabh.beacon.domain.*;
import dev.saurabh.beacon.repo.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Service
public class IncidentService {
    private final IncidentRepository incidents;
    private final EvidenceRepository evidence;
    private final ActionProposalRepository proposals;
    public IncidentService(IncidentRepository incidents, EvidenceRepository evidence, ActionProposalRepository proposals) {
        this.incidents = incidents; this.evidence = evidence; this.proposals = proposals;
    }

    @Transactional
    public IncidentView ingest(AlertRequest request) {
        String fingerprint = fingerprint(request.source(), request.title());
        Incident incident = incidents.findByFingerprint(fingerprint).orElseGet(() ->
            incidents.save(new Incident(fingerprint, request.title(), request.severity(), Instant.now())));
        if (request.severity().ordinal() > incident.severity.ordinal()) incident.severity = request.severity();
        incident.updatedAt = Instant.now();
        evidence.save(new Evidence(incident, request.source(), request.occurredAt(), request.summary()));
        return IncidentView.of(incident, evidence.findByIncidentIdOrderByObservedAtAsc(incident.id).size());
    }

    @Transactional(readOnly = true)
    public List<IncidentView> list() {
        return incidents.findAll().stream().sorted((a,b) -> b.openedAt.compareTo(a.openedAt))
            .map(i -> IncidentView.of(i, evidence.findByIncidentIdOrderByObservedAtAsc(i.id).size())).toList();
    }

    @Transactional(readOnly = true)
    public List<Evidence> timeline(UUID id) {
        requireIncident(id);
        return evidence.findByIncidentIdOrderByObservedAtAsc(id);
    }

    @Transactional
    public Incident updateStatus(UUID id, IncidentStatus status) {
        Incident incident = requireIncident(id);
        incident.status = status; incident.updatedAt = Instant.now();
        return incident;
    }

    @Transactional
    public ActionProposal propose(UUID id, String action, String rationale) {
        return proposals.save(new ActionProposal(requireIncident(id), action, rationale, Instant.now()));
    }

    @Transactional
    public ActionProposal review(UUID proposalId, boolean approve) {
        ActionProposal proposal = proposals.findById(proposalId).orElseThrow(() -> new EntityNotFoundException("Proposal not found"));
        if (proposal.approvalState != ApprovalState.PENDING) throw new IllegalStateException("Proposal has already been reviewed");
        proposal.approvalState = approve ? ApprovalState.APPROVED : ApprovalState.REJECTED;
        proposal.reviewedAt = Instant.now();
        return proposal;
    }

    private Incident requireIncident(UUID id) {
        return incidents.findById(id).orElseThrow(() -> new EntityNotFoundException("Incident not found"));
    }

    private static String fingerprint(String source, String title) {
        try {
            String normalized = (source.trim().toLowerCase() + "|" + title.trim().toLowerCase()).replaceAll("\\s+", " ");
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(normalized.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) { throw new IllegalStateException("Fingerprint unavailable", e); }
    }
}

