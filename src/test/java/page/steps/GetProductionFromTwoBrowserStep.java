package page.steps;

import Base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class GetProductionFromTwoBrowserStep extends BasePage {

    public GetProductionFromTwoBrowserStep() {
    }
    @FindBy(xpath = "//font[text()='2']")
    WebElement CLICK_PAGE;

    @Step("Get Iphone 13 prm LAZADA")
    public void get_SearchIPFromShopee() {

    }
}
