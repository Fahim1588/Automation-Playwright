package base;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.LoadState;
import playwrightPractice.utilities.ExtentReport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class BasePage {

    protected Page page;
    protected ExtentReport logger;
    public BasePage(Page page) {
        this.page = page;
        //this.logger = logger;
    }

    public boolean isElementVisible(Locator locator) {
        step("Checking visibility of element: " + locator);
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        boolean visible = locator.isVisible();
        logWithTimestamp("Element visibility: " + visible);
        return visible;
    }

    public void hoverOnElement(Locator locator) {
        step("Hovering over element: " + locator);
        locator.hover();
        logWithTimestamp("Hovered on element: " + locator);
    }

    public void clickElement(Locator locator) {
        step("Clicking on element: " + locator);
        locator.click();
        logWithTimestamp("Clicked on element: " + locator);
    }

//previous Code with Selenium WebDriver is removed and replaced with Playwright code below

    public Locator findState(String searchText) {
        return page.locator("//span[@class='mdc-list-item__primary-text' and contains(text(),'" + searchText + "')]");
    }

    public Locator selectDPPA(String searchText) {
        return page.locator("//mat-option/span[contains(text(),'" + searchText + "')]");
    }

    public Locator selectDDvalue(String searchText) {
        return page.locator("//mat-option//span[contains(text(),'" + searchText + "')]");
    }

    public void RWselectValueDropDown(String we, String value) {
        page.locator(we).click();
        page.waitForTimeout(1000);
        findState(value).click();
    }

    public void RWselectValueDropDown1(String we, String value) {
        page.locator(we).click();
        page.waitForTimeout(1000);
        selectDDvalue(value).click();
    }

    protected Locator test() { return page.locator("//mat-select[@placeholder='Select a Pre-Defined Range...']"); }

    public void RWselectdateValueDropDown(String we, String value) {
        String validatedate = test().innerText().trim();
        if (validatedate.equalsIgnoreCase(value)) {
            logWithTimestamp("Quick date is already selected");
        } else {
            page.locator(we).click();
            waitUntilAngularReady();
            page.waitForTimeout(200);
            findState(value).click();
        }
    }

    public void click(Locator locator) {
        locator.scrollIntoViewIfNeeded();
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        locator.click();
    }

    public void moveToElement(Locator locator) {
        locator.hover();
    }

    public void clickElementJS(Locator locator) {
        locator.evaluate("element => element.click()");
    }

    public void clickLink(String linkText) {
        page.locator("a", new Page.LocatorOptions().setHasText(linkText)).click();
    }

    public void enterText(Locator locator, String text) {
        locator.scrollIntoViewIfNeeded();
        locator.fill("");
        locator.click();
        locator.fill(text); // Use fill instead of deprecated type
    }
    // ...existing code...

    // Removed Selenium-based enterText(WebElement, String) method

    public void selectRowByIndex(String tableID, int row) {
        Locator baseTable = page.locator(tableID);
        Locator tableRows = baseTable.locator("mat-row");
        Locator icon = tableRows.nth(row).locator(".mdc-checkbox");
        click(icon);
        logInfo(row + " Row selected");
    }

    public void selectRowByIndex2(String tableID, int row) {
        Locator baseTable = page.locator(tableID);
        Locator tableRows = baseTable.locator("mat-row");
        Locator icon = page.locator("(//div[@class='mat-mdc-checkbox-touch-target'])[" + (row + 1) + "]");
        click(icon);
        logInfo(row + " Row selected");
    }

    private static final String tableName = "//mat-table";

    public void selectRowSearch(int rowNum) {
        selectRowByIndex2(tableName, rowNum);
        logInfo(rowNum + " Row selected");
    }


    // Removed Selenium-based scrollToElement(WebElement) method

    /*
     * Scrolls to top of the page
     */

    public void scrollToTop() {
        page.evaluate("window.scrollTo(0,0);");
        logInfo("Scrolled to the top of the page");
    }

    public void scrollToDown() {
        page.evaluate("window.scrollTo(0,400);");
        logInfo("Scrolled to the bottom of 1st page");
    }


    public void scrollToValue(int value) {
        String script = "window.scrollTo(0," + value + ");";
        page.evaluate(script);
    }

    public void waitUntilAngularReady() {
        // Playwright does not support AngularJS hooks directly; fallback to wait for network idle
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    // Removed Selenium-based Angular wait methods


    // Removed Selenium-based Angular 15 wait method


    public void sleep(long milis) {
        page.waitForTimeout(milis);
    }

    // Removed Selenium-based Angular 18 wait method

    public void waitForElementToAppearCustomEle(String selector, int timeout) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(timeout * 1000));
    }

    public void waitForElementToAppearEle(Locator locator, int timeoutInSeconds) {
        locator.waitFor(new Locator.WaitForOptions().setTimeout(timeoutInSeconds * 1000).setState(WaitForSelectorState.VISIBLE));
        logWithTimestamp("info - Locator is visible: " + locator);
    }

    public void waitForElementToDisappearEle(Locator locator, int timeoutInSeconds) {
        locator.waitFor(new Locator.WaitForOptions().setTimeout(timeoutInSeconds * 1000).setState(WaitForSelectorState.HIDDEN));
        logWithTimestamp("info - Locator is hidden: " + locator);
    }


    public List<String> readTabledb(String tableID) {
        List<String> cells = new ArrayList<>();
        Locator table = page.locator(tableID);
        Locator tablecols = table.locator("td");
        int count = tablecols.count();
        for (int i = 0; i < count; i++) {
            String text = tablecols.nth(i).innerText().toLowerCase();
            if (text != null && !text.isEmpty()) {
                cells.add(text);
            }
        }
        logWithTimestamp("info - List: " + cells);

        ArrayList<String> wordsList = new ArrayList<>();

        // Iterate through each sentence and split into words
        for (String sentence : cells) {
            String[] words = sentence.split("\\s+"); // Split by whitespace
            wordsList.addAll(Arrays.asList(words)); // Add words to list
        }
        // Print out all individual words
        for (String word : wordsList) {
            logWithTimestamp(word);
        }

        return wordsList;
    }

    public List<String> readtableSearchPage(int rowNum) {
        // Playwright: Get the nth row (rowNum is 1-based)
        Locator row = page.locator("//mat-table/mat-row").nth(rowNum - 1);
        Locator cellsLocator = row.locator("mat-cell");
        List<String> cells = cellsLocator.allInnerTexts();
        // Remove null or empty strings
        cells.removeAll(Arrays.asList(null, ""));
        logWithTimestamp("Data: " + cells);

        ArrayList<String> wordsList = new ArrayList<>();

        // Iterate through each sentence and split into words
        for (String sentence : cells) {
            String[] words = sentence.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase().split("\\s+");
            wordsList.addAll(Arrays.asList(words)); // Add words to list
        }
        // Print out all individual words
        for (String word : wordsList) {
            logWithTimestamp("Data: " + word);
        }
        return wordsList;
    }

    public String readtableSearchPageCell(String table, int rowNum, int ColNum) {
        // Build the XPath for the cell
        String xpath = table + "//mat-row[" + rowNum + "]//mat-cell[" + ColNum + "]";
        Locator cellLocator = page.locator(xpath);
        String cell = cellLocator.innerText();
        String cellFormatted = cell.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase().trim();
        logInfo("Search page cell value: " + cellFormatted);
        logWithTimestamp("Search page cell value: " + cellFormatted);
        return cellFormatted;
    }

    public String readtableSearchPageCellNoFormatting(String table, int rowNum, int ColNum) {
        String xpath = table + "//mat-row[" + rowNum + "]//mat-cell[" + ColNum + "]";
        Locator cellLocator = page.locator(xpath);
        String cell = cellLocator.innerText();
        logInfo("Search page cell value: " + cell);
        return cell;
    }

    public List<String> readtablePSpage(int rowNum) {
        String xpath = "(//div[contains(@class,'column')]//app-search-searchable//*[@title='Address'])[" + rowNum + "]";
        Locator columns = page.locator(xpath);
        String cell = columns.innerText().toLowerCase();
        logInfo("Address displayed: " + cell);

        ArrayList<String> wordsList = new ArrayList<>();
        String[] words = cell.split("\\s+"); // Split by whitespace
        wordsList.addAll(Arrays.asList(words)); // Add words to list

        // Print out all individual words
        for (String word : wordsList) {
            logWithTimestamp(word);
        }
        return wordsList;
    }


    public String readCellValue(String tableID, int rowNum, int colNum) {
        Locator table = page.locator(tableID);
        Locator row = table.locator("tr").nth(rowNum);
        Locator cell = row.locator("td").nth(colNum);
        String cellText = cell.innerText();
        logWithTimestamp("info - Value: " + cellText);
        return cellText;
    }


    public List<String> readTable2(String tableID, int rowNum) {
        Locator table = page.locator(tableID);
        Locator cellsLocator = table.locator("mat-cell");
        List<String> cells = cellsLocator.allInnerTexts();
        cells.removeAll(Arrays.asList(null, ""));
        logWithTimestamp("info - List: " + cells);
        return cells;
    }


    public List<String> readTable3(String section) {
        String xpath = "//mat-panel-title[contains(text,'" + section + "')]" +
                "//parent::span/parent::mat-expansion-panel-header/following-sibling::div//span[@class='mdc-list-item__content']//div[contains(@class,'item-details flex-row')]";
        Locator assetLocator = page.locator(xpath);
        String searchAsset = assetLocator.innerText();
        String searchAssetFormatted = searchAsset.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();

        List<String> wordsList = new ArrayList<>();
        String[] words = searchAssetFormatted.split("\\s+"); // Split by whitespace
        wordsList.addAll(Arrays.asList(words)); // Add words to list
        logInfo("Value: " + wordsList);
        return wordsList;
    }


    public int isThisDateWithinRange(String dateToValidate, String range) {
        int diff = 0;

        String[] date = dateToValidate.split("/");

        LocalDate localDate = LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[0]),
                Integer.parseInt(date[1]));
        LocalDate currentDate = LocalDate.now();

        Period age = Period.between(localDate, currentDate);
        if (range.contains("months")) {
            diff = age.getMonths();

        } else if (range.contains("days")) {
            diff = age.getDays();
        }
        return diff;
    }


    public boolean isThisDateWithinRangeCDB(String dateToValidate, String range, String Days, int count) {
        int diff = 0;
        int DCount = count;

        String[] date = dateToValidate.split("/");

        LocalDate localDate = LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[0]),
                Integer.parseInt(date[1]));
        LocalDate currentDate = LocalDate.now();

        Period age = Period.between(localDate, currentDate);
        if (range.contains("months")) {
            diff = age.getMonths();

        } else if (range.contains("days")) {
            diff = age.getDays();
        }

        if (diff <= DCount) {
            logPass("Results are displayed within range -" + Days);
            return true;

        } else {
            logFail("Result is not displayed within range - " + Days);
            return false;
        }
    }


    // Return Current date in the given format
    public String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/YYYY");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }


    // ...existing code...

    public List<Integer> format3tabsDataToIntArray(List<String> stringList) {
        //format the array
        List<String> numericStrings = stringList.stream()
                .map(str -> str.replaceAll("[^\\d.]", ""))
                .collect(Collectors.toList());

        //Convert String list to integer list
        List<Integer> integerList = new ArrayList<>();
        for (String str : numericStrings) {
            integerList.add(Integer.parseInt(str));
        }
        return integerList;
    }

    public boolean compare2intLists(List<Integer> list1, List<Integer> list2) {
        if (IntStream.range(0, list1.size())
                .allMatch(i -> list1.get(i).equals(list2.get(i)))) {
            logWithTimestamp("Lists are equal, list1: " + list1 + " " + "list2: " + list2);
            return true;
        } else {
            logWithTimestamp("Lists are not equal, list1: " + list1 + " " + "list2: " + list2);
            return false;
        }
    }

    //get string value
    public String getStringValue(Locator locator) {
        String value = locator.innerText();
        logWithTimestamp("Value: " + value);
        return value;
    }

    //Convert a string into integer
    public int convertStringToInt(String str) {
        String numericString = str.replaceAll("[^\\d.]", "").trim();
        return Integer.parseInt(numericString);
    }


    //compare 2 integers
    public boolean compare2integers(int int1, int int2) {
        if (int1 > int2) {
            logWithTimestamp("Data - int1  " + int1 + " > " + "int2: " + int2);
            return true;
        } else {
            logWithTimestamp("Data - int1: " + int1 + " int2: " + int2);
            return false;
        }
    }

    //validate dates sorted in order
    public boolean validateDatesSortedDescendingOrder(List<Locator> incidentsDateList) {
        List<String> dates = incidentsDateList.stream().map(Locator::innerText).filter(Objects::nonNull).collect(Collectors.toList());
        logWithTimestamp("Dates: " + dates);
        boolean isSorted = true;
        for (int i = 1; i < dates.size(); i++) {
            incidentsDateList.get(i).scrollIntoViewIfNeeded();
            if (dates.get(i).compareTo(dates.get(i - 1)) < 0) {
                isSorted = false;
                break;
            }
        }
        return isSorted;
    }

    protected boolean areDatesWithinRangeArray(String startDate, String endDate, List<String> dates) {

        String dateFormatPattern = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatPattern);

        try {
            Date startDateParsd = sdf.parse(startDate);
            Date endDateParsd = sdf.parse(endDate);

            for (String dateString : dates) {
                Date dateToValidate = sdf.parse(dateString);
                boolean isWithinRange = dateToValidate.after(startDateParsd) && dateToValidate.before(endDateParsd);
                logWithTimestamp(dateString + " is within range: " + isWithinRange);
            }
        } catch (ParseException e) {
            e.fillInStackTrace();
        }
        return true;
    }


    public String convertArrayToString(List<String> keyWordsList) {
        //        Convert List String to one string
        return keyWordsList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
//        return String.join(", ", keyWordsList);
    }

    public int getIntCount(List<Locator> listEle) {
        int count = listEle.size();
        logWithTimestamp("Elements count: " + count);
        return count;
    }

    //convert List String into List integer
    public List<Integer> convertStringListToIntList(List<String> stringList) {
        List<Integer> integerList = new ArrayList<>();
        for (String str : stringList) {
            integerList.add(Integer.parseInt(str));
        }
        logWithTimestamp("info - Integer list: " + integerList);
        return integerList;
    }

    public String getCurrentdateAndTime() {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

        // Get the date today using the Date object
        Date today = new Date();

        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        return df.format(today);
    }

    public boolean goThroughArrayValidateifContainsString(List<String> list, String value) {
        boolean isContains = list.stream().anyMatch(n -> n.contains(value));
        logWithTimestamp("Is value present in the list: " + isContains);
        if (isContains) {
            logPass("Value is present in the list: " + value);
        } else {
            logFail("Value is not present in the list: " + value);
        }
        return isContains;
    }


    public String formattDateUA(String originalDate) throws ParseException {

        // Create a SimpleDateFormat object for the original date format
        SimpleDateFormat originalFormat = new SimpleDateFormat("MMM dd yyyy hh:mm:ss a");

        // Parse the original date string into a Date object
        Date date = originalFormat.parse(originalDate);

        // Create a SimpleDateFormat object for the desired numeric date format
        SimpleDateFormat desiredFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

        // Format the Date object into the desired numeric date format
        String numericDate = desiredFormat.format(date);

        // Print the numeric date
        // Outputs: 07/05/2024 hh:mm:ss
        logWithTimestamp(numericDate);

        return numericDate;
    }

    public static String removeNonAlphanumeric(String str) {
        str = str.replaceAll("[^a-zA-Z0-9]", "");
        return str;
    }

    public static boolean stringContainsItemFromList(String inputStr, List<String> items) {
        return items.stream().anyMatch(inputStr::contains);
    }

    public boolean isBackgroundColorCorrect(String color, Locator locator) {
        String bgColor = locator.evaluate("el => getComputedStyle(el).backgroundColor").toString();
        if (bgColor.equals(color)) {
            logInfo("Background Color is matching expected: " + color);
            return true;
        } else {
            logInfo("Background Color is not matching expected: " + color);
            return false;
        }
    }

    public boolean isElementColorCorrect(String color, Locator locator) {
        String elColor = locator.evaluate("el => getComputedStyle(el).color").toString();
        if (elColor.equals(color)) {
            logWithTimestamp("info - Element Color is matching expected");
            return true;
        } else {
            logWithTimestamp("info - Element Color is not matching expected");
            return false;
        }
    }

    public boolean compare2StringLists(List<String> list1, List<String> list2) {
        if (list1.equals(list2)) {
            logWithTimestamp("Lists are equal, list1: " + list1 + " " + "list2: " + list2);
            return true;
        } else {
            logWithTimestamp("Lists are not equal, list1: " + list1 + " " + "list2: " + list2);
            return false;
        }
    }

    public boolean isListSortedAZ(List<String> listWords) {
        List<String> sortedList = new ArrayList<>(listWords);
        Collections.sort(sortedList);
        if (listWords.equals(sortedList)) {
            logWithTimestamp("List is sorted alphabetically A-Z");
            return true;
        } else {
            logWithTimestamp("List is not sorted alphabetically A-Z");
            return false;
        }
    }

    public boolean isListSortedZA(List<String> listWords) {
        List<String> sortedList = new ArrayList<>(listWords);
        sortedList.sort(Collections.reverseOrder());
        if (listWords.equals(sortedList)) {
            logWithTimestamp("List is sorted alphabetically Z-A");
            return true;
        } else {
            logWithTimestamp("List is not sorted alphabetically Z-A");
            return false;
        }
    }

    public void moveToElementJS(Locator locator) {
        locator.evaluate("el => el.scrollIntoView({behavior: 'smooth', block: 'center'})");
    }

  /*  public static void logWithTimestamp(String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String timestamp = sdf.format(new Date());
        System.out.println(timestamp + " - " + message);
    }*/

    public static void logWithTimestampInt(int message) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String timestamp = sdf.format(new Date());
        System.out.println(timestamp + " - " + message);
    }



    public String removeNonAlphabetical(String input) {
        return input.replaceAll("[^a-zA-Z\\s]", "").trim();
    }

    public boolean validateIfWordIsPresentInList(List<String> list, String word) {
        String lowerCaseWord = word.toLowerCase();
        boolean isPresent = list.stream()
                .filter(Objects::nonNull)
                .anyMatch(n -> n.toLowerCase().contains(lowerCaseWord));
        return isPresent;
    }

    public boolean validateIf2WordArePresentInList(List<String> list, String word1, String word2) {
        String lowerCaseWord1 = word1.toLowerCase();
        String lowerCaseWord2 = word2.toLowerCase();
        boolean isPresent = list.stream()
                .filter(Objects::nonNull)
                .anyMatch(n -> n.toLowerCase().contains(lowerCaseWord1) || n.toLowerCase().contains(lowerCaseWord2));
        return isPresent;
    }

    public int ExtractLastInteger (String input, int i) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        int lastNumber = -i;
        while (matcher.find()) {
            lastNumber = Integer.parseInt(matcher.group());
        }
        return lastNumber;
    }
    //=====================================================================================
    //=====================================================================================

    private static final ThreadLocal<String> currentStep = new ThreadLocal<>();

    // ✅ Your original logger (kept + enhanced)
    public static void logWithTimestamp(String message) {

        String timeStamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
        String logMessage = "[" + timeStamp + "] " + message;

        // Console log
        System.out.println(logMessage);

        // ✅ Extent Report log (AUTO PUSH)
        try {
            playwrightPractice.utilities.ExtentReport.logInfo(logMessage);
        } catch (Exception ignored) {
            // Avoid breaking execution if report not initialized
        }
    }

    // ✅ AUTO STEP LOGGER
    public static void step(String message) {
        currentStep.set(message);
        logWithTimestamp("STEP ➤ " + message);
    }

    public static void logInfo(String message) {
        logWithTimestamp("INFO ➤ " + message);
    }

    public static void logPass(String message) {
        logWithTimestamp("PASS ➤ " + message);

        if (ExtentReport.getTest() != null) {
            ExtentReport.getTest().pass(message);
        }
    }

    public static void logFail(String message) {
        logWithTimestamp("FAIL ➤ " + message);

        if (ExtentReport.getTest() != null) {
            ExtentReport.getTest().fail(message);
        }
    }

    public static void logWarn(String message) {
        logWithTimestamp("WARN ➤ " + message);

        if (ExtentReport.getTest() != null) {
            ExtentReport.getTest().warning(message);
        }
    }

    public static String getCurrentStep() {
        return currentStep.get();
    }




}
