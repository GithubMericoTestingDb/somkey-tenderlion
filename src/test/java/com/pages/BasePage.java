package com.pages;

import org.openqa.selenium.*;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

    private WebDriver driver;
    public WebDriverWait wait;

    //// Constructor ////

    public BasePage(WebDriver driver){
        this.driver = driver;
    }

    //// Locators ////

    private By signInButton = By.linkText("Sign in");
    By homePageUserName = By.xpath("//table//tr[@class='heading3']");

    //// Methods ////

    public void waitForInvisibilityOfElement(WebElement element) {
        wait = new WebDriverWait(driver,15);
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public void scrollToMiddle(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        String javascript = "arguments[0].scrollIntoView({behavior: \"smooth\", block: \"center\", inline: \"nearest\"})";
        js.executeScript(javascript, element);
    }

    public void scrollToTop(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        String javascript = "arguments[0].scrollIntoView(true);";
        js.executeScript(javascript, element);
    }

    public void highlightElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].setAttribute('style','background: Aqua; border: 2px solid pink;');", element);
    }

    //// Getters ////
    public String getHomePageDashboardUserName(){
        return    driver.findElement(homePageUserName).getText();
    }

}