package dev.saurabh.beacon.repo;
import dev.saurabh.beacon.domain.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
public interface EvidenceRepository extends JpaRepository<Evidence, UUID> {
    List<Evidence> findByIncidentIdOrderByObservedAtAsc(UUID incidentId);
}

