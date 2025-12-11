package com.aiu.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * BookingsPage - Page Object for My Bookings page
 */
public class BookingsPage extends BasePage {

    // Locators
    @FindBy(xpath = "//h1[contains(text(), 'My Bookings') or contains(text(), 'Bookings')]")
    private WebElement bookingsHeading;

    @FindBy(xpath = "//div[contains(@class, 'booking-card') or contains(@class, 'booking-item')]")
    private List<WebElement> bookingCards;

    @FindBy(xpath = "//button[contains(text(), 'Cancel Booking')]")
    private List<WebElement> cancelButtons;

    @FindBy(xpath = "//button[contains(text(), 'View Ticket')] | //a[contains(text(), 'View Ticket')]")
    private List<WebElement> viewTicketButtons;

    @FindBy(xpath = "//div[contains(@class, 'qr-code')]")
    private WebElement qrCode;

    @FindBy(xpath = "//span[contains(@class, 'status')]")
    private List<WebElement> bookingStatuses;

    @FindBy(xpath = "//div[contains(text(), 'No bookings found')]")
    private WebElement noBookingsMessage;

    public BookingsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Check if on bookings page
     */
    public boolean isOnBookingsPage() {
        return getCurrentUrl().contains("/bookings") || isDisplayed(bookingsHeading);
    }

    /**
     * Get bookings heading text
     */
    public String getBookingsHeading() {
        return getText(bookingsHeading);
    }

    /**
     * Get number of bookings
     */
    public int getBookingCount() {
        return bookingCards.size();
    }

    /**
     * Check if bookings are displayed
     */
    public boolean areBookingsDisplayed() {
        return !bookingCards.isEmpty();
    }

    /**
     * Click cancel button for first booking
     */
    public void cancelFirstBooking() {
        if (!cancelButtons.isEmpty()) {
            click(cancelButtons.get(0));
            waitForPageLoad();
            logger.info("Clicked cancel button for first booking");
        }
    }

    /**
     * Click view ticket button for first booking
     */
    public void viewFirstTicket() {
        if (!viewTicketButtons.isEmpty()) {
            click(viewTicketButtons.get(0));
            waitForPageLoad();
            logger.info("Clicked view ticket button");
        }
    }

    /**
     * Check if QR code is displayed
     */
    public boolean isQrCodeDisplayed() {
        return isDisplayed(qrCode);
    }

    /**
     * Check if no bookings message is displayed
     */
    public boolean isNoBookingsMessageDisplayed() {
        return isDisplayed(noBookingsMessage);
    }

    /**
     * Get status of first booking
     */
    public String getFirstBookingStatus() {
        if (!bookingStatuses.isEmpty()) {
            return getText(bookingStatuses.get(0));
        }
        return "";
    }
}
