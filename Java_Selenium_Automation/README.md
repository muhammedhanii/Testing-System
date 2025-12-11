# AIU Trips & Events - Java Selenium Automation Framework

## 📋 Overview

This is a comprehensive **Java + Selenium + TestNG** automation testing framework for the AIU Trips & Events Management System. The framework follows industry best practices including **Page Object Model (POM)** design pattern, **data-driven testing**, and **advanced reporting**.

## 🏗️ Framework Architecture

```
Java_Selenium_Automation/
├── pom.xml                                    # Maven dependencies and configuration
├── testng.xml                                 # TestNG suite configuration
├── src/
│   ├── test/
│   │   ├── java/com/aiu/
│   │   │   ├── pages/                         # Page Object Model classes
│   │   │   │   ├── BasePage.java             # Base page with common methods
│   │   │   │   ├── LoginPage.java            # Login page object
│   │   │   │   ├── DashboardPage.java        # Dashboard page object
│   │   │   │   ├── EventsPage.java           # Events page object
│   │   │   │   ├── CreateEventPage.java      # Create event page object
│   │   │   │   └── BookingsPage.java         # Bookings page object
│   │   │   ├── tests/                         # Test classes
│   │   │   │   ├── TestBase.java             # Base test class
│   │   │   │   ├── LoginTest.java            # Login test cases
│   │   │   │   ├── EventTest.java            # Event test cases
│   │   │   │   └── BookingTest.java          # Booking test cases
│   │   │   └── utils/                         # Utility classes
│   │   │       ├── ConfigReader.java         # Configuration reader
│   │   │       ├── WebDriverFactory.java     # WebDriver factory
│   │   │       ├── TestUtils.java            # Test utilities
│   │   │       └── ExtentManager.java        # ExtentReports manager
│   │   └── resources/
│   │       ├── config.properties              # Test configuration
│   │       └── log4j2.xml                     # Logging configuration
├── test-output/                               # Test execution output
│   ├── reports/                               # HTML test reports
│   ├── screenshots/                           # Failure screenshots
│   └── logs/                                  # Execution logs
└── README.md                                  # This file
```

## 🛠️ Technology Stack

- **Java 11** - Programming language
- **Selenium WebDriver 4.15.0** - Browser automation
- **TestNG 7.8.0** - Test framework
- **Maven** - Build and dependency management
- **WebDriverManager 5.6.2** - Automatic driver management
- **ExtentReports 5.1.1** - Advanced HTML reporting
- **Log4j 2.21.1** - Logging framework
- **JavaFaker 1.0.2** - Test data generation

## ✨ Key Features

### 1. Page Object Model (POM)
- Clean separation of test logic and page elements
- Reusable page classes
- Easy maintenance and scalability

### 2. Data-Driven Testing
- Configuration via `config.properties`
- Support for external test data
- Parameterized tests

### 3. Cross-Browser Testing
- Chrome, Firefox, Edge support
- Headless mode support
- Automatic driver management

### 4. Advanced Reporting
- ExtentReports with detailed HTML reports
- Screenshots on test failure
- Test execution logs
- Pass/Fail statistics

### 5. Robust Synchronization
- Explicit waits
- Implicit waits
- Page load timeouts
- Custom wait utilities

### 6. Logging
- Log4j integration
- Console and file logging
- Different log levels (INFO, DEBUG, ERROR)

## 📦 Prerequisites

Before running the tests, ensure you have:

1. **Java Development Kit (JDK) 11 or higher**
   ```bash
   java -version
   ```

2. **Apache Maven 3.6 or higher**
   ```bash
   mvn -version
   ```

3. **Web Browser** (Chrome/Firefox/Edge)
   - Drivers are automatically managed by WebDriverManager

4. **Internet Connection** (for downloading dependencies)

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/muhammedhanii/Testing-System.git
cd Testing-System/Java_Selenium_Automation
```

### 2. Install Dependencies
```bash
mvn clean install
```

This command will:
- Download all Maven dependencies
- Compile the project
- Run basic validation

### 3. Configure Test Environment

Edit `src/test/resources/config.properties`:

```properties
# Application URL
base.url=http://localhost:3001

# Browser Configuration
browser=chrome
headless=false

# Test Credentials
admin.email=admin@aiu.edu
admin.password=admin123
```

## 🧪 Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Suite
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run Specific Test Class
```bash
mvn test -Dtest=LoginTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=LoginTest#testValidAdminLogin
```

### Run in Headless Mode
Edit `config.properties` and set:
```properties
headless=true
```

### Run with Different Browser
Edit `config.properties` and set:
```properties
browser=firefox  # or edge
```

### Run Tests in Parallel
Edit `testng.xml` and set:
```xml
<suite name="Test Suite" parallel="tests" thread-count="3">
```

## 📊 Test Reports

### ExtentReports
After test execution, HTML report is generated at:
```
test-output/reports/AIU_Trips_Events_Test_Report.html
```

Open in browser to view:
- Test execution summary
- Pass/Fail statistics
- Test duration
- Screenshots of failures
- Detailed test steps

### TestNG Reports
Default TestNG reports at:
```
test-output/index.html
test-output/emailable-report.html
```

### Screenshots
Failure screenshots are saved to:
```
test-output/screenshots/
```

### Logs
Execution logs are saved to:
```
test-output/logs/automation.log
```

## 📝 Test Scenarios

### Login Tests (`LoginTest.java`)
- ✅ Valid admin login
- ✅ Valid student login
- ✅ Invalid password
- ✅ Non-existent email
- ✅ Empty credentials
- ✅ Logout functionality

### Event Tests (`EventTest.java`)
- ✅ Events page access
- ✅ Events display
- ✅ Search events
- ✅ View event details
- ✅ Create event button visibility
- ✅ Book event

### Booking Tests (`BookingTest.java`)
- ✅ Bookings page access
- ✅ View bookings
- ✅ View ticket with QR code
- ✅ Cancel booking
- ✅ Booking status display
- ✅ Complete booking flow

## 🎯 Page Objects

### BasePage.java
Base class with common methods:
- `navigateTo(url)` - Navigate to URL
- `click(element)` - Click element with wait
- `type(element, text)` - Type text with wait
- `getText(element)` - Get element text
- `isDisplayed(element)` - Check element visibility

### LoginPage.java
```java
LoginPage loginPage = new LoginPage(driver);
loginPage.open();
loginPage.login(email, password);
loginPage.loginAsAdmin();
```

### EventsPage.java
```java
EventsPage eventsPage = new EventsPage(driver);
eventsPage.open();
eventsPage.searchEvents("Conference");
eventsPage.clickFirstEvent();
```

### BookingsPage.java
```java
BookingsPage bookingsPage = new BookingsPage(driver);
bookingsPage.navigateToBookings();
bookingsPage.viewFirstTicket();
bookingsPage.cancelFirstBooking();
```

## 🔧 Configuration

### config.properties
```properties
# URLs
base.url=http://localhost:3001
api.url=http://localhost:8081

# Browser
browser=chrome
headless=false

# Timeouts (seconds)
implicit.wait=10
explicit.wait=20
page.load.timeout=30

# Test Users
admin.email=admin@aiu.edu
admin.password=admin123
student.email=john.doe@aiu.edu
student.password=password123

# Reporting
screenshot.on.failure=true
screenshot.path=test-output/screenshots/
report.path=test-output/reports/
```

### testng.xml
Configure test suite:
```xml
<suite name="Test Suite" parallel="false">
    <test name="Login Tests">
        <classes>
            <class name="com.aiu.tests.LoginTest"/>
        </classes>
    </test>
</suite>
```

## 🐛 Debugging

### Enable Debug Logging
Edit `log4j2.xml`:
```xml
<Root level="debug">
```

### Take Screenshots Manually
```java
String screenshotPath = TestUtils.takeScreenshot(driver, "test_name");
```

### View Console Logs
```bash
mvn test -X  # Maven debug mode
```

## 📚 Best Practices

### 1. Page Object Model
- Keep page locators in page classes
- Use meaningful method names
- Return page objects for method chaining

### 2. Waits
- Use explicit waits over Thread.sleep()
- Wait for elements to be clickable before clicking
- Wait for elements to be visible before interacting

### 3. Test Independence
- Each test should be independent
- Use @BeforeMethod for setup
- Clean up test data in @AfterMethod

### 4. Assertions
- Use meaningful assertion messages
- Assert expected behavior, not implementation

### 5. Logging
- Log important test steps
- Use appropriate log levels
- Log test data (except sensitive info)

## 🔒 Security Notes

- **Never commit credentials** to version control
- Use environment variables for sensitive data
- Keep test data separate from production data
- Review logs before sharing

## 🤝 Contributing

### Adding New Test Cases

1. **Create/Update Page Object:**
```java
public class NewPage extends BasePage {
    @FindBy(id = "element")
    private WebElement element;
    
    public void performAction() {
        click(element);
    }
}
```

2. **Create Test Class:**
```java
public class NewTest extends TestBase {
    @Test
    public void testNewFeature() {
        // Test implementation
    }
}
```

3. **Update testng.xml:**
```xml
<test name="New Tests">
    <classes>
        <class name="com.aiu.tests.NewTest"/>
    </classes>
</test>
```

## 🐞 Troubleshooting

### Issue: WebDriver not found
**Solution:** WebDriverManager handles this automatically. Ensure internet connection.

### Issue: Tests failing randomly
**Solution:** Increase wait times in `config.properties`

### Issue: Element not found
**Solution:** Update locators in page classes. Use browser dev tools to find correct locators.

### Issue: Browser not opening
**Solution:** Check browser installation and WebDriverManager logs.

### Issue: Tests running slow
**Solution:** Enable headless mode or increase timeout values.

## 📞 Support

### Documentation
- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [TestNG Documentation](https://testng.org/doc/)
- [Maven Documentation](https://maven.apache.org/guides/)

### Issues
For issues or questions, please check:
1. Test execution logs: `test-output/logs/automation.log`
2. ExtentReports: `test-output/reports/`
3. Screenshots: `test-output/screenshots/`

## 📄 License

This automation framework is part of the AIU Trips & Events System project.

## ✨ Acknowledgments

**Testing Team:**
- QA Automation Architect
- Senior QA Engineer
- QA Team

**Tools & Technologies:**
- Selenium WebDriver
- TestNG
- Maven
- ExtentReports
- WebDriverManager

---

## 🎯 Quick Start Checklist

- [ ] Install Java 11+
- [ ] Install Maven 3.6+
- [ ] Clone repository
- [ ] Run `mvn clean install`
- [ ] Update `config.properties`
- [ ] Run `mvn test`
- [ ] View reports in `test-output/reports/`

---

## 📈 Test Execution Statistics

```
┌──────────────────────────────────────────────────┐
│  TEST EXECUTION SUMMARY                          │
├──────────────────────────────────────────────────┤
│  Total Test Cases:              18               │
│  ├─ Login Tests:                6                │
│  ├─ Event Tests:                6                │
│  └─ Booking Tests:              6                │
│                                                   │
│  Automation Coverage:           100%             │
│  Framework:                     Java + Selenium  │
│  Design Pattern:                Page Object Model│
│  Reporting:                     ExtentReports    │
└──────────────────────────────────────────────────┘
```

---

**Version:** 1.0.0  
**Last Updated:** December 2025  
**Status:** ✅ Production Ready

**🚀 Happy Testing with Java & Selenium! 🚀**
