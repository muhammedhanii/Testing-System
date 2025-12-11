package com.aiu.pages;

import com.aiu.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * EventsPage - Page Object for Events listing page
 */
public class EventsPage extends BasePage {

    // Locators
    @FindBy(xpath = "//h1[contains(text(), 'Events') or contains(text(), 'Trips')]")
    private WebElement eventsHeading;

    @FindBy(xpath = "//button[contains(text(), 'Create Event')] | //a[contains(text(), 'Create Event')]")
    private WebElement createEventButton;

    @FindBy(xpath = "//div[contains(@class, 'event-card') or contains(@class, 'event-item')]")
    private List<WebElement> eventCards;

    @FindBy(xpath = "//input[@placeholder='Search events' or @name='search']")
    private WebElement searchInput;

    @FindBy(xpath = "//button[contains(text(), 'Search')]")
    private WebElement searchButton;

    @FindBy(xpath = "//select[@name='category' or contains(@class, 'filter')]")
    private WebElement categoryFilter;

    @FindBy(xpath = "//button[contains(text(), 'Book') or contains(text(), 'Register')]")
    private List<WebElement> bookButtons;

    public EventsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to events page
     */
    public void open() {
        navigateTo(ConfigReader.getBaseUrl() + "/events");
        logger.info("Opened Events page");
    }

    /**
     * Check if on events page
     */
    public boolean isOnEventsPage() {
        return getCurrentUrl().contains("/events") || isDisplayed(eventsHeading);
    }

    /**
     * Get events heading text
     */
    public String getEventsHeading() {
        return getText(eventsHeading);
    }

    /**
     * Click create event button
     */
    public void clickCreateEvent() {
        click(createEventButton);
        logger.info("Clicked Create Event button");
    }

    /**
     * Get number of events displayed
     */
    public int getEventCount() {
        return eventCards.size();
    }

    /**
     * Search for events
     */
    public void searchEvents(String searchTerm) {
        type(searchInput, searchTerm);
        click(searchButton);
        waitForPageLoad();
        logger.info("Searched for: " + searchTerm);
    }

    /**
     * Click first event to view details
     */
    public void clickFirstEvent() {
        if (!eventCards.isEmpty()) {
            click(eventCards.get(0));
            logger.info("Clicked first event");
        }
    }

    /**
     * Click book button for first event
     */
    public void clickFirstBookButton() {
        if (!bookButtons.isEmpty()) {
            click(bookButtons.get(0));
            logger.info("Clicked first book button");
        }
    }

    /**
     * Check if events are displayed
     */
    public boolean areEventsDisplayed() {
        return !eventCards.isEmpty();
    }

    /**
     * Check if create event button is visible (for organizer/admin)
     */
    public boolean isCreateEventButtonVisible() {
        return isDisplayed(createEventButton);
    }
}
