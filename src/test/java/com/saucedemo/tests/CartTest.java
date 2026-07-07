package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.TestConfig;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("Cart")
public class CartTest extends BaseTest {

    @Step("Given: logged in as standard user")
    private InventoryPage loggedIn() {
        return new LoginPage(driver).loginAs(
                TestConfig.get("user.standard"), TestConfig.get("password.valid"));
    }

    @Test(description = "TC-06: Verify that adding one item updates the cart badge to 1")
    @Story("Add to cart")
    @Severity(SeverityLevel.CRITICAL)
    public void tc06_addSingleItemUpdatesCartBadge() {
        InventoryPage inventory = loggedIn();
        inventory.addItemToCart(TestConfig.get("item.backpack"));
        Assert.assertEquals(inventory.getCartCount(), 1,
                "Cart badge should show 1 after adding one item");
    }

    @Test(description = "TC-07: Verify that adding two different items updates the cart badge to 2")
    @Story("Add to cart")
    @Severity(SeverityLevel.CRITICAL)
    public void tc07_addTwoItemsBadgeShowsTwo() {
        InventoryPage inventory = loggedIn();
        inventory.addItemToCart(TestConfig.get("item.backpack"));
        inventory.addItemToCart(TestConfig.get("item.bikelight"));
        Assert.assertEquals(inventory.getCartCount(), 2,
                "Cart badge should show 2 after adding two items");
    }

    @Test(description = "TC-08: Verify that removing one of two items from the inventory page decreases the cart badge to 1")
    @Story("Remove from cart")
    @Severity(SeverityLevel.NORMAL)
    public void tc08_removeItemDecreasesCartCount() {
        InventoryPage inventory = loggedIn();
        inventory.addItemToCart(TestConfig.get("item.backpack"));
        inventory.addItemToCart(TestConfig.get("item.bikelight"));
        inventory.removeItemFromCart(TestConfig.get("item.backpack"));
        Assert.assertEquals(inventory.getCartCount(), 1,
                "Cart badge should show 1 after removing one of two items");
    }

    @Test(description = "TC-09: Verify that the cart page shows the item that was added on the inventory page")
    @Story("Cart page")
    @Severity(SeverityLevel.CRITICAL)
    public void tc09_cartPageShowsAddedItem() {
        InventoryPage inventory = loggedIn();
        inventory.addItemToCart(TestConfig.get("item.backpack"));
        CartPage cart = inventory.openCart();
        Assert.assertTrue(cart.isItemPresent(TestConfig.get("item.backpack")),
                "Cart page should list the added item");
    }

    @Test(description = "TC-10: Verify that removing the only item from the cart page leaves the cart empty")
    @Story("Remove from cart")
    @Severity(SeverityLevel.NORMAL)
    public void tc10_removeItemFromCartPageEmptiesCart() {
        InventoryPage inventory = loggedIn();
        inventory.addItemToCart(TestConfig.get("item.backpack"));
        CartPage cart = inventory.openCart();
        cart.removeItem(TestConfig.get("item.backpack"));
        Assert.assertEquals(cart.getItemCount(), 0,
                "Cart should be empty after removing the only item");
    }
}
