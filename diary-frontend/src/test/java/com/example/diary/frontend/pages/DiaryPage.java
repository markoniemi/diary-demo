package com.example.diary.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DiaryPage {
    private WebDriver driver;
    private By titleInput = By.cssSelector("input[placeholder='Title']");
    private By contentInput = By.cssSelector("textarea[placeholder='Content']");
    private By submitButton = By.xpath("//button[text()='Add Entry']");

    public DiaryPage(WebDriver driver) {
        this.driver = driver;
    }

    public void createEntry(String title, String content) {
        driver.findElement(titleInput).sendKeys(title);
        driver.findElement(contentInput).sendKeys(content);
        driver.findElement(submitButton).click();
    }
}
