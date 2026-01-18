# Diary Application Project Plan

## 1. Project Overview
A Diary application allowing users to create, read, update, and delete diary entries. The application is a multi-module Maven project consisting of a React frontend (packaged as a WebJar), a Spring Boot backend, and a dedicated Spring Boot Authorization Server.

## 2. Architecture
The project follows a microservices-lite architecture within a monorepo.

### Modules
*   **`diary-parent`**: Root module managing dependencies and build configuration.
*   **`oauth-server`**: Spring Boot Authorization Server (OIDC/OAuth2).
*   **`diary-frontend`**: React application (TypeScript) built with `frontend-maven-plugin` and packaged as a WebJar.
*   **`diary-backend`**: Spring Boot Resource Server that includes `diary-frontend` as a dependency and serves the UI.

## 3. Technologies & Versions
*   **Java**: 21 (LTS)
*   **Spring Boot**: 3.3.x (Latest Stable)
*   **Node.js**: v20 (LTS)
*   **Frontend**: React, TypeScript, Bootstrap 5
*   **Build Tool**: Maven
*   **Frontend Build**: `frontend-maven-plugin`
*   **Containerization**: Google Jib (for OAuth Server)
*   **Integration Testing**: Testcontainers

## 4. Module Details

### 4.1. Root (`diary-parent`)
*   **Role**: Aggregator and Parent POM.
*   **Configuration**:
    *   Java 21.
    *   Spring Boot 3.3.x dependencies.
    *   Plugin management for `frontend-maven-plugin`.

### 4.2. OAuth Server (`oauth-server`)
*   **Role**: Centralized Authentication Authority.
*   **Tech**: Spring Authorization Server.
*   **Features**:
    *   OIDC/OAuth2 Provider.
    *   Login Page (Custom or Default).
    *   User Management (In-memory or Database).
*   **Build**:
    *   **Jib Plugin**: Configured to build a Docker image of the OAuth Server (`oauth-server:latest`) to be used in integration tests.

### 4.3. Frontend (`diary-frontend`)
*   **Role**: User Interface.
*   **Tech**: React, TypeScript, Bootstrap 5.
*   **Rules**: Adheres strictly to `gemini.md`:
    *   **Language**: TypeScript (No `any`).
    *   **State**: Functional Components & Hooks.
    *   **Data Fetching**: TanStack Query.
    *   **Forms**: React Hook Form + Zod.
    *   **Styling**: Bootstrap 5 (Utility classes, `react-bootstrap`).
*   **Build**:
    *   Uses `frontend-maven-plugin` to install Node/NPM and build the app.
    *   **Packaging**: Packages build artifacts (e.g., `dist/` or `build/`) into `META-INF/resources` inside a JAR file (WebJar format).

### 4.4. Backend (`diary-backend`)
*   **Role**: Resource Server & UI Host.
*   **Tech**: Spring Boot Web, Spring Data JPA, H2/PostgreSQL.
*   **Features**:
    *   **API**: REST endpoints for Diary operations.
    *   **Security**: Validates JWTs from `oauth-server`.
    *   **UI Hosting**: Depends on `diary-frontend` module. Spring Boot automatically serves static resources from the included WebJar.
*   **Testing**:
    *   **Unit**: JUnit 5, Mockito.
    *   **Integration**: `RestAssured` for API testing.
    *   **Testcontainers**: Uses `Testcontainers` to run the `oauth-server` Docker image during integration/E2E tests to ensure full security flow verification.
    *   **E2E**: Selenium WebDriver with Page Object Model (POM) located in `src/test/java`.

## 5. Testing Strategy

### 5.1. Unit Tests
*   **Backend**: Service and Controller layer tests using JUnit 5 and Mockito.
*   **Frontend**: Vitest/Jest and React Testing Library (as per modern React standards).

### 5.2. Integration Tests (Backend)
*   **Tool**: RestAssured + Testcontainers.
*   **Setup**: Spins up `oauth-server` using Testcontainers.
*   **Scope**: Test REST API endpoints (`GET`, `POST`, `PUT`, `DELETE`) ensuring correct status codes, payloads, and security constraints.

### 5.3. User Interface Tests (E2E)
*   **Location**: `diary-backend` module (`src/test/java`).
*   **Tool**: Selenium WebDriver.
*   **Pattern**: Page Object Model (POM).
*   **Scenario**:
    1.  Start `oauth-server` using Testcontainers.
    2.  Start `diary-backend` (Spring Boot Test Context).
    3.  Selenium navigates to the app URL.
    4.  Perform Login (redirects to OAuth server container and back).
    5.  Create/Edit/Delete diary entries.
    6.  Verify UI updates.

## 6. Implementation Plan
1.  **Project Setup**: Configure `pom.xml` with modules and versions.
2.  **OAuth Server**: 
    *   Implement basic OIDC configuration.
    *   Configure **Jib Maven Plugin** to build the Docker image.
3.  **Frontend**:
    *   Initialize React TypeScript project.
    *   Configure `frontend-maven-plugin` in `pom.xml`.
    *   Implement UI following `gemini.md`.
4.  **Backend**:
    *   Create API.
    *   Add `diary-frontend` dependency.
    *   Configure Resource Server security.
    *   Configure **Testcontainers** to use the `oauth-server` image.
5.  **Testing**:
    *   Write RestAssured tests for API.
    *   Write Selenium POM tests in `diary-backend` to test the user interface.
