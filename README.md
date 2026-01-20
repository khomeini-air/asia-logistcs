# SmartLoad Optimization API

A Spring Boot–based logistics optimization service that selects the optimal combination of orders for a truck while respecting capacity and compatibility constraints.

--- 
## Overview

Given:
- A truck with maximum weight and volume specifications.
- A list of shipment orders.

The service will find the **optimal subset of orders** that:
- Maximizes total payout
- Does not exceed truck weight or volume limits
- Respects route compatibility (same origin & destination)
- Respects hazmat compatibility (hazmat and non-hazmat orders cannot be mixed)

---

## Architecture 
The system is a stateless Spring Boot service exposing a single optimization API.
It follows a layered architecture:
```
Controller → Service (Orchestration) → Redis Cache
```
Redis is used as an in-memory cache to prevent recomputation of identical optimization requests.

---
### Tech Stack

- Java 17
- Spring Boot 3, with Swagger/OpenAPI 3.0 and Actuator-Prometheus integration.
- Redis (cache)
- Docker & Docker Compose

---
### File Structure

```
src/main/java/com/asia/logistics/loads
├── api               # REST API layer
├── component         # Component class: Cache key generator
├── config            # Open API & Redis configuration
├── dto               # API request/response models
├── entity            # Core domain models (Load, Order, Truck)
├── exception         # Global error handling
├── filter            # Request filter (logging filter)
├── mapper            # Entity to DTO mapper
├── service           # Business orchestration
└── validation        # Custom API validations
```

---

## How to Run

### Prerequisites
- Docker
- Docker Compose

### Running with Docker
1. Clone the project into you local environment.
2. Go to the project directory and run the docker command
```bash
docker compose up --build
```

The service will be available at:

```
http://localhost:8080
```
### Access Points

| Endpoint                                    | Purpose                       | Authentication |
|---------------------------------------------|-------------------------------|----------------|
| http://localhost:8080/api/v1/load-optimizer/optimize | Optimize load API             | None (public)  |
| http://localhost:8080/swagger-ui/index.html | Interactive API Documentation | None (public)  |
| http://localhost:8080/v3/api-docs           | OpenAPI Specification (JSON)  | None (public)  |
| http://localhost:8080/actuator/health       | Health Check                  | None (public)  |
| http://localhost:8080/actuator/info         | Application Info              | None (public)     |
| http://localhost:8080/actuator/metrics      | Metrics                       | None (public)     |
| http://localhost:8080/actuator/prometheus   | Prometheus Metrics            | None (public)     |

## Optimize Load API

### Endpoint

```
POST /api/v1/load-optimizer/optimize
```

### Example Request

```bash
curl -X POST http://localhost:8080/api/v1/load-optimizer/optimize \
  -H "Content-Type: application/json" \
  -d @sample-request.json
```

### Example Response
- Successful Response
```
HTTP 200
{
  "truck_id": "truck-003",
  "selected_order_ids": [
    "ord-001",
    "ord-003",
    "ord-004",
    "ord-005",
    "ord-006",
    "ord-007",
    "ord-008",
    "ord-012",
    "ord-013",
    "ord-014",
    "ord-015",
    "ord-016",
    "ord-019"
  ],
  "total_payout_cents": 1573000,
  "total_weight_lbs": 20000,
  "total_volume_cuft": 855,
  "utilization_weight_percent": 100.00,
  "utilization_volume_percent": 85.50
}
```

- Failed Response with various validation errors
```
HTTP 400
{
    "truck.id": "Truck id is missing",
    "orders[2].deliveryDate": "order delivery date must be in the future",
    "orders[2].pickupDate": "order pickup date must be in the future",
    "orders[9]": "pickup date must be before delivery date"
}
```

---

## Optimization Implementation

> **Assumption:**  Pickup date ≤ delivery
date for all orders, and no overlapping time conflicts.

### 1. Hazmat & Non‑Hazmat Partition
Orders are first split into two independent groups:
- Hazmat orders
- Non‑hazmat orders

These groups are never mixed to satisfy safety constraints and hazmat isolation

### 2. Route-Based Partitioning
Each group is further partitioned by:
- Origin and destination
- Hazmat flag

Each partition represents a consistent route and safety category and can be optimized independently.

### 3. Optimization Algorithm (Dynamic Programming + Bitmask)

For each route partition:
- Each order is represented by a bit in a bitmask
- A DP-based 0/1 Knapsack approach is applied
- Constraints:
  - Total weight ≤ truck maximum weight
  - Total volume ≤ truck maximum volume
- Objective: Maximize total payout (revenue) within the capacity constraints.


### 4. Final Load Selection
All optimized route results are compared and the load with the **highest total payout**
is selected as the final optimized load.

---


## Performance

- Handles **22 orders** and more
- Typical execution time: **~50 ms**

---

## Edge Cases Handled

- All parameters are mandatory, return HTTP 400 otherwise.
- No feasible order combination -> HTTP 422.
- Payload too large -> HTTP 413

---

## Bonus Considerations

- Bitmask DP (explicitly encouraged)
- Early pruning for performance
- Redis-based memoization
- Easily extensible to:
  - Pareto-optimal solutions
  - Configurable scoring (revenue vs utilization)
  - Hazmat-capable truck flag

---