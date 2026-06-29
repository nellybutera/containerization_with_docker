package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutReviewPage extends BasePage {

    private static final By FINISH_BUTTON = By.id("finish");
    private static final By ITEM_TOTAL    = By.className("summary_subtotal_label");

    public CheckoutReviewPage(WebDriver driver) {
        super(driver);
    }

    public String getItemTotal() {
        return waitForVisible(ITEM_TOTAL).getText();
    }

    public CheckoutCompletePage clickFinish() {
        waitForClickable(FINISH_BUTTON).click();
        return new CheckoutCompletePage(driver);
    }
}
