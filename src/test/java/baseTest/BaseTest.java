package baseTest;

import base.BasePage;
import com.microsoft.playwright.Page;
import org.testng.ITestResult;
import org.testng.annotations.*;
import playwrightPractice.utilities.*;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    protected Page page;
    protected static ExtentReport logger;

    protected static final String PROJECT_NAME = "Playwright-A1";

    public String returnData(String rowData, String table) {
        BasePage.logWithTimestamp("Fetching test data for: " + rowData);

        DataRetriever.readExcel();

        Map<String, String> testFilter = new HashMap<>();
        testFilter.put("TestCaseName", rowData);

        String data = DataRetriever.getQuery(table, testFilter);

        BasePage.logWithTimestamp("Data fetched successfully");

        return data;
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        BasePage.logWithTimestamp("===== Test Class Initialization Started =====");

        Config cfg = new Config();

        if (ExtentReport.extent == null) {
            String reportPath = System.getProperty("user.dir") + "/target/reports/"
                    + PROJECT_NAME + "-" + format() + "-" + cfg.getProperty("browser") + ".html";

            cfg.setProperty("extent.report.pathname", reportPath);

            BasePage.logWithTimestamp("Initializing Extent Report at: " + reportPath);

            logger = new ExtentReport(cfg);
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {

        BasePage.logWithTimestamp("----- Starting Test: " + method.getName() + " -----");

        Config cfg = new Config();

        page = Browsers.getDriver(
                cfg.getProperty("browser"),
                cfg.getProperty("headless"),
                cfg.getProperty("remoteUrl")
        );

        BasePage.logWithTimestamp("Navigating to URL: " + cfg.getProperty("url"));

        page.navigate(cfg.getProperty("url"));

        logger.createTestCase(method.getName(), "Test case for " + method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {

        BasePage.logWithTimestamp("Test execution completed for: " + result.getName());

        if (result.getStatus() == ITestResult.FAILURE) {

            BasePage.logWithTimestamp("Test FAILED: " + result.getName());
            ExtentReport.logFail("Test Failed: " + result.getName());
            ExtentReport.captureFailure(page, "Failure Screenshot");

            Browsers.closeAll(page);

            BasePage.logWithTimestamp("----- Test Finished: " + result.getName() + " -----");
        }
    }
    @AfterClass(alwaysRun = true)
    public void afterClass() {
        BasePage.logWithTimestamp("Flushing Extent Reports...");

        if (ExtentReport.extent != null) {
            ExtentReport.extent.flush();
        }

        BasePage.logWithTimestamp("===== Test Class Execution Completed =====");
    }

    public void step(boolean condition, String message) {
        if (!condition) {
            BasePage.logWithTimestamp("Step FAILED: " + message);
            ExtentReport.logFail(message);
            ExtentReport.captureFailure(page, message);
            throw new AssertionError(message);
        } else {
            BasePage.logWithTimestamp("Step PASSED: " + message);
            ExtentReport.logPass(message);
        }
    }

    public String format() {
        return format(new Date());
    }

    public static String format(Date date) {
        return new SimpleDateFormat("yyyyMMdd-HHmmss").format(date);
    }
}