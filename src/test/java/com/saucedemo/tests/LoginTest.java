package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.CsvDataProvider;
import com.saucedemo.utils.TestConfig;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Feature("Login")
public class LoginTest extends BaseTest {

    @Test(description = "TC-01: Verify that a standard user with correct credentials lands on the inventory page")
    @Story("Valid credentials")
    @Severity(SeverityLevel.BLOCKER)
    public void tc01_validLoginRedirectsToInventory() {
        LoginPage login = new LoginPage(driver);
        login.loginAs(TestConfig.get("user.standard"), TestConfig.get("password.valid"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/inventory.html"),
                "Valid login should redirect to inventory page");
    }

    @Test(description = "TC-05: Verify that the inventory page displays at least one product after valid login")
    @Story("Valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void tc05_inventoryPageShowsProductsAfterLogin() {
        LoginPage login = new LoginPage(driver);
        InventoryPage inventory = login.loginAs(
                TestConfig.get("user.standard"), TestConfig.get("password.valid"));
        Assert.assertTrue(inventory.getInventoryItemCount() > 0,
                "Inventory page should display products after login");
    }

    // Rows come from testdata/login_validation_data.csv:
    //   username | password | expected_error | description
    @DataProvider(name = "loginValidationData")
    public Object[][] loginValidationData() {
        return CsvDataProvider.read("testdata/login_validation_data.csv");
    }

    @Test(dataProvider = "loginValidationData",
          description = "TC-02/03/04: Verify that an invalid credential combination shows the correct validation error")
    @Story("Invalid credentials")
    @Severity(SeverityLevel.NORMAL)
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
