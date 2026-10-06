# Happy Tree News
#### Live link: https://asus-laptop.tail5942de.ts.net/swagger-ui/index.html#/article-controller/list

A news aggregator that collects articles from public APIs and distributes them through **RabbitMQ** to independent consumers. Built to explore event-driven design.

**Stack:** Java 25 · Spring Boot 4.1 · RabbitMQ · PostgreSQL · Flyway · Docker

    News APIs → news-ingestor → RabbitMQ (news.articles) → article-store → Frontend
*Built to explore event-driven design with Spring Boot 4 and RabbitMQ.*

## News-Ingestor
A **Spring Boot** service that:

- Polls public news sources (the Guardian, Hacker News) on a schedule
- Maps each source's format into one common **Article** model (one adapter per source)
- Publishes new articles to **RabbitMQ**, the producer side of the system
- Isolates failures: a misconfigured source disables itself at startup instead of
  crashing the app, and a malformed article is skipped without losing the rest of its batch
- Deduplicates articles before publishing, using a bounded in-memory **Caffeine** cache.
- Adapters are unit-tested offline with **MockRestServiceServer** (the tests caught a bug
  where one malformed date discarded a whole batch)

*Design note:* the dedup cache is lost on restart, so a restart re-publishes one batch.
That's acceptable because deduplication here is only an optimization: the article store
stores articles idempotently, so duplicates are harmless. This avoids running **Redis**;
a shared store like Redis would become necessary when running several ingestor instances. 

## Article-store

A **Spring Boot** service that consumes articles from **RabbitMQ**, stores them in
**PostgreSQL**, and serves them through a paged **REST API**:

- Consumes from its own queue, bound to the `news.articles` topic exchange
  (the consumer owns its queue; the ingestor never knows it exists)
- Stores articles **idempotently**: a unique constraint plus `INSERT ... ON CONFLICT DO NOTHING`,
  so redelivered or duplicate messages are absorbed atomically, with no race conditions
- Handles failures with **retries and exponential backoff**, then moves messages that still
  fail to a **dead-letter queue**, instead of redelivering them forever
- Manages the schema with versioned **Flyway** migrations, validated by Hibernate at startup
- Exposes `GET /api/articles`, newest first, with paging, a maximum page size,
  and filtering by source, documented with **OpenAPI / Swagger UI**

*Design note:* duplicates are expected by design. RabbitMQ delivers at least once, and the
ingestor re-publishes after a restart, so the guarantee lives in the database. A check-then-insert
would race under concurrent consumers; `ON CONFLICT` makes "insert if absent" a single atomic
operation. The trade-off is PostgreSQL-specific SQL.

## Running it

Requires **Docker**. Copy `.env.example` to `.env` and add a free
[Guardian API key](https://open-platform.theguardian.com/access/). Then:

    docker compose --profile apps up -d --build

This builds both services and starts them with RabbitMQ and PostgreSQL.
Swagger UI: http://localhost:8081/swagger-ui/index.html

For development, `docker compose up -d` starts only the infrastructure,
and the services run from the IDE.

## Deployment

The live demo runs on a **self-managed Linux server** with **Docker Compose**:
restart policies, log rotation, and every port bound to localhost only.
The API is exposed over HTTPS through a **Tailscale Funnel** tunnel, so no ports are
opened on the network.