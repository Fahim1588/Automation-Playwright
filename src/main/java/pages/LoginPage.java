package pages;

import base.BasePage;
import com.microsoft.playwright.Page;

public class LoginPage extends BasePage {

    public LoginPage(Page page) {
        super(page);
    }

    //Test

    private final String signIn = "button:has-text('Sign In')";
    private final String edlinkLogo = "img[alt='LDOE']";

    public boolean isSignInButtonVisible() {
        boolean visible = page.isVisible(signIn);
        BasePage.logWithTimestamp("Sign In button visible: " + visible);
        return visible;
    }

    public boolean isLogoVisible() {
        boolean found = page.isVisible(edlinkLogo);

        if (found) {
            BasePage.logWithTimestamp("Edlink logo is visible on login page.");
        } else {
            BasePage.logWithTimestamp("Edlink logo is NOT visible on login page.");
        }

        return found;
    }

    public String getSignIn() {
        String text = page.textContent(signIn);
        BasePage.logWithTimestamp("Retrieved text from Sign In button: " + text);
        return text;
    }

    public void clickSignIn() {
        page.click(signIn);
        BasePage.logWithTimestamp("Clicked on Sign In button.");
    }
}