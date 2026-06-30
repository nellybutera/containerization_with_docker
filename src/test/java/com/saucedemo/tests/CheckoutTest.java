package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.CheckoutInfoPage;
import com.saucedemo.pages.CheckoutReviewPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.CsvDataProvider;
import com.saucedemo.utils.TestConfig;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Feature("Checkout")
public class CheckoutTest extends BaseTest {

    private CartPage cartWithOneItem() {
        InventoryPage inventory = new LoginPage(driver).loginAs(
                TestConfig.get("user.standard"), TestConfig.get("password.valid"));
        inventory.addItemToCart(TestConfig.get("item.backpack"));
        return inventory.openCart();
    }

    // -------------------------------------------------------------------------
    // TC-11: Happy path — full checkout flow completes successfully
    // -------------------------------------------------------------------------
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

    // -------------------------------------------------------------------------
    // TC-15: Review page shows item total
    // -------------------------------------------------------------------------
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

    // -------------------------------------------------------------------------
    // TC-12 / TC-13 / TC-14 — data-driven: incomplete form validation
    // Rows come from testdata/checkout_validation_data.csv:
    //   firstname | lastname | zip | expected_error | description
    // -------------------------------------------------------------------------
    @DataProvider(name = "checkoutValidationData")
    public Object[][] checkoutValidationData() {
        return CsvDataProvider.read("testdata/checkout_validation_data.csv");
    }

    @Test(dataProvider = "checkoutValidationData")
    @Story("Form validation")
    @Description("Data-driven: each CSV row supplies partial form data and the validation error it should trigger")
    public void tcDD_checkoutValidationShowsCorrectError(
            String firstname, String lastname, String zip,
            String expectedError, String description) {
        CartPage cart = cartWithOneItem();
        CheckoutInfoPage info = cart.clickCheckout();
        info.fillFirstName(firstname)
            .fillLastName(lastname)
            .fillZip(zip)
            .clickContinue();
        Assert.assertTrue(info.getErrorMessage().contains(expectedError),
                "[" + description + "] Expected error '" + expectedError
                        + "' but got: " + info.getErrorMessage());
    }
}
