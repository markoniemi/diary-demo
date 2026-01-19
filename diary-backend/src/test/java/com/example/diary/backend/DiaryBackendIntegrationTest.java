package com.example.diary.backend;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
public class DiaryBackendIntegrationTest {

    @LocalServerPort
    private int port;

    @Container
    public static GenericContainer<?> oauthServer = new GenericContainer<>("oauth-server:latest")
            .withExposedPorts(9000);

    private static String issuerUri;

    @BeforeAll
    public static void beforeAll() {
        oauthServer.start();
        issuerUri = "http://" + oauthServer.getHost() + ":" + oauthServer.getMappedPort(9000);
        System.setProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri", issuerUri);
    }

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void whenGetAllEntries_withoutToken_thenUnauthorized() {
        given()
            .when()
            .get("/api/entries")
            .then()
            .statusCode(401);
    }

    @Test
    public void whenGetAllEntries_withToken_thenOk() {
        String token = getAccessToken("user", "password");

        given()
            .auth().oauth2(token)
            .when()
            .get("/api/entries")
            .then()
            .statusCode(200);
    }

    private String getAccessToken(String username, String password) {
        RestTemplate restTemplate = new RestTemplate();
        String tokenEndpoint = issuerUri + "/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "diary-client");
        params.add("client_secret", "secret");
        params.add("username", username);
        params.add("password", password);
        params.add("scope", "diary.read");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        Map<String, Object> response = restTemplate.postForObject(tokenEndpoint, request, Map.class);
        return (String) response.get("access_token");
    }
}
