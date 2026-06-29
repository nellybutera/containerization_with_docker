package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    private static final By CART_ITEMS       = By.className("cart_item");
    private static final By CHECKOUT_BUTTON  = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getItemCount() {
        return driver.findElements(CART_ITEMS).size();
    }

    public boolean isItemPresent(String itemName) {
        return !driver.findElements(
            By.xpath("//div[@class='cart_item']//div[@class='inventory_item_name' and text()='" + itemName + "']")
        ).isEmpty();
    }

    public void removeItem(String itemName) {
        By removeBtn = By.xpath(
            "//div[@class='cart_item' and .//div[text()='" + itemName + "']]" +
            "//button[contains(text(),'Remove')]"
        );
        waitForClickable(removeBtn).click();
    }

    public CheckoutInfoPage clickCheckout() {
        waitForClickable(CHECKOUT_BUTTON).click();
        return new CheckoutInfoPage(driver);
    }
}
