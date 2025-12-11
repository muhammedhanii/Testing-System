package com.aiu.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

/**
 * TestUtils - Common utility methods for testing
 */
public class TestUtils {

    /**
     * Takes a screenshot and saves it to the specified path
     */
    public static String takeScreenshot(WebDriver driver, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String screenshotPath = ConfigReader.getScreenshotPath() + fileName;

        try {
            // Create directory if it doesn't exist
            File directory = new File(ConfigReader.getScreenshotPath());
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Take screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotPath);
            FileUtils.copyFile(source, destination);

            return screenshotPath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Wait for element to be visible
     */
    public static WebElement waitForElementToBeVisible(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for element to be clickable
     */
    public static WebElement waitForElementToBeClickable(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Generate random email
     */
    public static String generateRandomEmail() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "test" + timestamp + "@aiu.edu";
    }

    /**
     * Generate random name
     */
    public static String generateRandomName() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "TestUser" + timestamp;
    }

    /**
     * Wait for page to load using document ready state
     */
    public static void waitForPageLoad(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        wait.until(webDriver -> 
            ((org.openqa.selenium.JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete")
        );
    }
}
