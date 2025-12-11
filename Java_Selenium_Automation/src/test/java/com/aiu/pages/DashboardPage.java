package com.aiu.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * DashboardPage - Page Object for Dashboard page
 */
public class DashboardPage extends BasePage {

    // Locators
    @FindBy(xpath = "//h1[contains(text(), 'Dashboard') or contains(text(), 'Welcome')]")
    private WebElement dashboardHeading;

    @FindBy(xpath = "//nav//a[contains(text(), 'Events')]")
    private WebElement eventsNavLink;

    @FindBy(xpath = "//nav//a[contains(text(), 'My Bookings')]")
    private WebElement bookingsNavLink;

    @FindBy(xpath = "//nav//a[contains(text(), 'Profile')]")
    private WebElement profileNavLink;

    @FindBy(xpath = "//button[contains(text(), 'Logout')] | //a[contains(text(), 'Logout')]")
    private WebElement logoutButton;

    @FindBy(xpath = "//div[contains(@class, 'notification')]")
    private WebElement notificationBadge;

    @FindBy(xpath = "//div[@class='stats' or contains(@class, 'dashboard-stats')]")
    private WebElement statsSection;

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Check if on dashboard page
     */
    public boolean isOnDashboardPage() {
        return getCurrentUrl().contains("/dashboard") || isDisplayed(dashboardHeading);
    }

    /**
     * Get dashboard heading text
     */
    public String getDashboardHeading() {
        return getText(dashboardHeading);
    }

    /**
     * Navigate to Events page
     */
    public void navigateToEvents() {
        click(eventsNavLink);
        logger.info("Navigated to Events page");
    }

    /**
     * Navigate to My Bookings page
     */
    public void navigateToBookings() {
        click(bookingsNavLink);
        logger.info("Navigated to My Bookings page");
    }

    /**
     * Navigate to Profile page
     */
    public void navigateToProfile() {
        click(profileNavLink);
        logger.info("Navigated to Profile page");
    }

    /**
     * Perform logout
     */
    public void logout() {
        click(logoutButton);
        waitForPageLoad();
        logger.info("Logged out successfully");
    }

    /**
     * Check if notification badge is displayed
     */
    public boolean isNotificationDisplayed() {
        return isDisplayed(notificationBadge);
    }

    /**
     * Check if stats section is displayed
     */
    public boolean isStatsDisplayed() {
        return isDisplayed(statsSection);
    }
}
