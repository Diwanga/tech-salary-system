# Tech Salary Transparency System

## Project overview

This is a cloud-native microservices application for anonymous salary transparency: submit salaries, search and filter approved entries, vote on submissions (with authentication), and view aggregated statistics.

The architecture uses a Backend-for-Frontend (BFF) so the browser talks only to the BFF; internal services stay inside the cluster or Docker network.

## High-level flow

User to Frontend to BFF to microservices to PostgreSQL.

## Services

| Component | Role | Stack |
|-----------|------|--------|
| Frontend | Web UI | React 18, Vite, Tailwind CSS |
| BFF | API gateway, routing, JWT for protected routes | Node.js, Express |
| Identity Service | Registration and login | Spring Boot |
| Salary Service | Anonymous salary submissions and approval hooks | Spring Boot |
| Vote Service | Votes on submissions; calls salary service for approval | Spring Boot |
| Search Service | Filter and list approved salaries | Node.js, Express, PostgreSQL (`pg`) |
| Stats Service | Aggregated salary statistics | Node.js, Express, PostgreSQL (`pg`) |

## Database

One PostgreSQL database (`techsalary`) with separate schemas:

- `identity` – users (for voting and auth)
- `salary` – submissions (anonymous; no user id on submissions)
- `community` – votes linked to submissions and users

Schema creation and local seed data live under `services/docker/postgres/init/` (used by `services/docker-compose.yml`). There is also a `database/` directory with its own Postgres init for alternate packaging.

## Technologies

- Frontend: React, Vite, Tailwind CSS, Axios, React Router
- BFF and Node services: Express
- Java services: Spring Boot, Maven
- Data: PostgreSQL 16
- Containers: Docker; orchestration: Kubernetes (manifests in `k8s/`)
- CI/CD: GitHub Actions builds Docker images and applies manifests to Azure Kubernetes Service (AKS) on `main` (see `.github/workflows/deploy.yml`)

## Project structure

```
tech-salary-system/
├── frontend/
├── bff/
├── services/
│   ├── identity-service/
│   ├── salary-service/
│   ├── vote-service/
│   ├── search-service/
│   ├── stats-service/
│   ├── docker/
│   │   └── postgres/init/
│   ├── docker-compose.yml
│   └── salary-service/dev/docker-compose-dev.yml
├── k8s/
│   ├── deploy.sh
│   ├── 00-namespaces.yaml
│   ├── 01-configmap.yaml
│   ├── 02-secret.yaml
│   ├── 03-postgres-secret.yaml
│   ├── data/
│   ├── app/
│   └── ingress.yaml
├── database/
├── .github/workflows/
└── README.md
```

## Local development

### Full stack with Docker Compose

From the repository root:

```bash
cd services
docker compose up --build
```

This starts PostgreSQL, all microservices, BFF, frontend (and optional pgAdmin per the compose file). Default ports include BFF on `3000` and frontend on host port `8088` (see `services/docker-compose.yml` for exact mappings).

### Postgres only (for working on a single Java service)

```bash
docker compose -f services/salary-service/dev/docker-compose-dev.yml up -d
```

Postgres is exposed on host port `5433` to avoid clashing with a local PostgreSQL instance. Point your service at that host and port with the credentials defined in that compose file.

### Running frontend or BFF on the host

Install dependencies in `frontend/` or `bff/` with `npm install`, then use `npm run dev` (frontend) or `npm run dev` with nodemon (BFF). Set environment variables to match the URLs of services you are calling (see each service’s Dockerfile or compose `environment` blocks).

## Kubernetes deployment

Manifests under `k8s/` define namespaces (`app`, `data`), ConfigMaps, Secrets, PostgreSQL in `data`, and deployments for all application services plus ingress.

A helper script at `k8s/deploy.sh` builds images, pushes them (expects Docker Hub and `kubectl` configured), and applies the same manifest set as in CI. For production-like URLs, configure ingress and hosts as documented in that script (for example `techsalary.local` with a hosts file entry when using local clusters).

GitHub Actions on pushes to `main` applies `k8s/` manifests to AKS when repository secrets (`DOCKER_USERNAME`, `DOCKER_PASSWORD`, `KUBE_CONFIG`, etc.) are configured.

## Design notes

- The frontend should call only the BFF, not individual microservices directly in production.
- Salary submission does not require login; voting uses identity (JWT flows through BFF as implemented).
- Search and stats should use only approved submissions where your business rules require it (see salary service documentation under `services/salary-service/README.md`).

## License

This project is developed for academic purposes.
