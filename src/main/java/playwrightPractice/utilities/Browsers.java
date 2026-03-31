package playwrightPractice.utilities;

import base.BasePage;
import com.microsoft.playwright.*;

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
            BasePage.logWithTimestamp("Launching local browser: " + browserName);
            BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                    .setHeadless(runHeadless)
                    .setArgs(Arrays.asList("--no-sandbox", "--disable-dev-shm-usage","--start-maximized"));

            switch (browserName.toLowerCase()) {
                case "firefox":
                    browser = playwright.firefox().launch(options);
                    break;
                case "chrome":
                    browser = playwright.chromium().launch(options.setChannel("chrome"));
                    break;
                case "edge":
                    browser = playwright.chromium().launch(options.setChannel("msedge"));
                    break;
                default:
                    browser = playwright.chromium().launch(options);
            }
        }

        BrowserContext context = browser.newContext(
                new Browser.NewContextOptions()
                        .setAcceptDownloads(true)
                        .setViewportSize(null)
                        .setRecordVideoDir(Paths.get(System.getProperty("user.dir"), "target/videos/")) // save under target
                        .setRecordVideoSize(1280, 720)
                        .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120 Safari/537.36")
                        .setTimezoneId("America/New_York")
                        .setGeolocation(33.7490, -84.3880) // Atlanta area
                        .setPermissions(Arrays.asList("geolocation"))
        );

        BasePage.logWithTimestamp("Browser context initialized, video recording enabled at target/videos/");

        return context.newPage();
    }

    public static void closeAll(Page page) {
        if (page != null) {
            Browser browser = page.context().browser();
            page.context().close();
            browser.close();
            BasePage.logWithTimestamp("Browser closed successfully");
        }
    }
}