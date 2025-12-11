"""
WebDriver Factory - Creates and manages WebDriver instances
"""

from selenium import webdriver
from selenium.webdriver.chrome.service import Service as ChromeService
from selenium.webdriver.firefox.service import Service as FirefoxService
from selenium.webdriver.edge.service import Service as EdgeService
from webdriver_manager.chrome import ChromeDriverManager
from webdriver_manager.firefox import GeckoDriverManager
from webdriver_manager.microsoft import EdgeChromiumDriverManager
from utils.config import Config


class DriverFactory:
    """Factory class to create WebDriver instances"""
    
    @staticmethod
    def get_driver(browser=None):
        """
        Create and return a WebDriver instance
        
        Args:
            browser (str): Browser name ('chrome', 'firefox', 'edge')
            
        Returns:
            WebDriver: Configured WebDriver instance
        """
        browser = browser or Config.BROWSER
        browser = browser.lower()
        
        if browser == 'chrome':
            return DriverFactory._get_chrome_driver()
        elif browser == 'firefox':
            return DriverFactory._get_firefox_driver()
        elif browser == 'edge':
            return DriverFactory._get_edge_driver()
        else:
            raise ValueError(f"Unsupported browser: {browser}")
    
    @staticmethod
    def _get_chrome_driver():
        """Create Chrome WebDriver"""
        options = webdriver.ChromeOptions()
        
        if Config.HEADLESS:
            options.add_argument('--headless')
            options.add_argument('--disable-gpu')
        
        options.add_argument('--no-sandbox')
        options.add_argument('--disable-dev-shm-usage')
        options.add_argument('--window-size=1920,1080')
        options.add_argument('--disable-blink-features=AutomationControlled')
        options.add_experimental_option('excludeSwitches', ['enable-logging'])
        
        service = ChromeService(ChromeDriverManager().install())
        driver = webdriver.Chrome(service=service, options=options)
        
        driver.implicitly_wait(Config.IMPLICIT_WAIT)
        driver.set_page_load_timeout(Config.PAGE_LOAD_TIMEOUT)
        
        return driver
    
    @staticmethod
    def _get_firefox_driver():
        """Create Firefox WebDriver"""
        options = webdriver.FirefoxOptions()
        
        if Config.HEADLESS:
            options.add_argument('--headless')
        
        options.add_argument('--width=1920')
        options.add_argument('--height=1080')
        
        service = FirefoxService(GeckoDriverManager().install())
        driver = webdriver.Firefox(service=service, options=options)
        
        driver.implicitly_wait(Config.IMPLICIT_WAIT)
        driver.set_page_load_timeout(Config.PAGE_LOAD_TIMEOUT)
        
        return driver
    
    @staticmethod
    def _get_edge_driver():
        """Create Edge WebDriver"""
        options = webdriver.EdgeOptions()
        
        if Config.HEADLESS:
            options.add_argument('--headless')
            options.add_argument('--disable-gpu')
        
        options.add_argument('--no-sandbox')
        options.add_argument('--disable-dev-shm-usage')
        options.add_argument('--window-size=1920,1080')
        
        service = EdgeService(EdgeChromiumDriverManager().install())
        driver = webdriver.Edge(service=service, options=options)
        
        driver.implicitly_wait(Config.IMPLICIT_WAIT)
        driver.set_page_load_timeout(Config.PAGE_LOAD_TIMEOUT)
        
        return driver
