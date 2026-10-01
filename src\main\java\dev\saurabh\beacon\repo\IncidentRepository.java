package dev.saurabh.beacon.repo;
import dev.saurabh.beacon.domain.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
public interface IncidentRepository extends JpaRepository<Incident, UUID> {
    Optional<Incident> findByFingerprint(String fingerprint);
}

