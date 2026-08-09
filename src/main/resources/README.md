# Simple Wallet API

A RESTful wallet management API built with Spring Boot, Spring Data JPA, PostgreSQL, Bean Validation, and a layered architecture.

The API allows users to:

* create wallets
* retrieve a wallet by ID
* retrieve all wallets
* deposit money
* withdraw money
* close a wallet

The project focuses on separating HTTP concerns, business logic, persistence, DTOs, validation, mapping, and exception handling.

---

## Tech Stack

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* PostgreSQL
* Bean Validation
* Maven
* JUnit 5
* Mockito

---

## Architecture

The application follows a layered structure:

```text
Client
  |
  v
Controller
  |
  v
Service
  |
  v
Repository
  |
  v
PostgreSQL
```

DTOs are used at the API boundary, and a mapper converts entities into response DTOs.

```text
Request JSON
   |
   v
Request DTO
   |
   v
Controller
   |
   v
Service
   |
   v
Repository
   |
   v
Database

Database Entity
   |
   v
Mapper
   |
   v
Response DTO
   |
   v
JSON Response
```

---

## Package Structure

```text
com.anish.wallet_api

controller
dto
exception
mapper
model
repository
service
```

### Responsibilities

**Controller**

Handles HTTP requests and responses.

**Service**

Contains business rules and application logic.

**Repository**

Handles persistence using Spring Data JPA.

**DTO**

Defines the request and response structures exposed by the API.

**Mapper**

Converts `Wallet` entities into `WalletResponse` DTOs.

**Exception**

Contains custom exceptions and the global exception handler.

**Model**

Contains the JPA entity and wallet status enum.

---

## Wallet Model

A wallet contains:

```text
id
ownerName
balance
status
createdAt
```

Wallet status can be:

```text
ACTIVE
CLOSED
```

`BigDecimal` is used for monetary values to avoid floating-point precision problems.

---

## API Endpoints

### Create Wallet

```http
POST /wallets
```

Request:

```json
{
  "ownerName": "Anish",
  "openingBalance": 100.00
}
```

Response:

```json
{
  "id": 1,
  "ownerName": "Anish",
  "balance": 100.00,
  "status": "ACTIVE",
  "createdAt": "2026-08-09T14:26:00"
}
```

Status:

```text
201 Created
```

---

### Get Wallet

```http
GET /wallets/{id}
```

Example:

```http
GET /wallets/1
```

Response:

```json
{
  "id": 1,
  "ownerName": "Anish",
  "balance": 100.00,
  "status": "ACTIVE",
  "createdAt": "2026-08-09T14:26:00"
}
```

Status:

```text
200 OK
```

---

### Get All Wallets

```http
GET /wallets
```

Response:

```json
[
  {
    "id": 1,
    "ownerName": "Anish",
    "balance": 100.00,
    "status": "ACTIVE",
    "createdAt": "2026-08-09T14:26:00"
  }
]
```

Status:

```text
200 OK
```

---

### Deposit Money

```http
POST /wallets/{id}/deposits
```

Request:

```json
{
  "amount": 50.00
}
```

Example response:

```json
{
  "id": 1,
  "ownerName": "Anish",
  "balance": 150.00,
  "status": "ACTIVE",
  "createdAt": "2026-08-09T14:26:00"
}
```

Status:

```text
200 OK
```

---

### Withdraw Money

```http
POST /wallets/{id}/withdrawals
```

Request:

```json
{
  "amount": 25.00
}
```

Example response:

```json
{
  "id": 1,
  "ownerName": "Anish",
  "balance": 125.00,
  "status": "ACTIVE",
  "createdAt": "2026-08-09T14:26:00"
}
```

Status:

```text
200 OK
```

---

### Close Wallet

```http
PATCH /wallets/{id}/close
```

A wallet can only be closed when its balance is zero.

Example response:

```json
{
  "id": 1,
  "ownerName": "Anish",
  "balance": 0.00,
  "status": "CLOSED",
  "createdAt": "2026-08-09T14:26:00"
}
```

Status:

```text
200 OK
```

---

## Business Rules

### Creating a Wallet

* `ownerName` is required.
* `openingBalance` cannot be negative.
* The server generates the wallet ID.
* A new wallet starts with status `ACTIVE`.
* The server generates `createdAt`.

### Deposits

* The wallet must exist.
* The wallet must be `ACTIVE`.
* The deposit amount must be greater than zero.
* The deposited amount is added to the current balance.

### Withdrawals

* The wallet must exist.
* The wallet must be `ACTIVE`.
* The withdrawal amount must be greater than zero.
* The withdrawal amount cannot exceed the current balance.

### Closing a Wallet

* The wallet must exist.
* The wallet must be `ACTIVE`.
* The wallet balance must be exactly zero.
* Closing changes the status to `CLOSED`.

---

## Validation

Bean Validation is used on request DTOs.

Example:

```java
@NotBlank(message = "Owner name is required")
private String ownerName;
```

```java
@NotNull(message = "Opening balance is required")
@DecimalMin(
    value = "0.00",
    inclusive = true,
    message = "Opening balance cannot be negative"
)
private BigDecimal openingBalance;
```

Money operations require a positive amount.

```java
@NotNull(message = "Amount is required")
@DecimalMin(
    value = "0.01",
    message = "Amount must be greater than zero"
)
private BigDecimal amount;
```

---

## Error Handling

The application uses a global exception handler with:

```java
@RestControllerAdvice
```

Errors are returned using a consistent `ApiError` structure.

Example:

```json
{
  "timestamp": "2026-08-09T14:29:57",
  "status": 400,
  "error": "Bad Request",
  "messages": [
    "Withdrawal amount exceeds current balance"
  ],
  "path": "/wallets/1/withdrawals"
}
```

### Exception Mapping

```text
WalletNotFoundException
→ 404 Not Found

InvalidWalletStateException
→ 409 Conflict

IllegalArgumentException
→ 400 Bad Request

MethodArgumentNotValidException
→ 400 Bad Request

Unexpected Exception
→ 500 Internal Server Error
```

Unexpected internal errors return a generic message instead of exposing implementation details.

---

## Database

This project uses PostgreSQL with Spring Data JPA.

`WalletRepository` extends:

```java
JpaRepository<Wallet, Long>
```

This provides standard persistence operations such as:

```text
save(...)
findById(...)
findAll()
deleteById(...)
existsById(...)
```

The service layer uses these repository operations while keeping business rules outside the persistence layer.

---

## Running Locally

### Requirements

Make sure you have:

* Java installed
* PostgreSQL running
* a PostgreSQL database configured for the application

Configure your connection in:

```text
src/main/resources/application.properties
```

Example configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/walletdb
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

Do not commit real passwords or sensitive credentials to GitHub.

---

### Start the Application

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The API runs by default at:

```text
http://localhost:8080
```

---

## curl Examples

### Create Wallet

```bash
curl -i -X POST http://localhost:8080/wallets \
  -H "Content-Type: application/json" \
  -d '{"ownerName":"Anish","openingBalance":100}'
```

### Get Wallet

```bash
curl -i http://localhost:8080/wallets/1
```

### Get All Wallets

```bash
curl -i http://localhost:8080/wallets
```

### Deposit

```bash
curl -i -X POST http://localhost:8080/wallets/1/deposits \
  -H "Content-Type: application/json" \
  -d '{"amount":50}'
```

### Withdraw

```bash
curl -i -X POST http://localhost:8080/wallets/1/withdrawals \
  -H "Content-Type: application/json" \
  -d '{"amount":25}'
```

### Test Insufficient Balance

```bash
curl -i -X POST http://localhost:8080/wallets/1/withdrawals \
  -H "Content-Type: application/json" \
  -d '{"amount":1000}'
```

### Close Wallet

```bash
curl -i -X PATCH http://localhost:8080/wallets/1/close
```

---

## Tests

The service layer is tested using JUnit 5 and Mockito.

The tests cover important business rules including:

* retrieving an existing wallet
* handling a missing wallet
* increasing balance after deposit
* rejecting deposits into closed wallets
* decreasing balance after withdrawal
* rejecting withdrawals that exceed the balance
* rejecting wallet closure with a non-zero balance
* successfully closing a zero-balance wallet

Run tests with:

```bash
./mvnw test
```

The service tests use mocked repositories and therefore do not require PostgreSQL.

---

## What I Learned

This project helped reinforce the responsibilities of each layer in a Spring Boot application.

I learned that Spring Data JPA provides database operations such as `findById`, `findAll`, and `save`, while the service layer is still responsible for application-specific business rules.

I also learned how `Optional` can represent the result of a database lookup that may not contain a value and how `orElseThrow()` can convert that situation into a domain-specific exception.

Using DTOs helped separate the public API contract from the JPA entity. Clients cannot directly control fields such as wallet IDs, status, or creation timestamps.

Bean Validation showed how invalid request data can be rejected at the API boundary before reaching business logic.

The project also demonstrated why `BigDecimal` is preferable for monetary values and how `compareTo()` should be used when comparing monetary amounts.

Finally, implementing a global exception handler showed how Java exceptions can be translated into consistent HTTP status codes and structured API error responses.

---

## Future Improvements

Possible future improvements include:

* integration tests using MockMvc
* repository integration tests
* Docker support for PostgreSQL
* database migrations using Flyway or Liquibase
* transaction handling for wallet operations
* concurrency protection for simultaneous deposits and withdrawals
* authentication and wallet ownership
* transaction history
* pagination for wallet listings
* OpenAPI / Swagger documentation

---

## Project Status

Core wallet functionality is implemented and manually tested.

Service-layer unit tests are implemented using JUnit and Mockito.
