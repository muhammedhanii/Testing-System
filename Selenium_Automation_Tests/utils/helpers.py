"""
Helper utility functions for tests
"""

import os
import time
from datetime import datetime
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
from utils.config import Config


def take_screenshot(driver, name="screenshot"):
    """
    Take a screenshot and save it
    
    Args:
        driver: WebDriver instance
        name (str): Screenshot filename
    """
    if not os.path.exists(Config.SCREENSHOT_DIR):
        os.makedirs(Config.SCREENSHOT_DIR)
    
    timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
    filename = f"{name}_{timestamp}.png"
    filepath = os.path.join(Config.SCREENSHOT_DIR, filename)
    
    driver.save_screenshot(filepath)
    print(f"Screenshot saved: {filepath}")
    return filepath


def wait_for_element(driver, locator, timeout=None):
    """
    Wait for element to be visible
    
    Args:
        driver: WebDriver instance
        locator: Tuple of (By.LOCATOR_TYPE, "locator_value")
        timeout (int): Wait timeout in seconds
        
    Returns:
        WebElement: The found element
    """
    timeout = timeout or Config.EXPLICIT_WAIT
    try:
        element = WebDriverWait(driver, timeout).until(
            EC.visibility_of_element_located(locator)
        )
        return element
    except TimeoutException:
        take_screenshot(driver, f"timeout_error_{locator[1]}")
        raise


def wait_for_element_clickable(driver, locator, timeout=None):
    """
    Wait for element to be clickable
    
    Args:
        driver: WebDriver instance
        locator: Tuple of (By.LOCATOR_TYPE, "locator_value")
        timeout (int): Wait timeout in seconds
        
    Returns:
        WebElement: The found element
    """
    timeout = timeout or Config.EXPLICIT_WAIT
    try:
        element = WebDriverWait(driver, timeout).until(
            EC.element_to_be_clickable(locator)
        )
        return element
    except TimeoutException:
        take_screenshot(driver, f"timeout_error_{locator[1]}")
        raise


def wait_for_url_contains(driver, text, timeout=None):
    """
    Wait for URL to contain specific text
    
    Args:
        driver: WebDriver instance
        text (str): Text to check in URL
        timeout (int): Wait timeout in seconds
    """
    timeout = timeout or Config.EXPLICIT_WAIT
    try:
        WebDriverWait(driver, timeout).until(
            EC.url_contains(text)
        )
    except TimeoutException:
        take_screenshot(driver, f"url_timeout_{text}")
        raise


def wait_for_text_in_element(driver, locator, text, timeout=None):
    """
    Wait for specific text to appear in element
    
    Args:
        driver: WebDriver instance
        locator: Tuple of (By.LOCATOR_TYPE, "locator_value")
        text (str): Expected text
        timeout (int): Wait timeout in seconds
    """
    timeout = timeout or Config.EXPLICIT_WAIT
    try:
        WebDriverWait(driver, timeout).until(
            EC.text_to_be_present_in_element(locator, text)
        )
    except TimeoutException:
        take_screenshot(driver, f"text_timeout_{text}")
        raise


def scroll_to_element(driver, element):
    """
    Scroll to make element visible
    
    Args:
        driver: WebDriver instance
        element: WebElement to scroll to
    """
    driver.execute_script("arguments[0].scrollIntoView(true);", element)
    time.sleep(0.5)  # Small wait for scroll animation


def generate_unique_email(prefix="test"):
    """
    Generate a unique email address for testing
    
    Args:
        prefix (str): Email prefix
        
    Returns:
        str: Unique email address
    """
    timestamp = datetime.now().strftime('%Y%m%d%H%M%S')
    return f"{prefix}{timestamp}@aiu.edu"


def generate_test_data(data_type="user"):
    """
    Generate test data
    
    Args:
        data_type (str): Type of data to generate ('user', 'event', 'trip')
        
    Returns:
        dict: Generated test data
    """
    from faker import Faker
    fake = Faker()
    
    if data_type == "user":
        return {
            'email': generate_unique_email(),
            'password': 'Test123!@#',
            'firstName': fake.first_name(),
            'lastName': fake.last_name()
        }
    elif data_type == "event":
        return {
            'title': f"Test Event {fake.word().title()}",
            'description': fake.text(max_nb_chars=200),
            'location': f"{fake.building_number()} {fake.street_name()}",
            'capacity': fake.random_int(min=10, max=100),
            'price': fake.random_int(min=0, max=500)
        }
    elif data_type == "trip":
        return {
            'title': f"Test Trip to {fake.city()}",
            'description': fake.text(max_nb_chars=200),
            'destination': fake.city(),
            'capacity': fake.random_int(min=10, max=50),
            'price': fake.random_int(min=100, max=1000)
        }
    else:
        raise ValueError(f"Unknown data type: {data_type}")
