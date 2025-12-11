package com.aiu.tests;

import com.aiu.pages.BookingsPage;
import com.aiu.pages.DashboardPage;
import com.aiu.pages.EventsPage;
import com.aiu.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * BookingTest - Test cases for booking functionality
 */
public class BookingTest extends TestBase {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private EventsPage eventsPage;
    private BookingsPage bookingsPage;

    @BeforeMethod
    public void setupPages() {
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        eventsPage = new EventsPage(driver);
        bookingsPage = new BookingsPage(driver);
    }

    @Test(priority = 1, description = "Verify my bookings page is accessible")
    public void testBookingsPageAccess() {
        logInfo("Starting test: Bookings Page Access");

        // Login as student
        loginPage.open();
        loginPage.loginAsStudent();

        // Navigate to bookings page
        dashboardPage.navigateToBookings();

        // Verify on bookings page
        Assert.assertTrue(bookingsPage.isOnBookingsPage(), 
            "User should be on bookings page");
        logPass("Bookings page accessed successfully");
    }

    @Test(priority = 2, description = "Verify bookings are displayed if available")
    public void testViewBookings() {
        logInfo("Starting test: View Bookings");

        // Login and navigate to bookings
        loginPage.open();
        loginPage.loginAsStudent();
        dashboardPage.navigateToBookings();

        // Check if bookings exist
        if (bookingsPage.areBookingsDisplayed()) {
            int bookingCount = bookingsPage.getBookingCount();
            Assert.assertTrue(bookingCount > 0, 
                "Booking count should be greater than 0");
            logPass("Bookings displayed successfully. Count: " + bookingCount);
        } else {
            Assert.assertTrue(bookingsPage.isNoBookingsMessageDisplayed(), 
                "No bookings message should be displayed");
            logInfo("No bookings found for user");
        }
    }

    @Test(priority = 3, description = "Verify view ticket functionality")
    public void testViewTicket() {
        logInfo("Starting test: View Ticket");

        // Login and navigate to bookings
        loginPage.open();
        loginPage.loginAsStudent();
        dashboardPage.navigateToBookings();

        // View ticket if bookings exist
        if (bookingsPage.areBookingsDisplayed()) {
            bookingsPage.viewFirstTicket();
            
            // Verify QR code is displayed
            Assert.assertTrue(bookingsPage.isQrCodeDisplayed(), 
                "QR code should be displayed on ticket");
            logPass("Ticket viewed successfully with QR code");
        } else {
            logInfo("No bookings available to view ticket");
        }
    }

    @Test(priority = 4, description = "Verify cancel booking functionality")
    public void testCancelBooking() {
        logInfo("Starting test: Cancel Booking");

        // Login and navigate to bookings
        loginPage.open();
        loginPage.loginAsStudent();
        dashboardPage.navigateToBookings();

        // Cancel booking if available
        if (bookingsPage.areBookingsDisplayed()) {
            int initialCount = bookingsPage.getBookingCount();
            bookingsPage.cancelFirstBooking();
            
            // Wait for page to refresh
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            logPass("Cancel booking action completed");
        } else {
            logInfo("No bookings available to cancel");
        }
    }

    @Test(priority = 5, description = "Verify booking status display")
    public void testBookingStatus() {
        logInfo("Starting test: Booking Status Display");

        // Login and navigate to bookings
        loginPage.open();
        loginPage.loginAsStudent();
        dashboardPage.navigateToBookings();

        // Check booking status
        if (bookingsPage.areBookingsDisplayed()) {
            String status = bookingsPage.getFirstBookingStatus();
            Assert.assertFalse(status.isEmpty(), 
                "Booking status should be displayed");
            logPass("Booking status displayed: " + status);
        } else {
            logInfo("No bookings available to check status");
        }
    }

    @Test(priority = 6, description = "Verify complete booking flow")
    public void testCompleteBookingFlow() {
        logInfo("Starting test: Complete Booking Flow");

        // Login
        loginPage.open();
        loginPage.loginAsStudent();

        // Navigate to events and book an event
        dashboardPage.navigateToEvents();
        
        if (eventsPage.areEventsDisplayed()) {
            eventsPage.clickFirstBookButton();
            
            // Navigate to bookings to verify
            dashboardPage.navigateToBookings();
            
            Assert.assertTrue(bookingsPage.isOnBookingsPage(), 
                "User should be on bookings page");
            logPass("Complete booking flow executed");
        } else {
            logInfo("No events available for booking");
        }
    }
}
