package tests.pagetest.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import page.steps.GetProductionFromTwoBrowserStep;
import tests.pagetest.base.BaseTest;
@Epic("GiangTester")
@Feature("Production")
public class GetProductionInTwoBrowserTest extends BaseTest {
    WebDriver driver;
    GetProductionFromTwoBrowserStep getProductionFromTwoBrowserStep=new GetProductionFromTwoBrowserStep();
    @Test(priority = 1, description = "sjhJSH")
    public void get_ProductionFromTwoOtherBrowser()
    {
        System.out.println("nnnn");
        // get spham IP13PRM tu 2 TMDT khac nhau va in ra san pham sap
        // xep thao gia tu nho den lon, in ra ten trang web


        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://www.shopee.vn/");
        driver.manage().window().maximize();

//        //Wrie code for shopee page
        driver.close();


    }
}
