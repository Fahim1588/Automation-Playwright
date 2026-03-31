package pages;

import base.BasePage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.Arrays;
import java.util.List;

public class HomePage extends BasePage {
    private final Locator uspsLogo;
    private final Locator quickToolsMenu;
    private final Locator sendMenu;
    private final Locator receiveMenu;
    private final Locator shopMenu;
    private final Locator businessMenu;
    private final Locator helpMenu;
    private final Locator internationalMenu;
    private final Locator searchButton;
    private final Locator location;
    private final Locator supportMenu;
    private final Locator informedDeliveryMenu;
    private final Locator registerMenu;
    private final Locator trackAPckageMenu;
    private final Locator uspsTrackingHeader;

    public HomePage(Page page) {
        super(page);
        this.uspsLogo= page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Image of USPS.com logo."));
        this.quickToolsMenu = page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Quick Tools"));
        this.sendMenu = page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Send"));
        this.receiveMenu = page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Receive"));
        this.shopMenu = page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Shop"));
        this.businessMenu = page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Business"));
        this.helpMenu = page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Help"));
        this.internationalMenu = page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("International"));
        this.searchButton = page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Search USPS.com"));
        this.location = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Locations"));
        this.supportMenu = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Support"));
        this.informedDeliveryMenu = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Informed Delivery").setExact(true));
        this.registerMenu = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Register / Sign In"));
        this.trackAPckageMenu = page.locator("a").filter(new Locator.FilterOptions().setHasText("Track a Package"));
        this.uspsTrackingHeader = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("USPS Tracking®"));
    }


    public boolean isMenuItemsVisible() {
        List<Locator> menuItems = Arrays.asList(
                uspsLogo, quickToolsMenu, sendMenu, receiveMenu, shopMenu,
                businessMenu, helpMenu, internationalMenu, searchButton,
                location, supportMenu, informedDeliveryMenu, registerMenu
        );

        for (Locator item : menuItems) {
            if (!isElementVisible(item)) {
                logFail("Menu item not visible: " + item);
                return false;
            }
        }

        return true;
    }

    public boolean isTrackAPackageVisible() {

        step("Hovering over Quick Tools menu");
        hoverOnElement(quickToolsMenu);

        step("Checking visibility of 'Track a Package' menu item");
        if (!isElementVisible(trackAPckageMenu)) {
            logFail("Track a Package menu item is NOT visible after hovering over Quick Tools.");
            return false;
        }

        step("Clicking 'Track a Package' menu item");
        clickElement(trackAPckageMenu);

        step("Validating USPS Tracking header visibility");
        if (!isElementVisible(uspsTrackingHeader)) {
            logFail("USPS Tracking header is NOT visible after clicking Track a Package.");
            return false;
        }

        logPass("Track a Package menu item and USPS Tracking header are visible as expected.");
        return true;
    }


}
