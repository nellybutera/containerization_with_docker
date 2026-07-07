package com.saucedemo.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME      = By.id("user-name");
    private static final By PASSWORD      = By.id("password");
    private static final By LOGIN_BUTTON  = By.id("login-button");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Log in as '{username}'")
    public InventoryPage loginAs(String username, String password) {
        waitForVisible(USERNAME).clear();
        driver.findElement(USERNAME).sendKeys(username);
        driver.findElement(PASSWORD).clear();
        driver.findElement(PASSWORD).sendKeys(password);
        waitForClickable(LOGIN_BUTTON).click();
        return new InventoryPage(driver);
    }

    public boolean hasError() {
        return !driver.findElements(ERROR_MESSAGE).isEmpty();
    }

    public String getErrorMessage() {
        return waitForVisible(ERROR_MESSAGE).getText();
    }
}
