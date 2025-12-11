package com.aiu.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * CreateEventPage - Page Object for Create Event page
 */
public class CreateEventPage extends BasePage {

    // Locators
    @FindBy(id = "title")
    private WebElement titleInput;

    @FindBy(id = "description")
    private WebElement descriptionInput;

    @FindBy(id = "date")
    private WebElement dateInput;

    @FindBy(id = "time")
    private WebElement timeInput;

    @FindBy(id = "location")
    private WebElement locationInput;

    @FindBy(id = "capacity")
    private WebElement capacityInput;

    @FindBy(xpath = "//select[@name='category' or @id='category']")
    private WebElement categoryDropdown;

    @FindBy(xpath = "//select[@name='type' or @id='type']")
    private WebElement typeDropdown;

    @FindBy(xpath = "//button[@type='submit' or contains(text(), 'Create')]")
    private WebElement createButton;

    @FindBy(xpath = "//button[contains(text(), 'Cancel')]")
    private WebElement cancelButton;

    @FindBy(xpath = "//div[contains(@class, 'success')]")
    private WebElement successMessage;

    @FindBy(xpath = "//div[contains(@class, 'error')]")
    private WebElement errorMessage;

    public CreateEventPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Enter event title
     */
    public void enterTitle(String title) {
        type(titleInput, title);
        logger.info("Entered title: " + title);
    }

    /**
     * Enter event description
     */
    public void enterDescription(String description) {
        type(descriptionInput, description);
        logger.info("Entered description");
    }

    /**
     * Enter event date
     */
    public void enterDate(String date) {
        type(dateInput, date);
        logger.info("Entered date: " + date);
    }

    /**
     * Enter event time
     */
    public void enterTime(String time) {
        type(timeInput, time);
        logger.info("Entered time: " + time);
    }

    /**
     * Enter event location
     */
    public void enterLocation(String location) {
        type(locationInput, location);
        logger.info("Entered location: " + location);
    }

    /**
     * Enter event capacity
     */
    public void enterCapacity(String capacity) {
        type(capacityInput, capacity);
        logger.info("Entered capacity: " + capacity);
    }

    /**
     * Click create button
     */
    public void clickCreateButton() {
        click(createButton);
        waitForPageLoad();
        logger.info("Clicked Create button");
    }

    /**
     * Create event with all details
     */
    public void createEvent(String title, String description, String date, String time, 
                           String location, String capacity) {
        enterTitle(title);
        enterDescription(description);
        enterDate(date);
        enterTime(time);
        enterLocation(location);
        enterCapacity(capacity);
        clickCreateButton();
        logger.info("Created event: " + title);
    }

    /**
     * Check if success message is displayed
     */
    public boolean isSuccessMessageDisplayed() {
        return isDisplayed(successMessage);
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    /**
     * Get success message text
     */
    public String getSuccessMessage() {
        return getText(successMessage);
    }

    /**
     * Get error message text
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /**
     * Check if on create event page
     */
    public boolean isOnCreateEventPage() {
        return getCurrentUrl().contains("/create") || getCurrentUrl().contains("/new");
    }
}
