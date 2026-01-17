package com.example.diary.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private WebDriver driver;
    private By loginButton = By.xpath("//button[text()='Log in']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }
}
