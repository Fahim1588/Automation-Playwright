package playwrightPractice.utilities;

import base.BasePage;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.microsoft.playwright.Page;

import java.io.File;
import java.util.Base64;

public class ExtentReport {

    private static ExtentReports extent;
    private static ExtentSparkReporter htmlReporter;

    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();


    // ✅ Initialize report once per suite
    public static void init(Config cfg) {

        if (extent != null)
            return;

        extent = new ExtentReports();

        String reportPath = cfg.getProperty("extent.report.pathname");

        new File(reportPath).getParentFile().mkdirs();

        htmlReporter = new ExtentSparkReporter(reportPath);

        htmlReporter.config().setDocumentTitle("Playwright-A1 Test Report");
        htmlReporter.config().setReportName("Playwright-A1 Test Execution Report");
        htmlReporter.config().setTheme(Theme.STANDARD);

        extent.attachReporter(htmlReporter);

        extent.setSystemInfo("Environment",
                cfg.getProperty("extent.report.environment"));

        extent.setSystemInfo("User Name",
                Info.getUsername());

        extent.setSystemInfo("Host Name",
                Info.getComputerName());

        extent.setSystemInfo("OS",
                Info.getOsName());

        extent.setSystemInfo("HeadLess",
                System.getProperty("headless",
                        cfg.getProperty("headless")));

        extent.setSystemInfo("URL",
                System.getProperty("url",
                cfg.getProperty("url")));

        extent.setSystemInfo("Browser",
                System.getProperty("browser",
                        cfg.getProperty("browser")));

        extent.setAnalysisStrategy(AnalysisStrategy.CLASS);
    }


    // ✅ Create test case (thread-safe)
    public static void createTestCase(String name, String description) {

        ExtentTest test = extent.createTest(name, description);

        testThread.set(test);
    }


    // ✅ Logging helpers
    public static void logInfo(String message) {

        if (testThread.get() != null)
            testThread.get().info(
                    "<span style='color:blue'>"+"INFO ➤ " + message + "</span>");
    }


    public static void logPass(String message) {

        if (testThread.get() != null)
            testThread.get().pass("<span style='color:green; font-weight:bold;'>PASS ➤ </span>" +
                    "<span style='color:green'>" + message + "</span>");
    }


    public static void logFail(String message) {

        if (testThread.get() != null)
            testThread.get().fail("<span style='color:red; font-weight:bold;'>FAIL ➤ </span>" +
                    "<span style='color:red'>" + message + "</span>");
    }


    public static void logWarn(String message) {

        if (testThread.get() != null)
            testThread.get().warning("<span style='color:orange; font-weight:bold;'>WARN ➤ </span>" +
                    "<span style='color:orange'>" + message + "</span>");
    }


    public static ExtentTest getTest() {

        return testThread.get();
    }


    // ✅ Screenshot attach on failure
    public static void captureFailure(Page page, String message) {

        try {

            if (page == null) {

                BasePage.logFail(
                        "Page is NULL - screenshot skipped");

                return;
            }

            byte[] screenshot = page.screenshot();

            String base64 =
                    Base64.getEncoder().encodeToString(screenshot);

            if (getTest() != null) {

                getTest().fail(
                        message,
                        MediaEntityBuilder
                                .createScreenCaptureFromBase64String(base64)
                                .build());
            }

            BasePage.logFail("📸 Screenshot captured");

        } catch (Exception e) {

            BasePage.logFail(
                    "Screenshot error: " + e.getMessage());
        }
    }


    // ✅ Attach video recording
    public static void attachVideo(Page page) {

        try {

            if (page != null && page.video() != null) {

                String path =
                        page.video().path().toString();

                if (getTest() != null)
                    getTest().info("🎥 Video: " + path);
            }

        } catch (Exception e) {

            BasePage.logWarn(
                    "Video attach failed: " + e.getMessage());
        }
    }


    // ✅ Flush report after execution
    public static void close() {

        if (extent != null) {

            extent.flush();

            System.out.println(
                    "Extent Report flushed successfully.");
        }
    }
}