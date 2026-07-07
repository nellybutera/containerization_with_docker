package com.saucedemo.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class InventoryPage extends BasePage {

    private static final By CART_BADGE      = By.className("shopping_cart_badge");
    private static final By CART_LINK       = By.className("shopping_cart_link");
    private static final By INVENTORY_ITEMS = By.className("inventory_item");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    @Step("Add '{itemName}' to cart from inventory page")
    public void addItemToCart(String itemName) {
        By addBtn = By.xpath(
            "//div[contains(@class,'inventory_item') and .//div[text()='" + itemName + "']]" +
            "//button[contains(text(),'Add to cart')]"
        );
        waitForClickable(addBtn).click();
        // Wait for the button to flip to "Remove" — confirms the add registered in the DOM
        // before any subsequent addItemToCart call reads the badge count.
        By removeBtn = By.xpath(
            "//div[contains(@class,'inventory_item') and .//div[text()='" + itemName + "']]" +
            "//button[contains(text(),'Remove')]"
        );
        waitForVisible(removeBtn);
    }

    @Step("Remove '{itemName}' from cart via inventory page")
    public void removeItemFromCart(String itemName) {
        By removeBtn = By.xpath(
            "//div[contains(@class,'inventory_item') and .//div[text()='" + itemName + "']]" +
            "//button[contains(text(),'Remove')]"
        );
        waitForClickable(removeBtn).click();
    }

    public int getCartCount() {
        List<?> badges = driver.findElements(CART_BADGE);
        return badges.isEmpty() ? 0 : Integer.parseInt(driver.findElement(CART_BADGE).getText());
    }

    public int getInventoryItemCount() {
        return driver.findElements(INVENTORY_ITEMS).size();
    }

    @Step("Open the cart page")
    public CartPage openCart() {
        waitForClickable(CART_LINK).click();
        wait.until(ExpectedConditions.urlContains("/cart.html"));
        return new CartPage(driver);
    }
}
