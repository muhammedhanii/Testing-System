"""
Login Page Object
"""

from selenium.webdriver.common.by import By
from pages.base_page import BasePage
from utils.config import Config


class LoginPage(BasePage):
    """Login page object model"""
    
    # Locators
    EMAIL_INPUT = (By.ID, "email")
    PASSWORD_INPUT = (By.ID, "password")
    LOGIN_BUTTON = (By.XPATH, "//button[contains(text(), 'Login') or contains(text(), 'Sign In')]")
    ERROR_MESSAGE = (By.CSS_SELECTOR, ".error-message, .alert-error, [role='alert']")
    FORGOT_PASSWORD_LINK = (By.LINK_TEXT, "Forgot Password")
    REGISTER_LINK = (By.LINK_TEXT, "Register")
    
    def __init__(self, driver):
        super().__init__(driver)
        self.url = Config.get_url('/login')
    
    def open(self):
        """Navigate to login page"""
        super().open(self.url)
    
    def login(self, email, password):
        """
        Perform login action
        
        Args:
            email (str): User email
            password (str): User password
        """
        self.enter_email(email)
        self.enter_password(password)
        self.click_login_button()
    
    def enter_email(self, email):
        """Enter email"""
        self.enter_text(self.EMAIL_INPUT, email)
    
    def enter_password(self, password):
        """Enter password"""
        self.enter_text(self.PASSWORD_INPUT, password)
    
    def click_login_button(self):
        """Click login button"""
        self.click(self.LOGIN_BUTTON)
    
    def get_error_message(self):
        """Get error message text"""
        return self.get_text(self.ERROR_MESSAGE)
    
    def is_error_displayed(self):
        """Check if error message is displayed"""
        return self.is_element_visible(self.ERROR_MESSAGE, timeout=3)
    
    def click_forgot_password(self):
        """Click forgot password link"""
        self.click(self.FORGOT_PASSWORD_LINK)
    
    def click_register(self):
        """Click register link"""
        self.click(self.REGISTER_LINK)
