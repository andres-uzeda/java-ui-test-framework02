import Data_Entities.Response;
import POM.MainPage;
import Utils.Util;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

public class TestFirstApproach {
    String email;
    String name;
    WebDriver driver;
    MainPage mainPage;

    @BeforeTest
    public void SetUp(){
        do {
            Response response = given().
                    header("accept", "application/json").
                    header("content-type", "application/json").
                    when().
                    get("http://dummy.restapiexample.com/api/v1/employee/1").as(Response.class);
            email = Util.CreateAnEMail(response.getData().getEmployee_name());
            name = Util.ObtainName(response.getData().getEmployee_name());
        }while (name.equals(""));
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        chromeOptions.addArguments("--headless");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://formy-project.herokuapp.com/");
        mainPage = new MainPage(driver);
    }

    @Test
    public void VerifyThatAFormWasFilledUp(){
        String actual = mainPage.SelectCompleteWebFormMenu().FillUpForm(name, email).VerifyThatAFormWasSent();
        Assert.assertEquals(actual,"The form was successfully submitted!");
    }

    @AfterTest
    public void AfterSteps(){
        driver.close();
    }
}
