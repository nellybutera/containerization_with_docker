package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.CheckoutInfoPage;
import com.saucedemo.pages.CheckoutReviewPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

    private static final String ITEM = "Sauce Labs Backpack";

    private CartPage cartWithOneItem() {
        InventoryPage inventory = new LoginPage(driver).loginAs("standard_user", "secret_sauce");
        inventory.addItemToCart(ITEM);
        return inventory.openCart();
    }

    @Test
    public void tc11_completeCheckoutFlowShowsConfirmation() {
        CartPage cart = cartWithOneItem();
        CheckoutInfoPage info = cart.clickCheckout();
        CheckoutReviewPage review = info.submitValidForm("Nelly", "Butera", "250100");
        CheckoutCompletePage complete = review.clickFinish();
        Assert.assertTrue(complete.isOrderComplete(),
                "Checkout complete page should show 'Thank you' after full flow");
    }

    @Test
    public void tc12_checkoutWithNoInfoShowsFirstNameError() {
        CartPage cart = cartWithOneItem();
        CheckoutInfoPage info = cart.clickCheckout();
        info.clickContinue();
        Assert.assertTrue(info.getErrorMessage().contains("First Name is required"),
                "Should require first name when no info entered");
    }

    @Test
    public void tc13_checkoutWithNoLastNameShowsError() {
        CartPage cart = cartWithOneItem();
        CheckoutInfoPage info = cart.clickCheckout();
        info.fillFirstName("Nelly").clickContinue();
        Assert.assertTrue(info.getErrorMessage().contains("Last Name is required"),
                "Should require last name when only first name entered");
    }

    @Test
    public void tc14_checkoutWithNoZipShowsError() {
        CartPage cart = cartWithOneItem();
        CheckoutInfoPage info = cart.clickCheckout();
        info.fillFirstName("Nelly").fillLastName("Butera").clickContinue();
        Assert.assertTrue(info.getErrorMessage().contains("Postal Code is required"),
                "Should require postal code when name is filled but zip is missing");
    }

    @Test
    public void tc15_reviewPageShowsItemTotal() {
        CartPage cart = cartWithOneItem();
        CheckoutInfoPage info = cart.clickCheckout();
        CheckoutReviewPage review = info.submitValidForm("Nelly", "Butera", "250100");
        Assert.assertFalse(review.getItemTotal().isEmpty(),
                "Review page should display the item total");
    }
}
