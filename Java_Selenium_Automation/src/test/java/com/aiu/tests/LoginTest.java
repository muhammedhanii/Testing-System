package com.aiu.tests;

import com.aiu.pages.DashboardPage;
import com.aiu.pages.LoginPage;
import com.aiu.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * LoginTest - Test cases for login functionality
 */
public class LoginTest extends TestBase {

    @Test(priority = 1, description = "Verify login with valid admin credentials")
    public void testValidAdminLogin() {
        logInfo("Starting test: Valid Admin Login");

        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboardPage = new DashboardPage(driver);

        // Navigate to login page
        loginPage.open();
        logInfo("Navigated to login page");

        // Perform login
        loginPage.loginAsAdmin();
        logInfo("Entered admin credentials and clicked login");

        // Verify redirection to dashboard
        Assert.assertTrue(dashboardPage.isOnDashboardPage(), 
            "User should be redirected to dashboard after successful login");
        logPass("Admin login successful - User redirected to dashboard");
    }

    @Test(priority = 2, description = "Verify login with valid student credentials")
    public void testValidStudentLogin() {
        logInfo("Starting test: Valid Student Login");

        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboardPage = new DashboardPage(driver);

        loginPage.open();
        loginPage.loginAsStudent();

        Assert.assertTrue(dashboardPage.isOnDashboardPage(), 
            "Student should be redirected to dashboard after successful login");
        logPass("Student login successful");
    }

    @Test(priority = 3, description = "Verify login with invalid password")
    public void testInvalidPassword() {
        logInfo("Starting test: Invalid Password Login");

        LoginPage loginPage = new LoginPage(driver);

        loginPage.open();
        loginPage.login(ConfigReader.getAdminEmail(), "wrongpassword123");

        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorDisplayed(), 
            "Error message should be displayed for invalid password");
        
        String errorMessage = loginPage.getErrorMessage().toLowerCase();
        Assert.assertTrue(errorMessage.contains("invalid") || errorMessage.contains("incorrect"), 
            "Error message should indicate invalid credentials");
        
        logPass("Invalid password handled correctly - Error message displayed");
    }

    @Test(priority = 4, description = "Verify login with non-existent email")
    public void testNonExistentEmail() {
        logInfo("Starting test: Non-existent Email Login");

        LoginPage loginPage = new LoginPage(driver);

        loginPage.open();
        loginPage.login("nonexistent@aiu.edu", "password123");

        Assert.assertTrue(loginPage.isErrorDisplayed(), 
            "Error message should be displayed for non-existent email");
        logPass("Non-existent email handled correctly");
    }

    @Test(priority = 5, description = "Verify login with empty credentials")
    public void testEmptyCredentials() {
        logInfo("Starting test: Empty Credentials Login");

        LoginPage loginPage = new LoginPage(driver);

        loginPage.open();
        loginPage.enterEmail("");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();

        // Should remain on login page or show validation error
        Assert.assertTrue(loginPage.isOnLoginPage(), 
            "User should remain on login page with empty credentials");
        logPass("Empty credentials validation working");
    }

    @Test(priority = 6, description = "Verify logout functionality")
    public void testLogout() {
        logInfo("Starting test: Logout Functionality");

        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboardPage = new DashboardPage(driver);

        // Login first
        loginPage.open();
        loginPage.loginAsStudent();

        Assert.assertTrue(dashboardPage.isOnDashboardPage(), 
            "User should be on dashboard");

        // Perform logout
        dashboardPage.logout();

        // Verify redirection to login page
        Assert.assertTrue(loginPage.isOnLoginPage(), 
            "User should be redirected to login page after logout");
        logPass("Logout successful");
    }
}
