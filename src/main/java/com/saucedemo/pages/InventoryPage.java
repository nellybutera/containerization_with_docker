package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class InventoryPage extends BasePage {

    private static final By CART_BADGE      = By.className("shopping_cart_badge");
    private static final By CART_LINK       = By.className("shopping_cart_link");
    private static final By INVENTORY_ITEMS = By.className("inventory_item");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public void addItemToCart(String itemName) {
        By btn = By.xpath(
            "//div[contains(@class,'inventory_item') and .//div[text()='" + itemName + "']]" +
            "//button[contains(@class,'btn_inventory')]"
        );
        waitForClickable(btn).click();
    }

    public void removeItemFromCart(String itemName) {
        By btn = By.xpath(
            "//div[contains(@class,'inventory_item') and .//div[text()='" + itemName + "']]" +
            "//button[contains(text(),'Remove')]"
        );
        waitForClickable(btn).click();
    }

    public int getCartCount() {
        List<?> badges = driver.findElements(CART_BADGE);
        return badges.isEmpty() ? 0 : Integer.parseInt(driver.findElement(CART_BADGE).getText());
    }

    public int getInventoryItemCount() {
        return driver.findElements(INVENTORY_ITEMS).size();
    }

    public CartPage openCart() {
        waitForClickable(CART_LINK).click();
        return new CartPage(driver);
    }
}
