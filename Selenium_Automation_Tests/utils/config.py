"""
Configuration settings for Selenium tests
"""

import os
from dotenv import load_dotenv

load_dotenv()

class Config:
    """Configuration class for test settings"""
    
    # Application URLs
    BASE_URL = os.getenv('BASE_URL', 'http://localhost:3001')
    API_BASE_URL = os.getenv('API_BASE_URL', 'http://localhost:8081')
    
    # Browser settings
    BROWSER = os.getenv('BROWSER', 'chrome')  # chrome, firefox, edge
    HEADLESS = os.getenv('HEADLESS', 'false').lower() == 'true'
    
    # Wait settings
    IMPLICIT_WAIT = int(os.getenv('IMPLICIT_WAIT', '10'))
    EXPLICIT_WAIT = int(os.getenv('EXPLICIT_WAIT', '20'))
    PAGE_LOAD_TIMEOUT = int(os.getenv('PAGE_LOAD_TIMEOUT', '30'))
    
    # Screenshot settings
    SCREENSHOT_ON_FAILURE = True
    SCREENSHOT_DIR = 'screenshots'
    
    # Report settings
    REPORT_DIR = 'reports'
    
    # Test Data - Admin User
    ADMIN_EMAIL = os.getenv('ADMIN_EMAIL', 'admin@aiu.edu')
    ADMIN_PASSWORD = os.getenv('ADMIN_PASSWORD', 'admin123')
    
    # Test Data - Student User
    STUDENT_EMAIL = os.getenv('STUDENT_EMAIL', 'john.doe@aiu.edu')
    STUDENT_PASSWORD = os.getenv('STUDENT_PASSWORD', 'password123')
    
    # Test Data - Organizer User
    ORGANIZER_EMAIL = os.getenv('ORGANIZER_EMAIL', 'organizer@aiu.edu')
    ORGANIZER_PASSWORD = os.getenv('ORGANIZER_PASSWORD', 'password123')
    
    # Test Event Data
    TEST_EVENT = {
        'title': 'Automation Test Event',
        'description': 'This is a test event created by automation',
        'type': 'EVENT',
        'category': 'Academic',
        'date': '2025-12-31',
        'time': '10:00',
        'location': 'Test Lab 101',
        'capacity': '50',
        'price': '100'
    }
    
    TEST_TRIP = {
        'title': 'Automation Test Trip',
        'description': 'This is a test trip created by automation',
        'type': 'TRIP',
        'destination': 'Alexandria',
        'start_date': '2025-12-25',
        'end_date': '2025-12-27',
        'capacity': '30',
        'price': '500'
    }
    
    @classmethod
    def get_url(cls, path=''):
        """Get full URL with path"""
        return f"{cls.BASE_URL}{path}"
    
    @classmethod
    def get_api_url(cls, path=''):
        """Get full API URL with path"""
        return f"{cls.API_BASE_URL}{path}"
