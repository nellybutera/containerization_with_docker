package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void tc01_validLoginRedirectsToInventory() {
        LoginPage login = new LoginPage(driver);
        login.loginAs("standard_user", "secret_sauce");
        Assert.assertTrue(driver.getCurrentUrl().contains("/inventory.html"),
                "Valid login should redirect to inventory page");
    }

    @Test
    public void tc02_invalidPasswordShowsError() {
        LoginPage login = new LoginPage(driver);
        login.loginAs("standard_user", "wrong_password");
        Assert.assertTrue(login.hasError(),
                "Error message should appear for wrong password");
        Assert.assertTrue(login.getErrorMessage().contains("Username and password do not match"),
                "Error text mismatch: " + login.getErrorMessage());
    }

    @Test
    public void tc03_lockedOutUserShowsError() {
        LoginPage login = new LoginPage(driver);
        login.loginAs("locked_out_user", "secret_sauce");
        Assert.assertTrue(login.hasError(),
                "Error message should appear for locked-out user");
        Assert.assertTrue(login.getErrorMessage().contains("locked out"),
                "Error should mention locked out: " + login.getErrorMessage());
    }

    @Test
    public void tc04_emptyCredentialsShowsError() {
        LoginPage login = new LoginPage(driver);
        login.loginAs("", "");
        Assert.assertTrue(login.hasError(),
                "Error message should appear for empty credentials");
        Assert.assertTrue(login.getErrorMessage().contains("Username is required"),
                "Error should ask for username: " + login.getErrorMessage());
    }

    @Test
    public void tc05_inventoryPageShowsProductsAfterLogin() {
        LoginPage login = new LoginPage(driver);
        InventoryPage inventory = login.loginAs("standard_user", "secret_sauce");
        Assert.assertTrue(inventory.getInventoryItemCount() > 0,
                "Inventory page should display products after login");
    }
}
