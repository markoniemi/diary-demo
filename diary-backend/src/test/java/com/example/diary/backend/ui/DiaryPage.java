package com.example.diary.backend.ui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DiaryPage {
    private WebDriver driver;

    @FindBy(tagName = "h1")
    private WebElement header;

    @FindBy(xpath = "//button[text()='Log in']")
    private WebElement loginButton;

    public DiaryPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getHeaderText() {
        return header.getText();
    }

    public void clickLogin() {
        loginButton.click();
    }
}
