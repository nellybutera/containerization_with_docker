package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.CheckoutInfoPage;
import com.saucedemo.pages.CheckoutReviewPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.TestConfig;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("Checkout")
public class CheckoutTest extends BaseTest {

    private CartPage cartWithOneItem() {
        InventoryPage inventory = new LoginPage(driver).loginAs(
                TestConfig.get("user.standard"), TestConfig.get("password.valid"));
        inventory.addItemToCart(TestConfig.get("item.backpack"));
        return inventory.openCart();
    }

    @Test
    @Story("Happy path")
    @Description("TC-11: Completing the full checkout flow should display the order confirmation page")
    public void tc11_completeCheckoutFlowShowsConfirmation() {
        CartPage cart = cartWithOneItem();
        CheckoutInfoPage info = cart.clickCheckout();
        CheckoutReviewPage review = info.submitValidForm(
                TestConfig.get("checkout.firstname"),
                TestConfig.get("checkout.lastname"),
                TestConfig.get("checkout.zip"));
        CheckoutCompletePage complete = review.clickFinish();
        Assert.assertTrue(complete.isOrderComplete(),
                "Checkout complete page should show 'Thank you' after full flow");
    }

    @Test
    @Story("Form validation")
    @Description("TC-12: Clicking Continue with no info entered — form should require first name")
    public void tc12_checkoutWithNoInfoShowsFirstNameError() {
        CartPage cart = cartWithOneItem();
        CheckoutInfoPage info = cart.clickCheckout();
        info.clickContinue();
        Assert.assertTrue(info.getErrorMessage().contains("First Name is required"),
                "Should require first name when no info entered");
    }

    @Test
    @Story("Form validation")
    @Description("TC-13: Entering only first name and clicking Continue — form should require last name")
    public void tc13_checkoutWithNoLastNameShowsError() {
        CartPage cart = cartWithOneItem();
        CheckoutInfoPage info = cart.clickCheckout();
        info.fillFirstName(TestConfig.get("checkout.firstname")).clickContinue();
        Assert.assertTrue(info.getErrorMessage().contains("Last Name is required"),
                "Should require last name when only first name entered");
    }

    @Test
    @Story("Form validation")
    @Description("TC-14: Entering name but no postal code — form should require postal code")
    public void tc14_checkoutWithNoZipShowsError() {
        CartPage cart = cartWithOneItem();
        CheckoutInfoPage info = cart.clickCheckout();
        info.fillFirstName(TestConfig.get("checkout.firstname"))
            .fillLastName(TestConfig.get("checkout.lastname"))
            .clickContinue();
        Assert.assertTrue(info.getErrorMessage().contains("Postal Code is required"),
                "Should require postal code when name is filled but zip is missing");
    }

    @Test
    @Story("Happy path")
    @Description("TC-15: The order review page should display a non-empty item total")
    public void tc15_reviewPageShowsItemTotal() {
        CartPage cart = cartWithOneItem();
        CheckoutInfoPage info = cart.clickCheckout();
        CheckoutReviewPage review = info.submitValidForm(
                TestConfig.get("checkout.firstname"),
                TestConfig.get("checkout.lastname"),
                TestConfig.get("checkout.zip"));
        Assert.assertFalse(review.getItemTotal().isEmpty(),
                "Review page should display the item total");
    }
}
