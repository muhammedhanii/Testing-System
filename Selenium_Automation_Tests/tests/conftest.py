"""
Pytest configuration and fixtures
"""

import pytest
from utils.driver_factory import DriverFactory
from utils.helpers import take_screenshot


@pytest.fixture(scope="function")
def driver(request):
    """
    WebDriver fixture - creates driver for each test
    Automatically closes after test completion
    """
    # Setup
    driver = DriverFactory.get_driver()
    driver.maximize_window()
    
    # Provide driver to test
    yield driver
    
    # Teardown
    # Take screenshot if test failed
    if request.node.rep_call.failed:
        test_name = request.node.name
        take_screenshot(driver, f"FAILED_{test_name}")
    
    driver.quit()


@pytest.hookimpl(tryfirst=True, hookwrapper=True)
def pytest_runtest_makereport(item, call):
    """
    Hook to capture test results for screenshot on failure
    """
    outcome = yield
    rep = outcome.get_result()
    setattr(item, f"rep_{rep.when}", rep)


def pytest_addoption(parser):
    """Add custom command line options"""
    parser.addoption(
        "--headless",
        action="store_true",
        default=False,
        help="Run tests in headless mode"
    )
    parser.addoption(
        "--browser",
        action="store",
        default="chrome",
        help="Browser to use for tests (chrome, firefox, edge)"
    )


def pytest_configure(config):
    """Configure pytest with custom options"""
    # Set headless mode from command line
    if config.getoption("--headless"):
        from utils.config import Config
        Config.HEADLESS = True
    
    # Set browser from command line
    browser = config.getoption("--browser")
    if browser:
        from utils.config import Config
        Config.BROWSER = browser
