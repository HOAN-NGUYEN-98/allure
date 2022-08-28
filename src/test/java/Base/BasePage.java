package Base;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.JavascriptExecutor;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.Reporter;
import utils.listeners.WebAppDriverManager;

public class BasePage {
    public Logger logger = Logger.getLogger(BasePage.class);
    public WebDriverWait wait;
    WebElement element;
    WebDriverWait waitExplicit;
    By byLocator;
    Actions action;
    List<WebElement> elements;
    JavascriptExecutor javascriptExecutor;
    public WebDriver driver = WebAppDriverManager.getDriver();

    public BasePage() {
        PageFactory.initElements(this.driver, this);
    }

    public void clickToElement(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.element = this.waitForElementVisible(locator);
        this.element.click();
    }

    public void clickToElement(WebElement element) {
        element = this.waitForElementVisible(element);
        element.click();
    }

    public void clearTextElement(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.element = this.waitForElementVisible(locator);
        this.element.clear();
    }

    public void clearTextElement(WebElement element) {
        element = this.waitForElementVisible(element);
        element.clear();
    }

    public void sendKeyToElement(WebElement element, String value) {
        element = this.waitForElementVisible(element);
        element.sendKeys(new CharSequence[]{value});
    }

    public void sendKeyToElement(String locator, String sendKeyValue, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.element = this.waitForElementVisible(locator);
        this.element.sendKeys(new CharSequence[]{sendKeyValue});
    }

    public void selectItemInDropdown(String locator, String expectedValueInDropdown, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.element = this.waitForElementVisible(locator);
        Select select = new Select(this.element);
        select.selectByVisibleText(expectedValueInDropdown);
    }

    public void selectItemInDropdown(WebElement element, String expectedValueInDropdown) {
        element = this.waitForElementVisible(element);
        Select select = new Select(element);
        select.selectByVisibleText(expectedValueInDropdown);
    }

    public List<WebElement> getListElementFromDrd(WebElement locator) {
        this.element = this.waitForElementVisible(locator);
        Select select = new Select(this.element);
        return select.getOptions();
    }

    public String getBrowserName() {
        return ((RemoteWebDriver) this.driver).getCapabilities().getBrowserName();
    }

    public void openAnyUrl(String url) {
        driver.get(url);
    }

    public String getCurrentPageUrl() {
        return this.driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return this.driver.getTitle();
    }

    public String getPageSourceCode() {
        return this.driver.getPageSource();
    }

    public void backToPreviousPage() {
        this.driver.navigate().back();
    }

    public void refreshCurrentPage() {
        this.driver.navigate().refresh();
    }

    public void forwardToNextPage() {
        this.driver.navigate().forward();
    }

    public void acceptAlert() {
        this.driver.switchTo().alert().accept();
    }

    public void cancelAlert() {
        this.driver.switchTo().alert().dismiss();
    }

    public String getTextAlert() {
        return this.driver.switchTo().alert().getText();
    }

    public void sendKeyToAlert(String value) {
        this.driver.switchTo().alert().sendKeys(value);
    }


    public String getFirstSelectedItemInDropdown(String locator) {
        this.element = this.driver.findElement(By.xpath(locator));
        Select select = new Select(this.element);
        return select.getFirstSelectedOption().getText();
    }

    public String getFirstSelectedItemInDropdown(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.element = this.driver.findElement(By.xpath(locator));
        Select select = new Select(this.element);
        return select.getFirstSelectedOption().getText();
    }

    public List<String> getAllSelectOption(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.element = this.driver.findElement(By.xpath(locator));
        Select select = new Select(this.element);
        List<String> listSelectText = new ArrayList();
        select.getOptions().forEach((webElement) -> {
            listSelectText.add(webElement.getText());
        });
        return listSelectText;
    }

    public void selectItemInCustomDropdown(String parentXpath, String allItemXpath, String expectedItem) throws Exception {
        this.javascriptExecutor = (JavascriptExecutor) this.driver;
        WebElement parentDropdown = this.driver.findElement(By.xpath(parentXpath));
        this.javascriptExecutor.executeScript("arguments[0].click();", new Object[]{parentDropdown});
        this.waitExplicit = new WebDriverWait(this.driver, 30L);
        this.waitExplicit.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(allItemXpath)));
        List<WebElement> all_Item = this.driver.findElements(By.xpath(allItemXpath));
        Iterator var6 = all_Item.iterator();

        while (var6.hasNext()) {
            WebElement childElement = (WebElement) var6.next();
            if (childElement.getText().equals(expectedItem)) {
                this.javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", new Object[]{childElement});
                Thread.sleep(1000L);
                if (childElement.isDisplayed()) {
                    childElement.click();
                } else {
                    this.javascriptExecutor.executeScript("arguments[0].click();", new Object[]{childElement});
                }

                Thread.sleep(1000L);
                break;
            }
        }

    }

    public List<WebElement> getAllElement(String locator) {
        this.waitExplicit = new WebDriverWait(this.driver, (long) WebAppDriverManager.getLONG_TIMEOUT());
        List<WebElement> elements = new ArrayList();
        this.byLocator = By.xpath(locator);

        try {
            elements = (List) this.waitExplicit.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(this.byLocator));
        } catch (Exception var4) {
            this.logger.warn("=========================== Element not visible==============================");
            this.logger.warn(var4.getMessage());
            System.err.println("================================== Element not visible===================================");
            System.err.println(var4.getMessage() + "\n");
        }

        return (List) elements;
    }

    public String getAttributeValue(String locator, String attributeName, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.element = this.driver.findElement(By.xpath(locator));
        return this.element.getAttribute(attributeName);
    }

    public String getAttributeValue(WebElement element, String attributeName) {
        element = this.waitForElementVisible(element);
        return element.getAttribute(attributeName);
    }

    public String getAttributeValue(String attributeName) {
        return this.element.getAttribute(attributeName);
    }

    public boolean isAttributePresent(String locator, String attributeName, String attributeValue, String... values) {
        boolean temp = true;
        String actualValue = this.getAttributeValue(locator, attributeName, values);
        return actualValue.equals(attributeValue);
    }

    public String getTextElement(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.element = this.waitForElementVisible(locator);
        return this.element.getText();
    }

    public String getTextElement(WebElement element) {
        element = this.waitForElementVisible(element);
        return element.getText();
    }

    public String getParentWindowID() {
        return this.driver.getWindowHandle();
    }

    public int countElementNumber(String locator) {
        this.overrideGlobalTimeout(WebAppDriverManager.getSHORT_TIMEOUT());
        this.elements = this.driver.findElements(By.xpath(locator));
        this.resetTimeOut();
        return this.elements.size();
    }

    public void checkToCheckBoxOrRadioButton(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.element = this.waitForElementVisible(locator);
        if (!this.element.isSelected()) {
            this.element.click();
        }

    }

    public void checkToCheckBoxOrRadioButton(WebElement element) {
        element = this.waitForElementVisible(element);
        if (!element.isSelected()) {
            element.click();
        }

    }

    public void unCheckToCheckBox(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.element = this.waitForElementVisible(locator);
        if (this.element.isSelected()) {
            this.element.click();
        }

    }

    public void unCheckToCheckBox(WebElement element) {
        element = this.waitForElementVisible(element);
        if (element.isSelected()) {
            element.click();
        }

    }

    public boolean isControlDisplayed(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.element = this.driver.findElement(By.xpath(locator));
        return this.element.isDisplayed();
    }

    public boolean isControlDisplayed(WebElement element) {
        element = this.waitForElementVisible(element);
        return element.isDisplayed();
    }

    public boolean isControlUnDisplayed(String locator) {
        this.overrideGlobalTimeout(WebAppDriverManager.getSHORT_TIMEOUT());
        List<WebElement> elements = this.driver.findElements(By.xpath(locator));
        if (elements.isEmpty()) {
            this.overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
            return true;
        } else if (!((WebElement) elements.get(0)).isDisplayed()) {
            this.overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
            return true;
        } else {
            this.overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
            return false;
        }
    }

    public boolean isControlUnDisplayed(String locator, String... values) {
        this.overrideGlobalTimeout(WebAppDriverManager.getSHORT_TIMEOUT());
        locator = String.format(locator, (Object[]) values);
        List<WebElement> elements = this.driver.findElements(By.xpath(locator));
        if (elements.isEmpty()) {
            this.overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
            return true;
        } else if (!((WebElement) elements.get(0)).isDisplayed()) {
            this.overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
            return true;
        } else {
            this.overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
            return false;
        }
    }

    public boolean isControlEnabled(WebElement element) {
        element = this.waitForElementVisible(element);
        return element.isEnabled();
    }

    public boolean isControlEnabled(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.element = this.waitForElementVisible(locator);
        return this.element.isEnabled();
    }

    public boolean isControlSelected(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.element = this.waitForElementVisible(locator);
        return this.element.isSelected();
    }

    public boolean isControlSelected(WebElement element) {
        element = this.waitForElementVisible(element);
        return element.isSelected();
    }

    public void switchToChildWindowByID(String parentID) {
        Set<String> allWindows = this.driver.getWindowHandles();
        Iterator var3 = allWindows.iterator();

        while (var3.hasNext()) {
            String runWindow = (String) var3.next();
            if (!runWindow.equals(parentID)) {
                this.driver.switchTo().window(runWindow);
                break;
            }
        }
    }

    public void switchToChildWindowByTitle(String expectedTitle) {
        Set<String> allWindows = this.driver.getWindowHandles();
        Iterator var3 = allWindows.iterator();

        while (var3.hasNext()) {
            String runWindow = (String) var3.next();
            this.driver.switchTo().window(runWindow);
            String currentWindowTitle = this.driver.getTitle();
            if (currentWindowTitle.equals(expectedTitle)) {
                break;
            }
        }
    }

    public boolean closeAllExceptParentWindow(String parentWindows) {
        Set<String> allWindows = this.driver.getWindowHandles();
        Iterator var3 = allWindows.iterator();

        while (var3.hasNext()) {
            String runWindow = (String) var3.next();
            if (!runWindow.equals(parentWindows)) {
                this.driver.switchTo().window(runWindow);
                this.driver.close();
            }
        }
        this.driver.switchTo().window(parentWindows);
        return this.driver.getWindowHandles().size() == 1;
    }

    public void switchToFrame(String locator) {
        this.element = this.driver.findElement(By.xpath(locator));
        this.driver.switchTo().frame(this.element);
    }

    public void backToTopWindow() {
        this.driver.switchTo().defaultContent();
    }

    public void hoverMouseToElement(String locator) {
        this.element = this.driver.findElement(By.xpath(locator));
        this.action = new Actions(this.driver);
        this.action.moveToElement(this.element).perform();
    }

    public void clickToElementByAction(WebElement element) {
        this.javascriptExecutor.executeScript("arguments[0].scrollIntoViewIfNeeded(true);", new Object[]{element});
        this.action = new Actions(this.driver);
        this.waitForElementVisible(element);
        this.action.click(element).perform();
    }

    public void hoverMouseToElement(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.element = this.driver.findElement(By.xpath(locator));
        this.action = new Actions(this.driver);
        this.action.moveToElement(this.element).perform();
    }

    public void doubleClickToElement(String locator) {
        this.element = this.driver.findElement(By.xpath(locator));
        this.action = new Actions(this.driver);
        this.action.doubleClick(this.element);
    }

    public void rightClickToElement(String locator) {
        this.element = this.driver.findElement(By.xpath(locator));
        this.action = new Actions(this.driver);
        this.action.contextClick(this.element);
    }

    public void dragAndDrop(String sourceLocator, String targetLocator) {
        WebElement sourceElement = this.driver.findElement(By.xpath(sourceLocator));
        WebElement targetElement = this.driver.findElement(By.xpath(targetLocator));
        this.action = new Actions(this.driver);
        this.action.dragAndDrop(sourceElement, targetElement).build().perform();
    }

    public void dragAndDrop(WebElement sourceElement, WebElement targetElement) {
        this.action = new Actions(this.driver);
        this.action.dragAndDrop(sourceElement, targetElement).build().perform();
    }

    public void sendKeyBoardToElement(WebElement element, Keys key) {
        this.action = new Actions(this.driver);
        this.action.sendKeys(element, new CharSequence[]{key}).perform();
    }

    public void sendKeyBoardToElement(String locator, Keys key) {
        this.element = this.driver.findElement(By.xpath(locator));
        this.action = new Actions(this.driver);
        this.action.sendKeys(this.element, new CharSequence[]{key}).perform();
    }

    public void sendKeyBoardToElement(String locator, Keys key, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.element = this.driver.findElement(By.xpath(locator));
        this.action = new Actions(this.driver);
        this.action.sendKeys(this.element, new CharSequence[]{key}).perform();
    }

    public void sleepTimeInSecond(int second) {
        try {
            Thread.sleep((long) second * 1000L);
        } catch (InterruptedException var3) {
            var3.printStackTrace();
        }

    }

    public void sleepTimeInMilSecond(int milSecond) {
        try {
            Thread.sleep((long) milSecond);
        } catch (InterruptedException var3) {
            var3.printStackTrace();
        }

    }

    public void highlightElement(String locator) {
    }

    public Object executeForBrowser(String javascript) {
        this.javascriptExecutor = (JavascriptExecutor) this.driver;
        return this.javascriptExecutor.executeScript(javascript, new Object[0]);
    }

    public void clickToElementByJavascript(String locator) {
        this.javascriptExecutor = (JavascriptExecutor) this.driver;
        this.element = this.driver.findElement(By.xpath(locator));
        this.javascriptExecutor.executeScript("arguments[0].click();", new Object[]{this.element});
    }

    public void clickToElementByJavascript(WebElement ele) {
        this.javascriptExecutor = (JavascriptExecutor) this.driver;
        this.javascriptExecutor.executeScript("arguments[0].click();", new Object[]{ele});
    }

    public void clickToElementByJavascript(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.javascriptExecutor = (JavascriptExecutor) this.driver;
        this.element = this.driver.findElement(By.xpath(locator));
        this.javascriptExecutor.executeScript("arguments[0].click();", new Object[]{this.element});
    }

    public void sendKeyToElementByJavascript(String locator, String value) {
        this.javascriptExecutor = (JavascriptExecutor) this.driver;
        this.element = this.driver.findElement(By.xpath(locator));
        this.javascriptExecutor.executeScript("arguments[0].setAttribute('value', '" + value + "')", new Object[]{this.element});
    }

    public void removeAttributeInDOM(String locator, String attribute) {
        this.javascriptExecutor = (JavascriptExecutor) this.driver;
        this.element = this.driver.findElement(By.xpath(locator));
        this.javascriptExecutor.executeScript("arguments[0].removeAttribute('" + attribute + "');", new Object[]{this.element});
    }

    public void removeAttributeInDOM(String locator, String attribute, String... values) {
        this.javascriptExecutor = (JavascriptExecutor) this.driver;
        locator = String.format(locator, (Object[]) values);
        this.element = this.driver.findElement(By.xpath(locator));
        this.javascriptExecutor.executeScript("arguments[0].removeAttribute('" + attribute + "');", new Object[]{this.element});
    }

    public void setAttributeInDOM(String locator, String attribute, String value) {
        this.javascriptExecutor = (JavascriptExecutor) this.driver;
        this.element = this.driver.findElement(By.xpath(locator));
        this.javascriptExecutor.executeScript("arguments[0].setAttribute('" + attribute + "', '" + value + "');", new Object[]{this.element});
    }

    public void scrollToCentralOfPage() {
        this.javascriptExecutor = (JavascriptExecutor) this.driver;
        this.javascriptExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight/2)", new Object[0]);
    }

    public void scrollToBottomPage() {
        this.javascriptExecutor = (JavascriptExecutor) this.driver;
        this.javascriptExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)", new Object[0]);
    }

    public void navigateToUrlByJavascript(String url) {
        this.javascriptExecutor = (JavascriptExecutor) this.driver;
        this.javascriptExecutor.executeScript("window.location = '" + url + "'", new Object[0]);
    }

    public void uploadSingleFileBySendKeyToElement(String filePath, String openButtonLocator, String uploadButtonLocator) {
        this.driver.findElement(By.xpath(openButtonLocator)).sendKeys(new CharSequence[]{filePath});
        this.driver.findElement(By.xpath(uploadButtonLocator)).click();
    }

    public void uploadMultiFilesBySendKeyToElement(String[] allFilesPaths, String openButtonLocator, String uploadButtonLocator) {
        String[] var4 = allFilesPaths;
        int var5 = allFilesPaths.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            String file = var4[var6];
            this.driver.findElement(By.xpath(openButtonLocator)).sendKeys(new CharSequence[]{file});
        }

        List<WebElement> allStartBtn = this.driver.findElements(By.xpath(uploadButtonLocator));
        Iterator var9 = allStartBtn.iterator();

        while (var9.hasNext()) {
            WebElement startButton = (WebElement) var9.next();
            startButton.click();
        }

    }

    public void waitForElementPresence(String locator) {
        this.waitExplicit = new WebDriverWait(this.driver, (long) WebAppDriverManager.getLONG_TIMEOUT());
        this.byLocator = By.xpath(locator);
        this.waitExplicit.until(ExpectedConditions.presenceOfElementLocated(this.byLocator));
    }

    public void waitForAllElementsPresence(String locator) {
        this.waitExplicit = new WebDriverWait(this.driver, (long) WebAppDriverManager.getLONG_TIMEOUT());
        this.byLocator = By.xpath(locator);
        this.waitExplicit.until(ExpectedConditions.presenceOfAllElementsLocatedBy(this.byLocator));
    }

    public WebElement waitForElementVisible(String locator, String... values) {
        this.waitExplicit = new WebDriverWait(this.driver, (long) WebAppDriverManager.getLONG_TIMEOUT());
        locator = String.format(locator, (Object[]) values);
        this.byLocator = By.xpath(locator);

        try {
            return (WebElement) this.waitExplicit.until(ExpectedConditions.visibilityOfElementLocated(this.byLocator));
        } catch (Exception var4) {
            this.logger.warn("=========================== Element not visible==============================");
            this.logger.warn(var4.getMessage());
            System.err.println("================================== Element not visible===================================");
            System.err.println(var4.getMessage() + "\n");
            return null;
        }
    }

    public WebElement waitForElementVisible(WebElement element) {
        this.waitExplicit = new WebDriverWait(this.driver, (long) WebAppDriverManager.getLONG_TIMEOUT());

        try {
            element = (WebElement) this.waitExplicit.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception var3) {
            this.logger.warn("=========================== Element not visible==============================");
            this.logger.warn(var3.getMessage());
            System.err.println("================================== Element not visible===================================");
            System.err.println(var3.getMessage() + "\n");
        }

        return element;
    }

    public void waitForElementClickable(String locator) {
        this.waitExplicit = new WebDriverWait(this.driver, (long) WebAppDriverManager.getLONG_TIMEOUT());
        this.byLocator = By.xpath(locator);
        this.waitExplicit.until(ExpectedConditions.elementToBeClickable(this.byLocator));
    }

    public WebElement waitForElementClickable(WebElement element) {
        this.waitExplicit = new WebDriverWait(this.driver, (long) WebAppDriverManager.getLONG_TIMEOUT());
        return (WebElement) this.waitExplicit.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForElementClickable(String locator, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.waitExplicit = new WebDriverWait(this.driver, (long) WebAppDriverManager.getLONG_TIMEOUT());
        this.byLocator = By.xpath(locator);
        this.waitExplicit.until(ExpectedConditions.elementToBeClickable(this.byLocator));
    }

    public void waitForTextToBePresentInElement(WebElement e, String expectedText) {
        this.waitExplicit = new WebDriverWait(this.driver, (long) WebAppDriverManager.getLONG_TIMEOUT());
        this.waitExplicit.until(ExpectedConditions.textToBePresentInElement(e, expectedText));
    }

    public void waitForTextToBePresentInElement(String locator, String expectedText, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.waitExplicit = new WebDriverWait(this.driver, (long) WebAppDriverManager.getLONG_TIMEOUT());
        this.byLocator = By.xpath(locator);
        this.waitExplicit.until(ExpectedConditions.textToBePresentInElementLocated(this.byLocator, expectedText));
    }

    public void waitForElementInvisible(WebElement element) {
        this.overrideGlobalTimeout(WebAppDriverManager.getSHORT_TIMEOUT());
        this.waitExplicit = new WebDriverWait(this.driver, (long) WebAppDriverManager.getSHORT_TIMEOUT());

        try {
            this.waitExplicit.until(ExpectedConditions.invisibilityOf(element));
        } catch (Exception var3) {
            this.logger.warn("=========================== Element not invisible==============================");
            this.logger.warn(var3.getMessage());
            System.err.println("================================== Element not invisible===================================");
            System.err.println(var3.getMessage() + "\n");
        }

        this.overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
    }

    public void waitForElementInvisible(String locator, String... values) {
        this.overrideGlobalTimeout(WebAppDriverManager.getSHORT_TIMEOUT());
        this.waitExplicit = new WebDriverWait(this.driver, (long) WebAppDriverManager.getSHORT_TIMEOUT());
        locator = String.format(locator, (Object[]) values);
        this.byLocator = By.xpath(locator);

        try {
            this.waitExplicit.until(ExpectedConditions.invisibilityOfElementLocated(this.byLocator));
        } catch (Exception var4) {
            this.logger.warn("=========================== Element not invisible==============================");
            this.logger.warn(var4.getMessage());
            System.err.println("================================== Element not invisible===================================");
            System.err.println(var4.getMessage() + "\n");
        }

        this.overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
    }

    public void waitForAlertPresence() {
        this.waitExplicit = new WebDriverWait(this.driver, (long) WebAppDriverManager.getLONG_TIMEOUT());
        this.waitExplicit.until(ExpectedConditions.alertIsPresent());
    }

    public void overrideGlobalTimeout(int timeOut) {
        this.driver.manage().timeouts().implicitlyWait((long) timeOut, TimeUnit.SECONDS);
    }

    public void resetTimeOut() {
        this.driver.manage().timeouts().implicitlyWait((long) WebAppDriverManager.getLONG_TIMEOUT(), TimeUnit.SECONDS);
    }

    public void waitForPageLoaded() {
        ExpectedCondition expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState", new Object[0]).toString().equals("complete");
            }
        };

        try {
            Thread.sleep(1000L);
            WebDriverWait wait = new WebDriverWait(this.driver, 30L);
            wait.until(expectation);
        } catch (Throwable var3) {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }

    }

    public void scrollToElement(Object locatorOrElement, String... values) {
        if (locatorOrElement instanceof WebElement) {
            this.element = (WebElement) locatorOrElement;
        } else {
            String locator = String.format((String) locatorOrElement, (Object[]) values);
            this.element = this.driver.findElement(By.xpath(locator));
        }

        ((JavascriptExecutor) this.driver).executeScript("arguments[0].scrollIntoView(true);", new Object[]{this.element});
        this.sleepTimeInMilSecond(500);
    }

    public void scrollToElement(WebElement element, String... viewOptions) {
        String scriptFormatString = this.getBrowserName().equalsIgnoreCase("safari") ? "arguments[0].scrollIntoViewIfNeeded(%s);" : "arguments[0].scrollIntoView(%s);";
        String script = viewOptions.length == 0 ? String.format(scriptFormatString, "") : String.format(scriptFormatString, String.join("", viewOptions));
        ((JavascriptExecutor) this.driver).executeScript(script, new Object[]{element});
    }

    public void scrollToElementUntilDisplayed(int yOffset, WebElement element, int scrollTime) {
        int count = 0;

        boolean isDisplayed;
        do {
            this.scrollTo(0, yOffset);
            isDisplayed = this.isControlDisplayed(element, 1L);
            ++count;
        } while (!isDisplayed && count < scrollTime);

    }

    public void scrollUntil(int yOffset, int scrollTime) {
        int count = 0;

        do {
            this.scrollTo(0, yOffset);
            ++count;
        } while (count < scrollTime);

    }

    public boolean isControlDisplayed(WebElement element, long timeout) {
        try {
            element = this.waitForElementVisible(element, timeout);
            return element.isDisplayed();
        } catch (Exception var5) {
            return false;
        }
    }

    public WebElement waitForElementVisible(WebElement element, long timeout) {
        this.waitExplicit = new WebDriverWait(this.driver, timeout);

        try {
            return (WebElement) this.waitExplicit.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception var5) {
            return null;
        }
    }

    public void scrollToTopOfPage() {
        ((JavascriptExecutor) this.driver).executeScript("window.scrollBy(0,0)", new Object[0]);
    }

    public void scrollTo(int x, int y) {
        ((JavascriptExecutor) this.driver).executeScript(String.format("window.scrollBy(%s,%s)", x, y), new Object[0]);
    }

    public static boolean isSafari() {
        String browser = System.getProperty("setelWeb.browser");
        return browser.equalsIgnoreCase("safari");
    }

    public static boolean isMacOs() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().contains("mac");
    }

    public void clickToElementSafari(WebElement element) {
        if (isSafari()) {
            this.waitForElementVisible(element);
            this.clickToElementByJavascript(element);
        } else {
            this.clickToElement(element);
        }

    }

    public void clickToElementSafari(String locator, String... param) {
        if (isSafari()) {
            this.waitForElementVisible(locator, param);
            this.clickToElementByJavascript(locator, param);
        } else {
            this.clickToElement(locator, param);
        }

    }

    public void waitForElementInvisible(String locator, long timeout, String... values) {
        this.overrideGlobalTimeout(WebAppDriverManager.getSHORT_TIMEOUT());
        this.waitExplicit = new WebDriverWait(this.driver, timeout);
        locator = String.format(locator, (Object[]) values);
        this.byLocator = By.xpath(locator);

        try {
            this.waitExplicit.until(ExpectedConditions.invisibilityOfElementLocated(this.byLocator));
        } catch (Exception var6) {
            this.logger.warn(var6.getMessage());
        }

        this.overrideGlobalTimeout(WebAppDriverManager.getLONG_TIMEOUT());
    }

    public String getCssValue(String locator, String propertyName, String... values) {
        locator = String.format(locator, (Object[]) values);
        this.element = this.waitForElementVisible(locator);
        return this.element.getCssValue(propertyName);
    }

    public String getCssValue(WebElement element, String propertyName) {
        element = this.waitForElementVisible(element);
        return element.getCssValue(propertyName);
    }

    public void sendKeyToElementByJavascript(WebElement element, String value) {
        this.javascriptExecutor = (JavascriptExecutor) this.driver;
        this.javascriptExecutor.executeScript("arguments[0].setAttribute('value', '" + value + "')", new Object[]{element});
    }

    public void waitForPageLoadedByAsync() {
        final String async = String.format("var readyCallback = arguments[arguments.length - 1];var checkReadyState=function() {document.readyState !== 'complete' ? setTimeout(checkReadyState, %s) : readyCallback(true);};checkReadyState();", WebAppDriverManager.getLONG_TIMEOUT());

        try {
            Wait wait = (new FluentWait(this.driver)).withTimeout(Duration.ofSeconds((long) WebAppDriverManager.getLONG_TIMEOUT())).pollingEvery(Duration.ofMillis((long) (WebAppDriverManager.getLONG_TIMEOUT() * 10))).ignoring(Exception.class);
            wait.until(new Function<WebDriver, Boolean>() {
                public Boolean apply(WebDriver webDriver) {
                    return (Boolean) ((JavascriptExecutor) BasePage.this.driver).executeAsyncScript(async, new Object[0]);
                }
            });
        } catch (Throwable var3) {
            Reporter.log("Timeout waiting for Page loaded by asynchronous.", true);
        }

    }

    public List<WebElement> getElementsWithArgs(List<WebElement> elements, Object... args) {
        if (args != null && args.length != 0) {
            By by = null;
            if (java.lang.reflect.Proxy.isProxyClass(elements.getClass())) {
                LocatingElementListHandler handler = (LocatingElementListHandler) java.lang.reflect.Proxy.getInvocationHandler(elements);

                try {
                    Field locatorField = LocatingElementListHandler.class.getDeclaredField("locator");
                    locatorField.setAccessible(true);
                    DefaultElementLocator locator = (DefaultElementLocator) locatorField.get(handler);
                    Field byFeild = DefaultElementLocator.class.getDeclaredField("by");
                    byFeild.setAccessible(true);
                    by = (By) byFeild.get(locator);
                    by = this.parameter(by, args);
                    byFeild.set(locator, by);
                } catch (IllegalAccessException var8) {
                    Reporter.log("Cannot access the locator field by reflection: " + var8.getMessage(), true);
                } catch (NoSuchFieldException var9) {
                    Reporter.log("No such field matches to locator section: " + var9.getMessage(), true);
                }
            }

            return by == null ? elements : this.driver.findElements(by);
        } else {
            return elements;
        }
    }

    public WebElement getElementWithArgs(WebElement element, Object... args) {
        if (args != null && args.length != 0) {
            By by = null;
            if (java.lang.reflect.Proxy.isProxyClass(element.getClass())) {
                LocatingElementHandler handler = (LocatingElementHandler) Proxy.getInvocationHandler(element);

                try {
                    Field locatorField = LocatingElementHandler.class.getDeclaredField("locator");
                    locatorField.setAccessible(true);
                    DefaultElementLocator locator = (DefaultElementLocator) locatorField.get(handler);
                    Field byFeild = DefaultElementLocator.class.getDeclaredField("by");
                    byFeild.setAccessible(true);
                    by = (By) byFeild.get(locator);
                    by = this.parameter(by, args);
                    byFeild.set(locator, by);
                } catch (IllegalAccessException var8) {
                    Reporter.log("Cannot access the locator field by reflection: " + var8.getMessage(), true);
                } catch (NoSuchFieldException var9) {
                    Reporter.log("No such field matches to locator section: " + var9.getMessage(), true);
                }
            }

            return by == null ? element : this.driver.findElement(by);
        } else {
            return element;
        }
    }

    private By parameter(By by, Object... args) {
        String byDesc = by.toString();
        if (byDesc.indexOf(":") == -1) {
            return by;
        } else {
            String using = byDesc.substring(byDesc.indexOf(":") + 2);
            using = MessageFormat.format(using.replace("'", "''"), args);
            if (byDesc.startsWith("By.id")) {
                return By.id(using);
            } else if (byDesc.startsWith("By.name")) {
                return By.name(using);
            } else if (byDesc.startsWith("By.className")) {
                return By.className(using);
            } else if (byDesc.startsWith("By.cssSelector")) {
                return By.cssSelector(using);
            } else if (byDesc.startsWith("By.xpath")) {
                return By.xpath(using);
            } else if (byDesc.startsWith("By.tagName")) {
                return By.tagName(using);
            } else if (byDesc.startsWith("By.linkText")) {
                return By.linkText(using);
            } else {
                return byDesc.startsWith("By.partialLinkText") ? By.partialLinkText(using) : by;
            }
        }
    }
}