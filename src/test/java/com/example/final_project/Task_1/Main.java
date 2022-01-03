package com.example.final_project.Task_1;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.JsonObject;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.devtools.v85.network.model.Request;
import org.testng.Assert;
import org.testng.annotations.*;
import static com.codeborne.selenide.Selenide.*;

public class Main {

    @DataProvider(name = "users")
    public Object[][] dpMethod() {
        return new Object[][]{
            {"Luka!@#123", "Luka!@#123"},
        };
    }

    @BeforeClass
    public static void browserInit() {
        Configuration.browser = "edge";
        Configuration.startMaximized = true;
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeMethod
    public void init() {
        open("https://demoqa.com/login");
    }

    @Test(dataProvider = "users")
    public void LogIn(String userName, String password) throws InterruptedException {
        $("#userName").setValue(userName);
        $("#password").setValue(password);
        $("#login").click();
        Thread.sleep(1000);
        $("#app > div > div > div.row > div.col-12.mt-4.col-md-6 > div.profile-wrapper > div.mt-2.buttonWrap.row > div.text-center.button > button")
        .click();
        Thread.sleep(200);
        $("#closeSmallModal-ok").click();
        var txt = switchTo().alert().getText();
        switchTo().alert().accept();
        Assert.assertEquals(txt, "User Deleted.");
        Thread.sleep(100);
    }


    @Test(dataProvider = "users", priority = 1)
    public void messageValidation(String userName, String password) throws InterruptedException {
        $("#userName").setValue(userName);
        Thread.sleep(500);
        $("#password").setValue(password);
        Thread.sleep(500);
        $("#login").click();
        Thread.sleep(500);
        System.out.println($x("/html/body/div[2]/div/div/div[2]/div[2]/div[1]/form/div[5]/div/p").getText(););
        Thread.sleep(500);
        Assert.assertEquals($("#name").getText(), "Invalid username or password!");
    }

    @Test(dataProvider = "users", priority = 2)
    public void SendRequest(String userName, String password) {
        RestAssured.baseURI = "https://bookstore.toolsqa.com/Account/v1";
        RequestSpecification req = RestAssured.given();
        var body = new JsonObject();
        body.addProperty("userName", userName);
        body.addProperty("password", password);
        req.header("Content-Type", "application/json");
        req.body(body.toString());
        var data = ParseBody(req.post("/Authorized").getBody());
        Assert.assertEquals(data.message, "User not found!");
    }


    private AuthDto ParseBody(ResponseBody body) {
        try {
            return body.as(AuthDto.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
