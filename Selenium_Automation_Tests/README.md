# AIU Trips & Events - Selenium Automation Tests

## Project Structure (Page Object Model)

```
Selenium_Automation_Tests/
├── pages/                      # Page Object Model classes
│   ├── __init__.py
│   ├── base_page.py           # Base page class with common methods
│   ├── login_page.py          # Login page object
│   ├── register_page.py       # Registration page object
│   ├── dashboard_page.py      # Dashboard page object
│   ├── events_page.py         # Events listing page object
│   ├── create_event_page.py   # Create event page object
│   ├── event_details_page.py  # Event details page object
│   ├── bookings_page.py       # My bookings page object
│   └── ticket_page.py         # Ticket/QR validation page object
├── tests/                      # Test cases
│   ├── __init__.py
│   ├── conftest.py            # Pytest fixtures and configuration
│   ├── test_login.py          # Login tests
│   ├── test_registration.py   # Registration tests
│   ├── test_create_event.py   # Create event tests
│   ├── test_book_event.py     # Book event tests
│   ├── test_cancel_booking.py # Cancel booking tests
│   └── test_qr_validation.py  # QR ticket validation tests
├── utils/                      # Utility functions
│   ├── __init__.py
│   ├── driver_factory.py      # WebDriver factory
│   ├── config.py              # Configuration settings
│   └── helpers.py             # Helper functions
├── reports/                    # Test reports (auto-generated)
├── screenshots/                # Screenshots on failure
├── requirements.txt            # Python dependencies
├── pytest.ini                  # Pytest configuration
└── README.md                   # This file
```

## Prerequisites

- Python 3.8 or higher
- pip (Python package installer)
- Google Chrome browser (or Firefox/Edge)
- ChromeDriver (or appropriate driver for your browser)

## Installation

### 1. Install Python Dependencies

```bash
cd Selenium_Automation_Tests
pip install -r requirements.txt
```

### 2. Install WebDriver

**Option A: Using webdriver-manager (Recommended)**
- Already included in requirements.txt
- Automatically downloads and manages drivers

**Option B: Manual Installation**
- Download ChromeDriver from: https://chromedriver.chromium.org/
- Add to system PATH

## Configuration

Edit `utils/config.py` to configure:
- Base URL (default: http://localhost:3001)
- Browser type (Chrome, Firefox, Edge)
- Implicit wait time
- Screenshot settings
- Test data

## Running Tests

### Run All Tests
```bash
pytest tests/
```

### Run Specific Test File
```bash
pytest tests/test_login.py
```

### Run Specific Test
```bash
pytest tests/test_login.py::test_valid_login
```

### Run with Verbose Output
```bash
pytest tests/ -v
```

### Run with HTML Report
```bash
pytest tests/ --html=reports/report.html --self-contained-html
```

### Run in Headless Mode
```bash
pytest tests/ --headless
```

### Run in Parallel (faster)
```bash
pytest tests/ -n 4
```

## Test Scenarios Covered

### 1. Login Tests (`test_login.py`)
- ✅ Valid user login
- ✅ Login with invalid password
- ✅ Login with non-existent email
- ✅ Login with empty credentials
- ✅ Logout functionality

### 2. Registration Tests (`test_registration.py`)
- ✅ Valid user registration
- ✅ Registration with existing email
- ✅ Registration with invalid email format
- ✅ Registration with weak password
- ✅ Registration with missing required fields

### 3. Create Event Tests (`test_create_event.py`)
- ✅ Create event with valid data
- ✅ Create event with past date (negative test)
- ✅ Create event with missing required fields
- ✅ Create trip with valid data

### 4. Book Event Tests (`test_book_event.py`)
- ✅ Book event with available seats
- ✅ Attempt to book full event
- ✅ Book event and verify ticket generation
- ✅ Book multiple events

### 5. Cancel Booking Tests (`test_cancel_booking.py`)
- ✅ Cancel confirmed booking
- ✅ Verify seat released after cancellation
- ✅ Attempt to cancel already cancelled booking

### 6. QR Ticket Validation Tests (`test_qr_validation.py`)
- ✅ Validate valid QR code
- ✅ Validate invalid QR code
- ✅ Validate already used QR code

## Test Data

Test data is managed in `utils/config.py`:
- Test users (student, organizer, admin)
- Sample events
- Booking data

For data-driven tests, CSV files can be used in `tests/test_data/` directory.

## Reporting

### HTML Reports
HTML reports are generated in the `reports/` directory:
```bash
pytest tests/ --html=reports/report.html
```

### Allure Reports (Optional)
For advanced reporting with Allure:
```bash
# Install Allure
pip install allure-pytest

# Run tests with Allure
pytest tests/ --alluredir=allure-results

# Generate and open report
allure serve allure-results
```

### Screenshots
Screenshots are automatically captured on test failure and saved to `screenshots/` directory.

## Continuous Integration (CI/CD)

### GitHub Actions Example
Create `.github/workflows/selenium-tests.yml`:

```yaml
name: Selenium Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v2
    
    - name: Set up Python
      uses: actions/setup-python@v2
      with:
        python-version: '3.9'
    
    - name: Install dependencies
      run: |
        cd Selenium_Automation_Tests
        pip install -r requirements.txt
    
    - name: Run tests
      run: |
        cd Selenium_Automation_Tests
        pytest tests/ --html=reports/report.html
    
    - name: Upload test results
      uses: actions/upload-artifact@v2
      if: always()
      with:
        name: test-results
        path: Selenium_Automation_Tests/reports/
```

## Best Practices

### 1. Page Object Model (POM)
- Each page has its own class
- Locators are defined in page classes
- Business logic in page methods
- Tests only call page methods

### 2. Wait Strategies
- Use explicit waits (WebDriverWait)
- Avoid hard-coded sleeps
- Wait for elements to be clickable/visible

### 3. Test Independence
- Each test should be independent
- Use fixtures for setup/teardown
- Clean up test data after execution

### 4. Assertions
- Use clear, descriptive assertion messages
- Assert expected behavior, not implementation

### 5. Error Handling
- Screenshot on failure
- Detailed error messages
- Proper exception handling

## Troubleshooting

### Common Issues

**1. WebDriver not found**
```
Solution: Install webdriver-manager or download driver manually
pip install webdriver-manager
```

**2. Element not found**
```
Solution: Increase wait time or check locator strategy
```

**3. Tests failing randomly**
```
Solution: Use explicit waits, check for dynamic content loading
```

**4. Browser not opening**
```
Solution: Check browser and driver compatibility
```

## Project Dependencies

Main dependencies (see `requirements.txt` for complete list):
- `selenium` - Web automation framework
- `pytest` - Testing framework
- `pytest-html` - HTML report generation
- `webdriver-manager` - Automatic driver management
- `pytest-xdist` - Parallel test execution
- `Faker` - Generate fake test data

## Support

For issues or questions:
1. Check test logs in `reports/` directory
2. Review screenshots in `screenshots/` directory
3. Verify configuration in `utils/config.py`
4. Ensure application is running at configured base URL

## Authors

- QA Team
- Automation Architect
- Senior QA Engineer

## Version

Version: 1.0.0
Last Updated: December 2025

## License

This project is part of the AIU Trips & Events System testing suite.
