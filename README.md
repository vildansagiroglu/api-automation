# API Test Automation – Java · RestAssured · JUnit 5

[![API tests](https://github.com/vildansagiroglu/api-automation/actions/workflows/api-tests.yml/badge.svg)](https://github.com/vildansagiroglu/api-automation/actions/workflows/api-tests.yml)

REST API test suite written with **RestAssured** and **JUnit 5**, running automatically in **GitHub Actions** on every push, every pull request and once a week.

It tests two public APIs:

| API | What is covered |
| --- | --- |
| [Restful-Booker](https://restful-booker.herokuapp.com/apidoc/index.html) | Full CRUD flow (create, read, filter, update, partial update, delete), token authentication, negative and security cases, JSON schema validation |
| [Swagger Codegen](https://generator.swagger.io) | Listing client languages and generating a Java SDK from an OpenAPI spec |

## Test design

- **Independent tests** – every test creates its own data with a unique name, so tests can run in any order or in parallel.
- **Positive and negative paths** – wrong credentials, missing or invalid token (403), unknown id (404), and a check that a rejected delete really leaves the data in place.
- **Contract checks** – responses are validated against JSON schemas in `src/test/resources/schemas`.
- **Readable failures** – request and response are logged only when an assertion fails.
- **Configurable** – base URLs and credentials can be passed as system properties, so the same suite runs against another environment.

## Project structure

```
src/test/java/io/github/vildansagiroglu/api
├── core/ApiSpecs.java              # base URLs, shared request specification
├── booking/BookingData.java        # test data builder
├── booking/BookingClient.java      # helper calls (create booking, get token)
├── booking/BookingCrudTest.java    # happy-path CRUD tests
├── booking/BookingNegativeTest.java# auth, 403 / 404 and security checks
└── swagger/SwaggerGeneratorTest.java
src/test/resources/schemas          # JSON schemas for contract validation
.github/workflows/api-tests.yml     # CI pipeline
```

## Running locally

Requirements: Java 21 and Maven 3.9+.

```bash
mvn test

# against another environment
mvn test -Dbooking.baseUrl=http://localhost:3001 -Dbooking.user=admin -Dbooking.password=secret
```

Reports are written to `target/surefire-reports`. In GitHub Actions, results appear as a check on each run and the raw reports can be downloaded as an artifact.

## Author

**Vildan Sağıroğlu** – QA Automation Engineer · [LinkedIn](https://www.linkedin.com/in/vildansagiroglu)
