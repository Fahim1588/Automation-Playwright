package baseTest;

import base.BasePage;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Browser;
import org.testng.ITestResult;
import org.testng.annotations.*;
import playwrightPractice.utilities.*;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    protected Page page;
    protected BrowserContext context;
    protected Browser browser;

    protected static final String PROJECT_NAME = "Playwright-A1";


    public String returnData(String rowData, String table) {

        DataRetriever.readExcel();

        Map<String, String> testFilter = new HashMap<>();

        testFilter.put("TestCaseName", rowData);

        return DataRetriever.getQuery(table, testFilter);
    }


    @BeforeClass(alwaysRun = true)
    public void beforeClass() {

        BasePage.logWithTimestamp("@BeforeClass: Initializing ExtentReport");

        Config cfg = new Config();

        if (ExtentReport.getTest() == null) {

            String reportPath =
                    System.getProperty("user.dir")
                            + "/target/reports/"
                            + PROJECT_NAME
                            + "-"
                            + format()
                            + ".html";

            cfg.setProperty("extent.report.pathname", reportPath);

            BasePage.logWithTimestamp(
                    "Initializing ExtentReport at: " + reportPath
            );

            // ✅ Correct initialization (singleton style)
            ExtentReport.init(cfg);
        }
    }


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {

        Config cfg = new Config();

        String browserName =
                cfg.getRuntimeProperty("browser");

        boolean runHeadless =
                Boolean.parseBoolean(
                        cfg.getRuntimeProperty("headless"));

        String remoteUrl =
                cfg.getRuntimeProperty("remoteUrl");


        BasePage.logWithTimestamp(
                "Launching browser: "
                        + browserName
                        + ", headless: "
                        + runHeadless
        );


        // get fresh browser per test
        page =
                Browsers.getDriver(
                        browserName,
                        Boolean.toString(runHeadless),
                        remoteUrl
                );


        context = page.context();

        browser = context.browser();


        // navigate to URL
        String appUrl =
                cfg.getRuntimeProperty("url");

        BasePage.logWithTimestamp(
                "Navigating to URL: "
                        + appUrl
        );

        page.navigate(appUrl);

        BasePage.logWithTimestamp(
                "Current page URL: "
                        + page.url()
        );


        // ✅ Correct test creation call
        ExtentReport.createTestCase(
                method.getName(),
                "Test case for " + method.getName()
        );
    }


    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {

        BasePage.logWithTimestamp(
                "Test execution completed for: "
                        + result.getName()
        );


        if (result.getStatus() == ITestResult.FAILURE) {

            BasePage.logWithTimestamp(
                    "Test FAILED: "
                            + result.getName()
            );

            ExtentReport.logFail(
                    "Test Failed: "
                            + result.getName()
            );

            ExtentReport.captureFailure(
                    page,
                    "Failure Screenshot"
            );


            try {

                BasePage.logWithTimestamp(
                        "Attaching failure video..."
                );

                ExtentReport.attachVideo(page);

            } catch (Exception e) {

                BasePage.logWithTimestamp(
                        "Video attach failed: "
                                + e.getMessage()
                );
            }


        } else if (result.getStatus() == ITestResult.SUCCESS) {

            BasePage.logWithTimestamp(
                    "Test PASSED: "
                            + result.getName()
            );

            ExtentReport.logPass(
                    "Test Passed: "
                            + result.getName()
            );


        } else {

            BasePage.logWithTimestamp(
                    "Test SKIPPED: "
                            + result.getName()
            );
        }


        // close browser after each test
        try {

            if (context != null)
                context.close();

            if (browser != null)
                browser.close();

        } catch (Exception e) {

            BasePage.logWithTimestamp(
                    "Teardown error: "
                            + e.getMessage()
            );
        }
    }


    public void step(boolean condition, String message) {

        if (!condition) {

            ExtentReport.captureFailure(
                    page,
                    message
            );

            throw new AssertionError(message);

        } else {

            ExtentReport.logPass(message);
        }
    }


    public String format() {

        Date nowIs = new Date();

        return format(nowIs);
    }


    public static String format(Date date) {

        SimpleDateFormat sm =
                new SimpleDateFormat("yyyyMMdd-HHmmss");

        return sm.format(date);
    }


    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {

        ExtentReport.close();
    }
}