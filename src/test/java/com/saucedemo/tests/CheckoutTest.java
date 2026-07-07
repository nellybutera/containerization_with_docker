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
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Feature("Checkout")
public class CheckoutTest extends BaseTest {

    @Step("Given: logged in as standard user with one item in cart")
    private CartPage cartWithOneItem() {
        InventoryPage inventory = new LoginPage(driver).loginAs(
                TestConfig.get("user.standard"), TestConfig.get("password.valid"));
        inventory.addItemToCart(TestConfig.get("item.backpack"));
        return inventory.openCart();
    }

    @Test(description = "TC-11: Verify that completing the full checkout flow displays the order confirmation page")
    @Story("Happy path")
    @Severity(SeverityLevel.BLOCKER)
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

    @Test(description = "TC-15: Verify that the order review page displays a non-empty item total")
    @Story("Happy path")
    @Severity(SeverityLevel.NORMAL)
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

    // Rows come from testdata/checkout_validation_data.csv:
    //   firstname | lastname | zip | expected_error | description
    @DataProvider(name = "checkoutValidationData")
    public Object[][] checkoutValidationData() {
        return CsvDataProvider.read("testdata/checkout_validation_data.csv");
    }

    @Test(dataProvider = "checkoutValidationData",
          description = "TC-12/13/14: Verify that an incomplete checkout form shows the correct validation error")
    @Story("Form validation")
    @Severity(SeverityLevel.NORMAL)
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
