<div align="center">

<img src="assets/project-banner.svg" alt="Animated Beacon — AI Incident Response banner" width="900" />

# Beacon — AI Incident Response

**Turn noisy alerts into a clear, reviewable response.**

Java 21 · Spring Boot · PostgreSQL · Flyway · Docker

</div>

Beacon is a runnable incident-management API. It accepts alert events, correlates repeats into incidents using a deterministic fingerprint, maintains an evidence timeline, and records response proposals behind a human approval step.

## Implemented

- POST /api/alerts: validate and ingest alert evidence; normalize source/title for deduplication; elevate incident severity when a more severe alert arrives.
- GET /api/incidents: list incidents with evidence counts.
- GET /api/incidents/{id}/evidence: return the incident evidence timeline.
- PATCH /api/incidents/{id}/status: acknowledge or resolve an incident.
- POST /api/incidents/{id}/proposals: create a pending response proposal.
- POST /api/proposals/{id}/review: approve or reject a pending proposal. This endpoint records review only; it never executes an action.
- PostgreSQL schema managed by Flyway, input validation, health/metrics endpoints, Docker Compose.

## Run

Requirements: Docker Compose. Start the API and database:

```bash
docker compose up --build
```

Health check: http://localhost:8080/actuator/health

Example alert:

```bash
curl -X POST http://localhost:8080/api/alerts \
  -H 'Content-Type: application/json' \
  -d '{"title":"checkout latency elevated","source":"payments","summary":"p95 latency reached 2.4s","severity":"HIGH","occurredAt":"2026-10-01T08:00:00Z"}'
```

## Architecture

```text
Alert source -> REST validation -> fingerprint correlation -> PostgreSQL
                                             |                 |
                                             +-> evidence timeline
                                             +-> proposal -> human review
```

The API is stateless; relational state lives in Postgres. Alert identity is currently a SHA-256 over normalized source and title, which is a transparent starter rule rather than a universal incident-correlation strategy.

## Boundaries and next steps

The repository currently does not include Kafka ingestion, authentication/authorization, an LLM provider, an executable runbook adapter, or production observability exporters. Recommendations are manually supplied proposal records; no model generates them. The explicit review state is a hard boundary: approval is stored, not executed. Next milestones are tenant identity, idempotent provider event IDs, correlation windows, Kafka adapter, grounded AI summaries with evidence references, and a separately permissioned runbook executor.

## API configuration

Set DATABASE_URL, DATABASE_USER, and DATABASE_PASSWORD to override the Compose defaults. Actuator exposes health, info, metrics, and Prometheus endpoints.

## License

MIT. See [LICENSE](LICENSE).

