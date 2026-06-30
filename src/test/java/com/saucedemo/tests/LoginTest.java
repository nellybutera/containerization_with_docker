package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.TestConfig;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("Login")
public class LoginTest extends BaseTest {

    @Test
    @Story("Valid credentials")
    @Description("TC-01: Standard user logs in with correct credentials — should redirect to the inventory page")
    public void tc01_validLoginRedirectsToInventory() {
        LoginPage login = new LoginPage(driver);
        login.loginAs(TestConfig.get("user.standard"), TestConfig.get("password.valid"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/inventory.html"),
                "Valid login should redirect to inventory page");
    }

    @Test
    @Story("Invalid credentials")
    @Description("TC-02: Wrong password — error message should appear explaining the mismatch")
    public void tc02_invalidPasswordShowsError() {
        LoginPage login = new LoginPage(driver);
        login.loginAs(TestConfig.get("user.standard"), TestConfig.get("password.invalid"));
        Assert.assertTrue(login.hasError(),
                "Error message should appear for wrong password");
        Assert.assertTrue(login.getErrorMessage().contains("Username and password do not match"),
                "Error text mismatch: " + login.getErrorMessage());
    }

    @Test
    @Story("Locked-out user")
    @Description("TC-03: Locked-out user — error message should state the account is locked")
    public void tc03_lockedOutUserShowsError() {
        LoginPage login = new LoginPage(driver);
        login.loginAs(TestConfig.get("user.locked"), TestConfig.get("password.valid"));
        Assert.assertTrue(login.hasError(),
                "Error message should appear for locked-out user");
        Assert.assertTrue(login.getErrorMessage().contains("locked out"),
                "Error should mention locked out: " + login.getErrorMessage());
    }

    @Test
    @Story("Empty credentials")
    @Description("TC-04: Submitting empty credentials — form should ask for username")
    public void tc04_emptyCredentialsShowsError() {
        LoginPage login = new LoginPage(driver);
        login.loginAs("", "");
        Assert.assertTrue(login.hasError(),
                "Error message should appear for empty credentials");
        Assert.assertTrue(login.getErrorMessage().contains("Username is required"),
                "Error should ask for username: " + login.getErrorMessage());
    }

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
}
