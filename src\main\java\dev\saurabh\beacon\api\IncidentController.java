package dev.saurabh.beacon.api;

import dev.saurabh.beacon.domain.*;
import dev.saurabh.beacon.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController @RequestMapping("/api")
public class IncidentController {
    private final IncidentService service;
    public IncidentController(IncidentService service) { this.service = service; }

    @PostMapping("/alerts") @ResponseStatus(HttpStatus.ACCEPTED)
    public IncidentView ingest(@Valid @RequestBody AlertRequest request) { return service.ingest(request); }
    @GetMapping("/incidents") public List<IncidentView> list() { return service.list(); }
    @GetMapping("/incidents/{id}/evidence") public List<Evidence> evidence(@PathVariable UUID id) { return service.timeline(id); }
    @PatchMapping("/incidents/{id}/status")
    public Incident status(@PathVariable UUID id, @RequestBody StatusRequest request) { return service.updateStatus(id, request.status()); }
    @PostMapping("/incidents/{id}/proposals") @ResponseStatus(HttpStatus.CREATED)
    public ActionProposal propose(@PathVariable UUID id, @Valid @RequestBody ProposalRequest request) { return service.propose(id, request.action(), request.rationale()); }
    @PostMapping("/proposals/{id}/review")
    public ActionProposal review(@PathVariable UUID id, @RequestBody ReviewRequest request) { return service.review(id, request.approve()); }

    public record StatusRequest(IncidentStatus status) {}
    public record ProposalRequest(@jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Size(max=240) String action,
                                  @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Size(max=1000) String rationale) {}
    public record ReviewRequest(boolean approve) {}
}

