# Grocery Stock Management

A small grocery store stock management system built to demonstrate enterprise Java and React skills.

- **Backend** - Java 21, Jakarta EE 10 and MicroProfile 6 on **Open Liberty** (IBM runtime, IBM Container Registry base image), Maven multi-module build.
- **Frontend** - React 18 with Vite. Plain React, no meta-framework.
- **Database** - PostgreSQL via JPA.
- **Hosting** - Docker Desktop, local only.

## Features

- JWT based login using MicroProfile JWT, RS256 signed. The key pair is generated during the image build.
- Three access levels enforced with `@RolesAllowed`:
  - `ADMIN` - everything, including user administration.
  - `MANAGER` - items, stock movements, sales history.
  - `CASHIER` - browse items and record sales at the till.
- Item catalogue with SKU, category, price and reorder level.
- Stock movements ledger. Receipts, returns, wastage and adjustments all pass through one code path.
- Till screen that records a sale and decrements stock in the same transaction, refusing to oversell.
- Dashboard with stock value, units on hand, sales today and a reorder list.

## Run it

```
docker compose build
docker compose up -d
```

- Frontend: http://localhost:3000
- API: http://localhost:9080/api
- Health: http://localhost:9080/health

The first startup creates an administrator from `SEED_ADMIN_USERNAME` and `SEED_ADMIN_PASSWORD`
(defaults `admin` / `Admin123!`) plus a small starter catalogue. Copy `.env.example` to `.env` to
change the local values.

Stop everything with:

```
docker compose down
```

## Project layout

```
backend/
  grocery-contracts   request and response records
  grocery-domain      JPA entities, enums, persistence unit
  grocery-core        repositories, services, security, mappers
  grocery-api         JAX-RS resources, Liberty server configuration
frontend/
  src/api             one module per API area
  src/components      shared presentation pieces
  src/features        one folder per feature, one file per piece
  src/hooks           auth context and data loading hook
```

## Backend tests

```
cd backend
mvn test
```

Rules for AI agents working in this repository are in `AGENTS.md`.
