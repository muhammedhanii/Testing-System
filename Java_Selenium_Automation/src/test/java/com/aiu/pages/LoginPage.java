package com.aiu.pages;

import com.aiu.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage - Page Object for Login page
 */
public class LoginPage extends BasePage {

    // Locators
    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement loginButton;

    @FindBy(xpath = "//div[contains(@class, 'error') or contains(@class, 'alert')]")
    private WebElement errorMessage;

    @FindBy(linkText = "Register")
    private WebElement registerLink;

    @FindBy(linkText = "Forgot Password?")
    private WebElement forgotPasswordLink;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to login page
     */
    public void open() {
        navigateTo(ConfigReader.getBaseUrl() + "/login");
        logger.info("Opened Login page");
    }

    /**
     * Enter email
     */
    public void enterEmail(String email) {
        type(emailInput, email);
        logger.info("Entered email: " + email);
    }

    /**
     * Enter password
     */
    public void enterPassword(String password) {
        type(passwordInput, password);
        logger.info("Entered password");
    }

    /**
     * Click login button
     */
    public void clickLoginButton() {
        click(loginButton);
        logger.info("Clicked login button");
    }

    /**
     * Perform complete login
     */
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        waitForPageLoad();
        logger.info("Performed login with email: " + email);
    }

    /**
     * Login with admin credentials
     */
    public void loginAsAdmin() {
        login(ConfigReader.getAdminEmail(), ConfigReader.getAdminPassword());
    }

    /**
     * Login with student credentials
     */
    public void loginAsStudent() {
        login(ConfigReader.getStudentEmail(), ConfigReader.getStudentPassword());
    }

    /**
     * Login with organizer credentials
     */
    public void loginAsOrganizer() {
        login(ConfigReader.getOrganizerEmail(), ConfigReader.getOrganizerPassword());
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    /**
     * Get error message text
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /**
     * Click register link
     */
    public void clickRegisterLink() {
        click(registerLink);
    }

    /**
     * Check if on login page
     */
    public boolean isOnLoginPage() {
        return getCurrentUrl().contains("/login");
    }
}
