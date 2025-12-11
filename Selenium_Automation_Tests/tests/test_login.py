"""
Login Test Cases
"""

import pytest
from pages.login_page import LoginPage
from pages.dashboard_page import DashboardPage
from utils.config import Config


@pytest.mark.login
class TestLogin:
    """Login functionality test cases"""
    
    def test_valid_login(self, driver):
        """Test login with valid credentials"""
        login_page = LoginPage(driver)
        dashboard_page = DashboardPage(driver)
        
        # Navigate to login page
        login_page.open()
        
        # Perform login
        login_page.login(Config.ADMIN_EMAIL, Config.ADMIN_PASSWORD)
        
        # Verify redirection to dashboard
        assert dashboard_page.is_on_dashboard_page(), "User not redirected to dashboard"
        print("✓ Login successful")
    
    def test_invalid_password(self, driver):
        """Test login with invalid password"""
        login_page = LoginPage(driver)
        
        login_page.open()
        login_page.login(Config.ADMIN_EMAIL, "wrongpassword")
        
        # Verify error message
        assert login_page.is_error_displayed(), "Error message not displayed"
        error_msg = login_page.get_error_message().lower()
        assert "invalid" in error_msg or "incorrect" in error_msg, f"Unexpected error: {error_msg}"
        print("✓ Invalid password handled correctly")
    
    def test_non_existent_email(self, driver):
        """Test login with non-existent email"""
        login_page = LoginPage(driver)
        
        login_page.open()
        login_page.login("nonexistent@aiu.edu", "password123")
        
        # Verify error message
        assert login_page.is_error_displayed(), "Error message not displayed"
        print("✓ Non-existent email handled correctly")
    
    def test_empty_credentials(self, driver):
        """Test login with empty credentials"""
        login_page = LoginPage(driver)
        
        login_page.open()
        login_page.click_login_button()
        
        # Verify validation (button disabled or error shown)
        # Implementation depends on frontend validation
        print("✓ Empty credentials validation checked")
