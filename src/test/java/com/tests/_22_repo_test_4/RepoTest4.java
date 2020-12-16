package com.tests._22_repo_test_4;

import com.tests._05_webdriverwait_usage_patterns.begin.WaitUsagePatternCombinedTest;
import com.tests._12_synchronised_components.begin.SlowAndLoadableExampleTest;
import com.tests._15_awaitility.ThreadedFileCreator;
import com.tests._15_awaitility.end.AwaitilityWebDriverTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class RepoTest4 {

    WebDriver driver;

    @BeforeAll
    public static void setupDriver(){
        WebDriverManager.chromedriver().setup();
    }

    @Test
    public void canClickOnSecondButtonsWithComponentAndNoWebDriverWait(){

        driver = new ChromeDriver();

        driver.get("https://eviltester.github.io/synchole/buttons.html");

        await().ignoreException(NoSuchElementException.class).
                until(()-> driver.findElement(By.id("button00")).isEnabled());
        driver.findElement(By.id("button00")).click();

//        ButtonComponent startButton = new ButtonComponent(driver, By.id("button00"));
//        startButton.waitTillReady();
//        startButton.click();

        AwaitilityWebDriverTest.ButtonComponent buttonOne = new AwaitilityWebDriverTest.ButtonComponent(driver, By.id("button01"));
        buttonOne.waitTillReady();
        buttonOne.click();

        AwaitilityWebDriverTest.ButtonComponent buttonTwo = new AwaitilityWebDriverTest.ButtonComponent(driver, By.id("button02"));
        buttonTwo.waitTillReady();
        buttonTwo.click();

        AwaitilityWebDriverTest.ButtonComponent buttonThree = new AwaitilityWebDriverTest.ButtonComponent(driver, By.id("button03"));
        buttonThree.waitTillReady();
        buttonThree.click();

        Assertions.assertEquals("All Buttons Clicked",
                driver.findElement(By.id("buttonmessage")).getText());
    }

    @AfterEach
    public void closeDriver(){
        driver.close();
    }

    private class ButtonComponent {
        private final By locator;
        private final WebDriver myDriver;
        private WebElement elem;

        public ButtonComponent(WebDriver myDriver, final By elementLocator) {
            this.myDriver = myDriver;
            this.locator = elementLocator;
        }

        public void waitTillReady() {
            await().ignoreException(NoSuchElementException.class).
                    until(isClickable());
        }

        private Callable<Boolean> isClickable() {
            elem = this.myDriver.findElement(locator);
            return () -> elem.isDisplayed() && elem.isEnabled();
        }

        public void click() {
            elem.click();
        }
    }

    @Test
    public void canWaitOnOtherStuffAgain() throws IOException {

        // Create the file in another thread

        File outputFolder = new File(System.getProperty("user.dir"),"temp");
        File outputFile = new File(outputFolder,System.currentTimeMillis()+".txt");

        ThreadedFileCreator w = new ThreadedFileCreator(
                outputFolder, outputFile, 3000 );
        new Thread(w).start();

        // Wait for file to be created

        await().
                atMost(Duration.ofMillis(5000)).
                until(() -> outputFile.exists());

        // Check File Contents

        final byte[] contents = Files.readAllBytes(outputFile.toPath());
        Assertions.assertEquals("File Contents", new String(contents));

        // Testing stuff to print out
        System.out.println("Testing stuff to print out");
    }

    @Test
    public void canClickOnSecondButtonsWithComponentOtherTest(){

        driver = new ChromeDriver();

        driver.get("https://eviltester.github.io/synchole/buttons.html");

        SlowAndLoadableExampleTest.ButtonComponent startButton = new SlowAndLoadableExampleTest.ButtonComponent(driver, By.id("button00"));
        startButton.waitTillReady();
        startButton.click();

        SlowAndLoadableExampleTest.ButtonComponent buttonOne = new SlowAndLoadableExampleTest.ButtonComponent(driver, By.id("button01"));
        buttonOne.waitTillReady();
        buttonOne.click();

        SlowAndLoadableExampleTest.ButtonComponent buttonTwo = new SlowAndLoadableExampleTest.ButtonComponent(driver, By.id("button02"));
        buttonTwo.waitTillReady();
        buttonTwo.click();

        SlowAndLoadableExampleTest.ButtonComponent buttonThree = new SlowAndLoadableExampleTest.ButtonComponent(driver, By.id("button03"));
        buttonThree.waitTillReady();
        buttonThree.click();

        Assertions.assertEquals("All Buttons Clicked",
                driver.findElement(By.id("buttonmessage")).getText());
    }

    @Test
    public void sharedForADriverContextMoreStuff(){

        driver = new ChromeDriver();

        final WaitUsagePatternCombinedTest.CollapsePage collapse = new WaitUsagePatternCombinedTest.CollapsePage(driver);
        collapse.get();
        collapse.clickAbout();

        Assertions.assertTrue(driver.
                getCurrentUrl().contains("about.html"));

        System.out.println("Just printing ");

    }

    @Test
    public void canNavigateToAboutFromExpandingSectionLinkAgain(){

        driver = new ChromeDriver();

        driver.get("https://somethinelse.github.io/synchole/collapseable.html");

        final WebElement section = driver.findElement(
                By.cssSelector("section.condense"));
        section.click();

        //Thread.sleep(1000);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        final WebElement sectionLink = driver.findElement(
                By.cssSelector("a#aboutlink"));
        sectionLink.click();

        Assertions.assertTrue(driver.getCurrentUrl().contains("about.html"));
    }
}
