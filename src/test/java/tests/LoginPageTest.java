package tests;

import baseTest.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.LoginPage;
import playwrightPractice.utilities.DataRetriever;

import java.util.Map;

public class LoginPageTest extends BaseTest {

    LoginPage _login = null;
    public Map<String, String> data;


    /**************************************
     * @author Fahim Ahmed
     * @Date: 03/27/2026
     * @description : GOVPS-27964 [A1] Advanced Mode - Search Bar - LexID
     ******************************************/

    @Test(groups = {"regression", "patch2", "advanced", "random"}, priority = 1)
    public void PL_1() {

        _login = new LoginPage(page);

        data = DataRetriever.getSheetData(returnData("PL_1", "Login"));

        if (page != null && !data.isEmpty()) {

            step(_login.isLogoVisible(), "Logo should be visible on login page");

            step(_login.isSignInButtonVisible(), "Sign-in button should be visible");

            step(_login.getSignIn().equals(" Sign In "), "Verify Sign In button text");
            _login.clickSignIn();
        }
    }
}
