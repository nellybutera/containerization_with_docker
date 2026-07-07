package com.saucedemo.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartPage extends BasePage {

    private static final By CART_ITEMS      = By.className("cart_item");
    private static final By CHECKOUT_BUTTON = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getItemCount() {
        return driver.findElements(CART_ITEMS).size();
    }

    public boolean isItemPresent(String itemName) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "//div[contains(@class,'cart_item')]" +
                    "//div[contains(@class,'inventory_item_name') and text()='" + itemName + "']"
                ))
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Remove '{itemName}' from cart page")
    public void removeItem(String itemName) {
        By cartRow = By.xpath(
            "//div[contains(@class,'cart_item') and .//div[text()='" + itemName + "']]"
        );
        By removeBtn = By.xpath(
            "//div[contains(@class,'cart_item') and .//div[text()='" + itemName + "']]" +
            "//button[contains(text(),'Remove')]"
        );
        waitForClickable(removeBtn).click();
        // Wait for the item's row to leave the DOM before returning — confirms the
        // removal has actually rendered before any subsequent getItemCount() call.
        wait.until(ExpectedConditions.invisibilityOfElementLocated(cartRow));
    }

    @Step("Click Checkout")
    public CheckoutInfoPage clickCheckout() {
        wait.until(ExpectedConditions.urlContains("/cart.html"));
        waitForClickable(CHECKOUT_BUTTON).click();
        return new CheckoutInfoPage(driver);
    }
}
