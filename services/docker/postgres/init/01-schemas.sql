-- ============================================================
-- TechSalary LK — PostgreSQL Schema Init
-- ============================================================

-- Extensions
CREATE EXTENSION IF NOT EXISTS "pgcrypto";  -- gen_random_uuid()

-- ============================================================
-- IDENTITY SCHEMA
-- ============================================================
CREATE SCHEMA IF NOT EXISTS identity;

CREATE TABLE IF NOT EXISTS identity.users (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email         VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20) DEFAULT 'USER',
    created_at    TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS identity.refresh_tokens (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID REFERENCES identity.users(id) ON DELETE CASCADE,
    token_hash  VARCHAR(255) NOT NULL,
    expires_at  TIMESTAMP NOT NULL,
    revoked     BOOLEAN DEFAULT FALSE
);

-- ============================================================
-- SALARY SCHEMA
-- ============================================================
CREATE SCHEMA IF NOT EXISTS salary;

CREATE TABLE IF NOT EXISTS salary.submissions (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_title        VARCHAR(255) NOT NULL,
    company          VARCHAR(255),
    anonymize        BOOLEAN DEFAULT TRUE,
    country          VARCHAR(100) NOT NULL,
    city             VARCHAR(100),
    experience_years INT,
    level            VARCHAR(50),
    gross_salary     NUMERIC(14,2) NOT NULL,
    currency         VARCHAR(10) DEFAULT 'LKR',
    tech_stack       TEXT[],
    status           VARCHAR(20) DEFAULT 'PENDING',
    submitted_at     TIMESTAMP DEFAULT now(),
    approved_at      TIMESTAMP
    -- NOTE: No user_id here — fully anonymous
);

CREATE INDEX IF NOT EXISTS idx_submissions_status  ON salary.submissions(status);
CREATE INDEX IF NOT EXISTS idx_submissions_country ON salary.submissions(country);
CREATE INDEX IF NOT EXISTS idx_submissions_title   ON salary.submissions(job_title);

-- ============================================================
-- COMMUNITY SCHEMA
-- ============================================================
CREATE SCHEMA IF NOT EXISTS community;

CREATE TABLE IF NOT EXISTS community.votes (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id UUID REFERENCES salary.submissions(id) ON DELETE CASCADE,
    user_id       UUID NOT NULL,
    vote_type     VARCHAR(10) NOT NULL CHECK (vote_type IN ('UP', 'DOWN')),
    voted_at      TIMESTAMP DEFAULT now(),
    UNIQUE(submission_id, user_id)
);

CREATE TABLE IF NOT EXISTS community.reports (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id UUID REFERENCES salary.submissions(id) ON DELETE CASCADE,
    user_id       UUID NOT NULL,
    reason        TEXT,
    reported_at   TIMESTAMP DEFAULT now()
);

-- ============================================================
-- SEED DATA (for local dev testing)
-- ============================================================
INSERT INTO salary.submissions (
    job_title, company, anonymize, country, city,
    experience_years, level, gross_salary, currency,
    tech_stack, status
) VALUES
(
    'Software Engineer', 'WSO2', true,
    'Sri Lanka', 'Colombo', 3, 'MID',
    250000.00, 'LKR', ARRAY['Java','Spring Boot','Kubernetes'],
    'PENDING'
),
(
    'DevOps Engineer', 'Sysco Labs', false,
    'Sri Lanka', 'Colombo', 5, 'SENIOR',
    380000.00, 'LKR', ARRAY['Kubernetes','Terraform','AWS'],
    'PENDING'
),
(
    'Frontend Developer', 'IFS', true,
    'Sri Lanka', 'Colombo', 2, 'JUNIOR',
    150000.00, 'LKR', ARRAY['React','TypeScript','TailwindCSS'],
    'PENDING'
);

\echo '✅ Schema and seed data created successfully'
