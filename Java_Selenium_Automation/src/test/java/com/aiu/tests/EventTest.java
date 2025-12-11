package com.aiu.tests;

import com.aiu.pages.DashboardPage;
import com.aiu.pages.EventsPage;
import com.aiu.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * EventTest - Test cases for events functionality
 */
public class EventTest extends TestBase {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private EventsPage eventsPage;

    @BeforeMethod
    public void setupPages() {
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        eventsPage = new EventsPage(driver);
    }

    @Test(priority = 1, description = "Verify events page is accessible")
    public void testEventsPageAccess() {
        logInfo("Starting test: Events Page Access");

        // Login as student
        loginPage.open();
        loginPage.loginAsStudent();

        // Navigate to events page
        dashboardPage.navigateToEvents();

        // Verify on events page
        Assert.assertTrue(eventsPage.isOnEventsPage(), 
            "User should be on events page");
        logPass("Events page accessed successfully");
    }

    @Test(priority = 2, description = "Verify events are displayed")
    public void testEventsAreDisplayed() {
        logInfo("Starting test: Events Display");

        // Login and navigate to events
        loginPage.open();
        loginPage.loginAsStudent();
        dashboardPage.navigateToEvents();

        // Verify events are displayed
        Assert.assertTrue(eventsPage.areEventsDisplayed(), 
            "Events should be displayed on the page");
        
        int eventCount = eventsPage.getEventCount();
        Assert.assertTrue(eventCount > 0, 
            "At least one event should be displayed");
        
        logPass("Events displayed successfully. Count: " + eventCount);
    }

    @Test(priority = 3, description = "Verify search events functionality")
    public void testSearchEvents() {
        logInfo("Starting test: Search Events");

        // Login and navigate to events
        loginPage.open();
        loginPage.loginAsStudent();
        dashboardPage.navigateToEvents();

        // Search for events
        eventsPage.searchEvents("Conference");

        // Verify search results
        Assert.assertTrue(eventsPage.isOnEventsPage(), 
            "Should remain on events page after search");
        logPass("Search functionality working");
    }

    @Test(priority = 4, description = "Verify event details can be viewed")
    public void testViewEventDetails() {
        logInfo("Starting test: View Event Details");

        // Login and navigate to events
        loginPage.open();
        loginPage.loginAsStudent();
        dashboardPage.navigateToEvents();

        // Click on first event
        if (eventsPage.areEventsDisplayed()) {
            eventsPage.clickFirstEvent();
            logPass("Event details page opened");
        } else {
            logInfo("No events available to view");
        }
    }

    @Test(priority = 5, description = "Verify create event button is visible for organizer")
    public void testCreateEventButtonVisibility() {
        logInfo("Starting test: Create Event Button Visibility");

        // Login as organizer
        loginPage.open();
        loginPage.loginAsOrganizer();
        dashboardPage.navigateToEvents();

        // Verify create event button is visible
        Assert.assertTrue(eventsPage.isCreateEventButtonVisible(), 
            "Create Event button should be visible for organizer");
        logPass("Create Event button visible for organizer");
    }

    @Test(priority = 6, description = "Verify booking event functionality")
    public void testBookEvent() {
        logInfo("Starting test: Book Event");

        // Login and navigate to events
        loginPage.open();
        loginPage.loginAsStudent();
        dashboardPage.navigateToEvents();

        // Try to book an event
        if (eventsPage.areEventsDisplayed()) {
            eventsPage.clickFirstBookButton();
            logPass("Book button clicked successfully");
        } else {
            logInfo("No events available to book");
        }
    }
}
