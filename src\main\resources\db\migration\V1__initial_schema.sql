create table incidents (
  id uuid primary key,
  fingerprint varchar(64) not null unique,
  title varchar(180) not null,
  severity varchar(16) not null check (severity in ('LOW','MEDIUM','HIGH','CRITICAL')),
  status varchar(16) not null check (status in ('OPEN','ACKNOWLEDGED','RESOLVED')),
  opened_at timestamptz not null,
  updated_at timestamptz not null
);
create index idx_incident_status_opened on incidents(status, opened_at);

create table evidence (
  id uuid primary key,
  incident_id uuid not null references incidents(id) on delete cascade,
  source varchar(80) not null,
  observed_at timestamptz not null,
  summary varchar(4000) not null
);
create index idx_evidence_incident_time on evidence(incident_id, observed_at);

create table action_proposals (
  id uuid primary key,
  incident_id uuid not null references incidents(id) on delete cascade,
  action varchar(240) not null,
  rationale varchar(1000) not null,
  approval_state varchar(16) not null check (approval_state in ('PENDING','APPROVED','REJECTED')),
  created_at timestamptz not null,
  reviewed_at timestamptz
);

