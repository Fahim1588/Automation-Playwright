package tests;

import baseTest.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HomePage;
import pages.LoginPage;
import playwrightPractice.utilities.DataRetriever;

import java.util.Map;

public class HomePageTest extends BaseTest {
    HomePage _home = null;
    public Map<String, String> data;

    /* @author Fahim Ahmed
     * @Date: 03/27/2026
     * @description : GOVPS-27964 [A1] Advanced Mode - Search Bar - LexID
     ******************************************/

    @Test(groups = {"regression", "patch2", "advanced", "random"}, priority = 1)
    public void PL_3() {

        _home = new HomePage(page);
        SoftAssert sa = new SoftAssert();

        data = DataRetriever.getSheetData(returnData("PL_3", "Home"));

        if (page != null && !data.isEmpty()) {
            sa.assertTrue(_home.isMenuItemsVisible(), "Menu items are visible on home page");
            sa.assertTrue(_home.isTrackAPackageVisible(), "Track a Package menu item should be visible");

        }
        sa.assertAll();

    }

}
