# Diary Application Project Plan

## 1. Project Overview
A Diary application allowing users to create, read, update, and delete diary entries. The application will use a React frontend, a Spring Boot backend, and a dedicated Spring Boot Authorization Server for security.

## 2. Architecture
The project will be a multi-module Maven project.

### Modules
*   **`diary-parent`**: Root module containing the parent POM to manage dependencies and build configuration.
*   **`oauth-server`**: Spring Boot Authorization Server responsible for user authentication and token issuance (OIDC/OAuth2).
*   **`diary-backend`**: Spring Boot Resource Server providing the REST API for diary entries.
*   **`diary-frontend`**: React application using Bootstrap, built and packaged using the `frontend-maven-plugin`.

## 3. Technologies & Versions
*   **Java**: Java 21 (Latest LTS)
*   **Spring Boot**: 3.3.x (Latest stable)
*   **Node.js**: v20 (Latest LTS)
*   **Frontend Framework**: React
*   **Styling**: Bootstrap 5
*   **Build Tool**: Maven
*   **Frontend Build**: `frontend-maven-plugin`
*   **Database**: H2 (for dev/test) or PostgreSQL (production)

## 4. Module Details

### 4.1. Root (`diary-parent`)
*   Defines common properties (Java version, Spring Boot version).
*   Manages dependency versions (BOMs).
*   Configures aggregation of sub-modules.

### 4.2. OAuth Server (`oauth-server`)
*   **Dependencies**: `spring-boot-starter-oauth2-authorization-server`, `spring-boot-starter-security`, `spring-boot-starter-web`.
*   **Functionality**:
    *   Manages registered clients (Frontend).
    *   Handles user login.
    *   Issues JWT access tokens and ID tokens.

### 4.3. Backend (`diary-backend`)
*   **Dependencies**: `spring-boot-starter-web`, `spring-boot-starter-oauth2-resource-server`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`.
*   **Functionality**:
    *   Exposes REST endpoints (e.g., `GET /entries`, `POST /entries`).
    *   Validates JWT tokens from the OAuth Server.
    *   Persists diary entries.

### 4.4. Frontend (`diary-frontend`)
*   **Dependencies**: `frontend-maven-plugin` (in `pom.xml`).
*   **Stack**: React, React Router, Bootstrap.
*   **Functionality**:
    *   UI for viewing and editing diary entries.
    *   Handles OAuth2 Authorization Code flow with PKCE to log in via `oauth-server`.
    *   Calls `diary-backend` APIs with the access token.

## 5. Testing Strategy

### 5.1. Unit Tests
*   **Backend**: JUnit 5 and Mockito for Services and Controllers.
*   **Frontend**: Vitest and React Testing Library for components.

### 5.2. Integration Tests
*   **Backend**: `@SpringBootTest` with `RestAssured` to test API endpoints and security configuration.
*   **Database**: Test persistence layer with H2.

### 5.3. UI Tests (E2E)
*   **Tool**: Selenium WebDriver.
*   **Pattern**: Page Object Model (POM).
*   **Scope**:
    *   Login flow.
    *   Creating a new entry.
    *   Verifying entry appears in the list.
    *   Logout.

## 6. Implementation Steps
1.  **Project Initialization**: Create the parent POM and sub-module structure.
2.  **OAuth Server Setup**: Configure the Authorization Server, user details service, and registered client.
3.  **Backend Development**: Create the entity model, repository, service, and controller. Configure Resource Server security.
4.  **Frontend Development**: Initialize React app, install Bootstrap, implement login logic, and create UI components.
5.  **Integration**: Connect Frontend to OAuth Server and Backend.
6.  **Testing**: Write unit, integration, and Selenium tests.
