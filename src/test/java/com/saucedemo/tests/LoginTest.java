package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
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

@Feature("Login")
public class LoginTest extends BaseTest {

    // -------------------------------------------------------------------------
    // TC-01: Happy path — valid login redirects to inventory
    // -------------------------------------------------------------------------
    @Test
    @Story("Valid credentials")
    @Description("TC-01: Standard user with correct credentials should land on the inventory page")
    public void tc01_validLoginRedirectsToInventory() {
        LoginPage login = new LoginPage(driver);
        login.loginAs(TestConfig.get("user.standard"), TestConfig.get("password.valid"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/inventory.html"),
                "Valid login should redirect to inventory page");
    }

    // -------------------------------------------------------------------------
    // TC-05: Inventory content loads after login
    // -------------------------------------------------------------------------
    @Test
    @Story("Valid credentials")
    @Description("TC-05: After valid login the inventory page should display at least one product")
    public void tc05_inventoryPageShowsProductsAfterLogin() {
        LoginPage login = new LoginPage(driver);
        InventoryPage inventory = login.loginAs(
                TestConfig.get("user.standard"), TestConfig.get("password.valid"));
        Assert.assertTrue(inventory.getInventoryItemCount() > 0,
                "Inventory page should display products after login");
    }

    // -------------------------------------------------------------------------
    // TC-02 / TC-03 / TC-04 — data-driven: invalid credential scenarios
    // Rows come from testdata/login_validation_data.csv:
    //   username | password | expected_error | description
    // -------------------------------------------------------------------------
    @DataProvider(name = "loginValidationData")
    public Object[][] loginValidationData() {
        return CsvDataProvider.read("testdata/login_validation_data.csv");
    }

    @Test(dataProvider = "loginValidationData")
    @Story("Invalid credentials")
    @Description("Data-driven: each CSV row supplies a credential combination and the error text it should trigger")
    public void tcDD_loginValidationShowsCorrectError(
            String username, String password, String expectedError, String description) {
        LoginPage login = new LoginPage(driver);
        login.loginAs(username, password);
        Assert.assertTrue(login.hasError(),
                "[" + description + "] An error message should appear");
        Assert.assertTrue(login.getErrorMessage().contains(expectedError),
                "[" + description + "] Expected error containing '" + expectedError
                        + "' but got: " + login.getErrorMessage());
    }
}
