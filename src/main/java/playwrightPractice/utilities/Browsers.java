package playwrightPractice.utilities;

import com.microsoft.playwright.*;
import base.BasePage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Browsers {

    public static Page getDriver(String browserName, String headless, String remoteUrl) {

        boolean isCi = "true".equalsIgnoreCase(System.getenv("CI"))
                || "true".equalsIgnoreCase(System.getProperty("CiCd", "false"));

        boolean runHeadless = headless == null
                ? isCi
                : "true".equalsIgnoreCase(headless);

        Playwright playwright = Playwright.create();
        Browser browser;

        boolean isRemote = remoteUrl != null && !remoteUrl.isEmpty();

        if (isRemote) {
            BasePage.logWithTimestamp("Connecting to remote browser at: " + remoteUrl);
            browser = playwright.chromium().connect(remoteUrl);
        } else {
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                    .setHeadless(runHeadless)
                    .setArgs(Arrays.asList(
                            "--no-sandbox",
                            "--disable-dev-shm-usage"
                    ));

            switch (browserName.toLowerCase()) {
                case "firefox":
                    BasePage.logWithTimestamp("Launching Firefox browser");
                    browser = playwright.firefox().launch(options);
                    break;
                case "chrome":
                    BasePage.logWithTimestamp("Launching Chrome browser");
                    browser = playwright.chromium().launch(options.setChannel("chrome"));
                    break;
                case "edge":
                    BasePage.logWithTimestamp("Launching Edge browser");
                    browser = playwright.chromium().launch(options.setChannel("msedge"));
                    break;
                default:
                    BasePage.logWithTimestamp("Launching Chromium browser");
                    browser = playwright.chromium().launch(options);
            }
        }

        // Prepare target video folder
        Path videoDir = Paths.get(System.getProperty("user.dir"), "target", "videos");
        try {
            Files.createDirectories(videoDir);
        } catch (Exception e) {
            BasePage.logWithTimestamp("Failed to create video folder: " + e.getMessage());
        }

        // Create context with video recording for CI or headless
        Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setAcceptDownloads(true)
                .setViewportSize(1325, 744);

        if (isCi) {
            BasePage.logWithTimestamp("Enabling video recording at: " + videoDir.toAbsolutePath());
            contextOptions.setRecordVideoDir(videoDir);
        }

        BrowserContext context = browser.newContext(contextOptions);

        Page page = context.newPage();
        BasePage.logWithTimestamp("Page initialized for test");

        return page;
    }

    public static void closeAll(Page page) {
        if (page != null) {
            Browser browser = page.context().browser();
            try {
                page.context().close();
                BasePage.logWithTimestamp("Browser context closed");
            } catch (Exception e) {
                BasePage.logWithTimestamp("Error closing context: " + e.getMessage());
            }
            try {
                browser.close();
                BasePage.logWithTimestamp("Browser closed");
            } catch (Exception e) {
                BasePage.logWithTimestamp("Error closing browser: " + e.getMessage());
            }
        }
    }
}