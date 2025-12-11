"""
Dashboard Page Object
"""

from selenium.webdriver.common.by import By
from pages.base_page import BasePage
from utils.config import Config


class DashboardPage(BasePage):
    """Dashboard page object model"""
    
    # Locators
    DASHBOARD_HEADER = (By.XPATH, "//h1[contains(text(), 'Dashboard') or contains(text(), 'Welcome')]")
    USER_MENU = (By.CSS_SELECTOR, ".user-menu, [data-testid='user-menu']")
    LOGOUT_BUTTON = (By.XPATH, "//button[contains(text(), 'Logout') or contains(text(), 'Sign Out')]")
    EVENTS_LINK = (By.LINK_TEXT, "Events")
    BOOKINGS_LINK = (By.LINK_TEXT, "My Bookings")
    
    def __init__(self, driver):
        super().__init__(driver)
        self.url = Config.get_url('/dashboard')
    
    def is_on_dashboard_page(self):
        """Check if on dashboard page"""
        return self.is_element_visible(self.DASHBOARD_HEADER, timeout=10) or "dashboard" in self.get_current_url().lower()
    
    def logout(self):
        """Logout user"""
        self.click(self.USER_MENU)
        self.click(self.LOGOUT_BUTTON)
    
    def navigate_to_events(self):
        """Navigate to events page"""
        self.click(self.EVENTS_LINK)
    
    def navigate_to_bookings(self):
        """Navigate to bookings page"""
        self.click(self.BOOKINGS_LINK)
