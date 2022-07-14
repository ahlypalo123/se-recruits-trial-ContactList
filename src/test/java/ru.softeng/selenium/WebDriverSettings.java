package ru.softeng.selenium;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverSettings {
    public ChromeDriver chromeDriver = null;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");
        chromeDriver = new ChromeDriver();
    }

    @After
    public void close() {
        chromeDriver.quit();
    }
}
