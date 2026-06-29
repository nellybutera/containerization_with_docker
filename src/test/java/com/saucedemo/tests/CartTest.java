package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    private static final String ITEM_1 = "Sauce Labs Backpack";
    private static final String ITEM_2 = "Sauce Labs Bike Light";

    private InventoryPage loggedIn() {
        return new LoginPage(driver).loginAs("standard_user", "secret_sauce");
    }

    @Test
    public void tc06_addSingleItemUpdatesCartBadge() {
        InventoryPage inventory = loggedIn();
        inventory.addItemToCart(ITEM_1);
        Assert.assertEquals(inventory.getCartCount(), 1,
                "Cart badge should show 1 after adding one item");
    }

    @Test
    public void tc07_addTwoItemsBadgeShowsTwo() {
        InventoryPage inventory = loggedIn();
        inventory.addItemToCart(ITEM_1);
        inventory.addItemToCart(ITEM_2);
        Assert.assertEquals(inventory.getCartCount(), 2,
                "Cart badge should show 2 after adding two items");
    }

    @Test
    public void tc08_removeItemDecreasesCartCount() {
        InventoryPage inventory = loggedIn();
        inventory.addItemToCart(ITEM_1);
        inventory.addItemToCart(ITEM_2);
        inventory.removeItemFromCart(ITEM_1);
        Assert.assertEquals(inventory.getCartCount(), 1,
                "Cart badge should show 1 after removing one of two items");
    }

    @Test
    public void tc09_cartPageShowsAddedItem() {
        InventoryPage inventory = loggedIn();
        inventory.addItemToCart(ITEM_1);
        CartPage cart = inventory.openCart();
        Assert.assertTrue(cart.isItemPresent(ITEM_1),
                "Cart page should list the added item");
    }

    @Test
    public void tc10_removeItemFromCartPageEmptiesCart() {
        InventoryPage inventory = loggedIn();
        inventory.addItemToCart(ITEM_1);
        CartPage cart = inventory.openCart();
        cart.removeItem(ITEM_1);
        Assert.assertEquals(cart.getItemCount(), 0,
                "Cart should be empty after removing the only item");
    }
}
