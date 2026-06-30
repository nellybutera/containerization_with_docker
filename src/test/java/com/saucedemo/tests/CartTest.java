package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.TestConfig;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("Cart")
public class CartTest extends BaseTest {

    private InventoryPage loggedIn() {
        return new LoginPage(driver).loginAs(
                TestConfig.get("user.standard"), TestConfig.get("password.valid"));
    }

    @Test
    @Story("Add to cart")
    @Description("TC-06: Adding one item should update the cart badge to 1")
    public void tc06_addSingleItemUpdatesCartBadge() {
        InventoryPage inventory = loggedIn();
        inventory.addItemToCart(TestConfig.get("item.backpack"));
        Assert.assertEquals(inventory.getCartCount(), 1,
                "Cart badge should show 1 after adding one item");
    }

    @Test
    @Story("Add to cart")
    @Description("TC-07: Adding two different items should update the cart badge to 2")
    public void tc07_addTwoItemsBadgeShowsTwo() {
        InventoryPage inventory = loggedIn();
        inventory.addItemToCart(TestConfig.get("item.backpack"));
        inventory.addItemToCart(TestConfig.get("item.bikelight"));
        Assert.assertEquals(inventory.getCartCount(), 2,
                "Cart badge should show 2 after adding two items");
    }

    @Test
    @Story("Remove from cart")
    @Description("TC-08: Removing one of two items from the inventory page should decrease the badge to 1")
    public void tc08_removeItemDecreasesCartCount() {
        InventoryPage inventory = loggedIn();
        inventory.addItemToCart(TestConfig.get("item.backpack"));
        inventory.addItemToCart(TestConfig.get("item.bikelight"));
        inventory.removeItemFromCart(TestConfig.get("item.backpack"));
        Assert.assertEquals(inventory.getCartCount(), 1,
                "Cart badge should show 1 after removing one of two items");
    }

    @Test
    @Story("Cart page")
    @Description("TC-09: Navigating to the cart page should show the item that was added")
    public void tc09_cartPageShowsAddedItem() {
        InventoryPage inventory = loggedIn();
        inventory.addItemToCart(TestConfig.get("item.backpack"));
        CartPage cart = inventory.openCart();
        Assert.assertTrue(cart.isItemPresent(TestConfig.get("item.backpack")),
                "Cart page should list the added item");
    }

    @Test
    @Story("Remove from cart")
    @Description("TC-10: Removing the only item from the cart page should leave the cart empty")
    public void tc10_removeItemFromCartPageEmptiesCart() {
        InventoryPage inventory = loggedIn();
        inventory.addItemToCart(TestConfig.get("item.backpack"));
        CartPage cart = inventory.openCart();
        cart.removeItem(TestConfig.get("item.backpack"));
        Assert.assertEquals(cart.getItemCount(), 0,
                "Cart should be empty after removing the only item");
    }
}
