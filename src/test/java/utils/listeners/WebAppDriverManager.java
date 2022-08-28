package utils.listeners;

import java.io.IOException;
import java.util.Objects;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class WebAppDriverManager {
    private static final Logger logger = Logger.getLogger(WebAppDriverManager.class);
    private static ThreadLocal<WebDriver> driver = new ThreadLocal();
    private static int LONG_TIMEOUT = 30;
    private static int SHORT_TIMEOUT = 5;

    public WebAppDriverManager() {
    }
    public static WebDriver getDriver() {
        return (WebDriver) driver.get();
    }


    public static void setDriver(WebDriver webDriver) {
        driver.set(webDriver);
    }

    public static void closeBrowserAndDriver(WebDriver driver) {
        try {
            driver.manage().deleteAllCookies();
            driver.quit();
            logger.info("-------------QUIT BROWSER SUCCESSFULLY -----------------");
        } catch (Exception var2) {
            System.out.println(var2.getMessage());
            logger.info(var2.getMessage());
        }

    }

    public static void cleanAllBrowsers(WebDriver driver) {
        try {
            String driverName = ((WebDriver)Objects.requireNonNull(driver)).toString().toLowerCase();
            String osName = System.getProperty("os.name").toLowerCase();
            System.out.println("OS name= " + osName);
            logger.info("OS name= " + osName);
            String cmd = "";
            if (driverName.contains("chrome")) {
                if (osName.toLowerCase().contains("mac")) {
                    cmd = "killAll Google\\ Chrome";
                    executeCommand(cmd);
                    cmd = "killAll chromedriver";
                    executeCommand(cmd);
                } else if (osName.toLowerCase().contains("windows")) {
                    cmd = "taskkill /F /FI\"IMAGENAME eq chromedriver*\"";
                    executeCommand(cmd);
                }
            } else if (driverName.contains("safari")) {
                cmd = "killAll safaridriver";
                executeCommand(cmd);
                cmd = "killAll Safari";
                executeCommand(cmd);
            }
        } catch (Exception var4) {
            System.out.println(var4.getMessage() + "cannot kill browser");
            logger.info(var4.getMessage());
        }

    }

    private static void executeCommand(String cmd) throws InterruptedException, IOException {
        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
    }

    public static int getLONG_TIMEOUT() {
        return LONG_TIMEOUT;
    }

    public static void setLONG_TIMEOUT(int LONG_TIMEOUT) {
        WebAppDriverManager.LONG_TIMEOUT = LONG_TIMEOUT;
    }

    public static int getSHORT_TIMEOUT() {
        return SHORT_TIMEOUT;
    }

    public static void setSHORT_TIMEOUT(int SHORT_TIMEOUT) {
        WebAppDriverManager.SHORT_TIMEOUT = SHORT_TIMEOUT;
    }
}

