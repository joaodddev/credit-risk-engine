# Credit Risk Engine

Enterprise-grade credit risk analysis engine built with **Kotlin**, **Spring Boot 3**, **DDD**, and **Clean Architecture**.

## Domain concepts

- **CreditApplication** — aggregate root representing the full credit request lifecycle
- **CreditScore** — immutable value object with risk classification (VERY_LOW → VERY_HIGH)
- **RequestedAmount** — value object with upper limit validation (max R$ 500.000)
- **ApplicationStatus** — explicit state machine: `PENDING → UNDER_REVIEW → APPROVED / REJECTED`
- **RiskPolicy** — domain service interface with two implementations:
    - `MinimumScorePolicy` — rejects score below 400
    - `MaximumAmountPolicy` — limits amount per risk classification
- **Domain Events** — published to Kafka on every status transition

## Stack

| Layer          | Technology                  |
|----------------|-----------------------------|
| Language       | Kotlin 1.9                  |
| Framework      | Spring Boot 3.3.4           |
| Database (dev) | H2 in-memory                |
| Messaging      | Apache Kafka 7.6            |
| Cache          | Redis 7.2 (score cache)     |
| Build          | Maven                       |
| Containers     | Docker Compose              |

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/credit-applications` | Submit a new credit application |
| POST | `/api/v1/credit-applications/{id}/evaluate` | Evaluate and decide on application |
| GET | `/api/v1/credit-applications/{id}` | Get application by ID |
| GET | `/api/v1/credit-applications?applicantDocument=` | List applications by document |

## Request examples

**Submit application**
```json
POST /api/v1/credit-applications
{
  "applicantDocument": "123.456.789-00",
  "requestedAmount": 25000.00
}
```

**Evaluate application**
```json
POST /api/v1/credit-applications/{applicationId}/evaluate
```

**Response — Approved**
```json
{
  "applicationId": "uuid",
  "status": "APPROVED",
  "creditScore": 750,
  "riskClassification": "LOW",
  "rejectionReason": null
}
```

**Response — Rejected**
```json
{
  "applicationId": "uuid",
  "status": "REJECTED",
  "creditScore": 300,
  "riskClassification": "HIGH",
  "rejectionReason": "Credit score 300 is below the minimum threshold of 400"
}
```

## Risk classification table

| Score     | Classification | Max Amount     |
|-----------|----------------|----------------|
| 800–1000  | VERY_LOW       | R$ 500.000     |
| 600–799   | LOW            | R$ 200.000     |
| 400–599   | MEDIUM         | R$ 50.000      |
| 200–399   | HIGH           | Rejected       |
| 0–199     | VERY_HIGH      | Rejected       |

## Error handling

All errors follow **RFC 7807 ProblemDetail**:

```json
{
  "type": "/errors/credit-application-not-found",
  "title": "Credit Application Not Found",
  "status": 404,
  "detail": "Credit application not found: <id>"
}
```

## Domain Events (Kafka)

All events published to topic `credit.risk.events`:

| Event | Trigger |
|-------|---------|
| `credit.application.submitted` | POST /credit-applications |
| `credit.application.under_review` | POST /evaluate |
| `credit.application.approved` | Score passes all policies |
| `credit.application.rejected` | Any policy fails |