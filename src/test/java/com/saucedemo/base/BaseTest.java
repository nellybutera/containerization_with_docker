package com.saucedemo.base;

import com.saucedemo.utils.TestConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;
import java.util.logging.Logger;

public class BaseTest {

    protected WebDriver driver;
    private static final Logger log = Logger.getLogger(BaseTest.class.getName());

    @BeforeMethod
    public void setUp() {
        java.util.logging.Logger
                .getLogger("org.openqa.selenium.chromium.ChromiumDriver")
                .setLevel(java.util.logging.Level.SEVERE);

        boolean headless = Boolean.parseBoolean(System.getenv().getOrDefault("CI", "false"))
                || Boolean.parseBoolean(System.getProperty("headless", "false"));

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--log-level=3");
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().window().maximize();
        driver.get(TestConfig.get("base.url"));
        log.info("Browser opened — navigated to " + TestConfig.get("base.url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.warning("Test FAILED: " + result.getName());
            log.warning("Current URL at failure: " + driver.getCurrentUrl());
            log.warning("Page title at failure: " + driver.getTitle());
            String source = driver.getPageSource();
            log.warning("Page source length: " + source.length()
                    + " | first 500 chars: " + source.substring(0, Math.min(500, source.length())));
            captureScreenshot(result.getName());
        }
        if (driver != null) {
            driver.quit();
        }
    }

    @Attachment(value = "Screenshot — {testName}", type = "image/png")
    private byte[] captureScreenshot(String testName) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
