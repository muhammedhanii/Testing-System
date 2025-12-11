"""
Base Page - Parent class for all page objects
Contains common methods used across all pages
"""

from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
from utils.config import Config
from utils.helpers import take_screenshot, scroll_to_element


class BasePage:
    """Base class for all page objects"""
    
    def __init__(self, driver):
        """
        Initialize base page
        
        Args:
            driver: WebDriver instance
        """
        self.driver = driver
        self.wait = WebDriverWait(driver, Config.EXPLICIT_WAIT)
    
    def open(self, url):
        """
        Open URL
        
        Args:
            url (str): URL to open
        """
        self.driver.get(url)
    
    def find_element(self, locator):
        """
        Find element using explicit wait
        
        Args:
            locator: Tuple of (By.LOCATOR_TYPE, "locator_value")
            
        Returns:
            WebElement: Found element
        """
        try:
            element = self.wait.until(EC.visibility_of_element_located(locator))
            return element
        except TimeoutException:
            take_screenshot(self.driver, f"element_not_found_{locator[1]}")
            raise
    
    def find_elements(self, locator):
        """
        Find multiple elements
        
        Args:
            locator: Tuple of (By.LOCATOR_TYPE, "locator_value")
            
        Returns:
            list: List of WebElements
        """
        try:
            elements = self.wait.until(EC.presence_of_all_elements_located(locator))
            return elements
        except TimeoutException:
            take_screenshot(self.driver, f"elements_not_found_{locator[1]}")
            raise
    
    def click(self, locator):
        """
        Click on element
        
        Args:
            locator: Tuple of (By.LOCATOR_TYPE, "locator_value")
        """
        element = self.wait.until(EC.element_to_be_clickable(locator))
        scroll_to_element(self.driver, element)
        element.click()
    
    def enter_text(self, locator, text):
        """
        Enter text into input field
        
        Args:
            locator: Tuple of (By.LOCATOR_TYPE, "locator_value")
            text (str): Text to enter
        """
        element = self.find_element(locator)
        element.clear()
        element.send_keys(text)
    
    def get_text(self, locator):
        """
        Get text from element
        
        Args:
            locator: Tuple of (By.LOCATOR_TYPE, "locator_value")
            
        Returns:
            str: Element text
        """
        element = self.find_element(locator)
        return element.text
    
    def is_element_visible(self, locator, timeout=5):
        """
        Check if element is visible
        
        Args:
            locator: Tuple of (By.LOCATOR_TYPE, "locator_value")
            timeout (int): Wait timeout
            
        Returns:
            bool: True if visible, False otherwise
        """
        try:
            WebDriverWait(self.driver, timeout).until(
                EC.visibility_of_element_located(locator)
            )
            return True
        except TimeoutException:
            return False
    
    def is_element_present(self, locator):
        """
        Check if element is present in DOM
        
        Args:
            locator: Tuple of (By.LOCATOR_TYPE, "locator_value")
            
        Returns:
            bool: True if present, False otherwise
        """
        try:
            self.driver.find_element(*locator)
            return True
        except:
            return False
    
    def wait_for_url_contains(self, text, timeout=None):
        """
        Wait for URL to contain text
        
        Args:
            text (str): Text to check
            timeout (int): Wait timeout
        """
        timeout = timeout or Config.EXPLICIT_WAIT
        self.wait.until(EC.url_contains(text))
    
    def get_current_url(self):
        """
        Get current page URL
        
        Returns:
            str: Current URL
        """
        return self.driver.current_url
    
    def get_page_title(self):
        """
        Get page title
        
        Returns:
            str: Page title
        """
        return self.driver.title
    
    def take_screenshot(self, name):
        """
        Take screenshot
        
        Args:
            name (str): Screenshot name
        """
        return take_screenshot(self.driver, name)
    
    def scroll_to_top(self):
        """Scroll to top of page"""
        self.driver.execute_script("window.scrollTo(0, 0);")
    
    def scroll_to_bottom(self):
        """Scroll to bottom of page"""
        self.driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
    
    def refresh_page(self):
        """Refresh current page"""
        self.driver.refresh()
    
    def go_back(self):
        """Go back to previous page"""
        self.driver.back()
    
    def accept_alert(self):
        """Accept browser alert"""
        alert = self.wait.until(EC.alert_is_present())
        alert.accept()
    
    def dismiss_alert(self):
        """Dismiss browser alert"""
        alert = self.wait.until(EC.alert_is_present())
        alert.dismiss()
    
    def get_alert_text(self):
        """Get alert text"""
        alert = self.wait.until(EC.alert_is_present())
        return alert.text
