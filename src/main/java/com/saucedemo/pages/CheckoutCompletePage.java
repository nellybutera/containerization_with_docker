package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    private static final By COMPLETE_HEADER = By.className("complete-header");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public String getHeader() {
        return waitForVisible(COMPLETE_HEADER).getText();
    }

    public boolean isOrderComplete() {
        return getHeader().contains("Thank you");
    }
}
