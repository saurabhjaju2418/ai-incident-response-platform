package dev.saurabh.beacon.repo;
import dev.saurabh.beacon.domain.ActionProposal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
public interface ActionProposalRepository extends JpaRepository<ActionProposal, UUID> {
    List<ActionProposal> findByIncidentIdOrderByCreatedAtDesc(UUID incidentId);
}

