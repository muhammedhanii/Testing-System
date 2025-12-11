package com.aiu.tests;

import com.aiu.utils.ConfigReader;
import com.aiu.utils.ExtentManager;
import com.aiu.utils.TestUtils;
import com.aiu.utils.WebDriverFactory;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * TestBase - Base class for all test classes
 * Handles setup, teardown, and reporting
 */
public class TestBase {
    protected WebDriver driver;
    protected Logger logger;
    protected static ExtentReports extent;
    protected static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @BeforeSuite
    public void setupSuite() {
        extent = ExtentManager.createInstance();
        logger = LogManager.getLogger(this.getClass());
        logger.info("Test Suite Started");
    }

    @BeforeMethod
    public void setup(java.lang.reflect.Method method) {
        logger = LogManager.getLogger(this.getClass());
        logger.info("Starting test: " + method.getName());

        // Create ExtentTest instance for this test
        ExtentTest test = extent.createTest(method.getName());
        extentTest.set(test);

        // Initialize WebDriver
        driver = WebDriverFactory.createDriver();
        logger.info("WebDriver initialized successfully");

        // Log test start
        extentTest.get().log(Status.INFO, "Test started: " + method.getName());
        extentTest.get().log(Status.INFO, "Browser: " + ConfigReader.getBrowser());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        // Handle test result
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("Test Failed: " + testName);
            extentTest.get().log(Status.FAIL, "Test Failed: " + testName);
            extentTest.get().log(Status.FAIL, result.getThrowable());

            // Take screenshot on failure
            if (ConfigReader.isScreenshotOnFailure()) {
                String screenshotPath = TestUtils.takeScreenshot(driver, testName);
                if (screenshotPath != null) {
                    extentTest.get().addScreenCaptureFromPath(screenshotPath);
                    logger.info("Screenshot captured: " + screenshotPath);
                }
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("Test Passed: " + testName);
            extentTest.get().log(Status.PASS, "Test Passed: " + testName);
        } else if (result.getStatus() == ITestResult.SKIP) {
            logger.info("Test Skipped: " + testName);
            extentTest.get().log(Status.SKIP, "Test Skipped: " + testName);
        }

        // Quit WebDriver
        if (driver != null) {
            driver.quit();
            logger.info("WebDriver closed successfully");
        }
    }

    @AfterSuite
    public void tearDownSuite() {
        if (extent != null) {
            extent.flush();
        }
        logger.info("Test Suite Completed");
    }

    /**
     * Get ExtentTest instance for current test
     */
    protected ExtentTest getTest() {
        return extentTest.get();
    }

    /**
     * Log info message to extent report
     */
    protected void logInfo(String message) {
        logger.info(message);
        if (extentTest.get() != null) {
            extentTest.get().log(Status.INFO, message);
        }
    }

    /**
     * Log pass message to extent report
     */
    protected void logPass(String message) {
        logger.info(message);
        if (extentTest.get() != null) {
            extentTest.get().log(Status.PASS, message);
        }
    }

    /**
     * Log fail message to extent report
     */
    protected void logFail(String message) {
        logger.error(message);
        if (extentTest.get() != null) {
            extentTest.get().log(Status.FAIL, message);
        }
    }
}
