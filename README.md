# Credit Risk Engine

Enterprise-grade credit risk analysis engine built with **Kotlin**, **Spring Boot 3**, **DDD**, and **Clean Architecture**.

## Domain concepts

- **CreditApplication** — aggregate root representing a credit request lifecycle
- **CreditScore** — immutable value object encapsulating score and risk classification
- **RiskPolicy** — domain service evaluating application against configurable rules
- **ApplicationStatus** — state machine: `PENDING → UNDER_REVIEW → APPROVED / REJECTED`
- **Domain Events** — published to Kafka on every status transition

## Stack

| Layer          | Technology                  |
|----------------|-----------------------------|
| Language       | Kotlin 1.9                  |
| Framework      | Spring Boot 3.3.4           |
| Database (dev) | H2 in-memory                |
| Messaging      | Apache Kafka 7.6            |
| Cache          | Redis 7.2                   |
| Build          | Maven                       |
| Containers     | Docker Compose              |

## Roadmap

- [x] Phase 0 — Repository setup & project scaffold
- [ ] Phase 1 — Domain layer (Aggregates, Value Objects, Policies, Ports)
- [ ] Phase 2 — Application layer (Use Cases)
- [ ] Phase 3 — Infrastructure layer (JPA, Kafka, Redis)
- [ ] Phase 4 — REST API (Controllers, DTOs, Exception Handling)
- [ ] Phase 5 — Docker Compose & finalization