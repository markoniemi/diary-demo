package com.example.diary.backend.ui;

import com.example.diary.backend.DiaryBackendIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class DiaryUITest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private static String baseUrl;

    @Container
    public static GenericContainer<?> oauthServer = new GenericContainer<>("oauth-server:latest")
            .withExposedPorts(9000);

    @BeforeAll
    public static void beforeAll() {
        oauthServer.start();
        System.setProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                "http://" + oauthServer.getHost() + ":" + oauthServer.getMappedPort(9000));
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLoginAndSeesDiaryPage() {
        driver.get(baseUrl);

        // Should be redirected to login
        LoginPage loginPage = new LoginPage(driver);
        assertTrue(driver.getCurrentUrl().contains("localhost:" + oauthServer.getMappedPort(9000)));

        // Perform login
        loginPage.login("user", "password");

        // After login, should be redirected back to the diary page
        DiaryPage diaryPage = new DiaryPage(driver);
        assertTrue(diaryPage.getHeaderText().contains("Diary"));
    }
}
