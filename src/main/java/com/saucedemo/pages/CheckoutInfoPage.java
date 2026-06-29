package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutInfoPage extends BasePage {

    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME  = By.id("last-name");
    private static final By ZIP        = By.id("postal-code");
    private static final By CONTINUE   = By.id("continue");
    private static final By ERROR      = By.cssSelector("[data-test='error']");

    public CheckoutInfoPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutInfoPage fillFirstName(String value) {
        waitForVisible(FIRST_NAME).sendKeys(value);
        return this;
    }

    public CheckoutInfoPage fillLastName(String value) {
        waitForVisible(LAST_NAME).sendKeys(value);
        return this;
    }

    public CheckoutInfoPage fillZip(String value) {
        waitForVisible(ZIP).sendKeys(value);
        return this;
    }

    /** Click Continue without navigating — stays on this page when validation fails. */
    public CheckoutInfoPage clickContinue() {
        waitForClickable(CONTINUE).click();
        return this;
    }

    /** Happy-path shortcut: fill all fields and continue to the review step. */
    public CheckoutReviewPage submitValidForm(String firstName, String lastName, String zip) {
        fillFirstName(firstName);
        fillLastName(lastName);
        fillZip(zip);
        waitForClickable(CONTINUE).click();
        return new CheckoutReviewPage(driver);
    }

    public String getErrorMessage() {
        return waitForVisible(ERROR).getText();
    }
}
