package ru.softeng.selenium;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import ru.softeng.propertiesReader.DetermineLocl;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContactSeleniumTest extends WebDriverSettings {

    private static final String IMG = "https://habrastorage.org/webt/0e/rn/j0/0ernj0wwnqnwdejls6zvzjup5k8.png";
    private static final String NAME = "Java";
    private static final String PHONE = "89281372285";
    private static final String DESCRIPTION = "programming language";

    private static final String IMG_EDITED = "https://pluralsight.imgix.net/paths/path-icons/c-plus-plus-93c7ddd5cc.png";
    private static final String NAME_EDITED = "C++";
    private static final String PHONE_EDITED = "321-123";
    private static final String DESC_EDITED = "also programming language";

    @Test
    public void checkAddContact() {

        //Checking standart page elements
        chromeDriver.get("localhost:8080/contacts/create");
        Assert.assertEquals(chromeDriver.getTitle(), DetermineLocl.getString("titleCreate"));
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("titleCreate")));
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("viewImg")));
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("contactImage")));
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("contactName")));
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("contactPhone")));
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("contactDescription")));
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("update")));
        Assert.assertTrue(chromeDriver.findElement(By.id("reset")).isEnabled());
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("cancel")));
        Assert.assertTrue(chromeDriver.findElement(By.id("cancel")).isEnabled());
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("add")));

        //Add contact
        if (chromeDriver.findElement(By.id("add_contact")).isEnabled()) {
            String del = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE;
            WebElement element = chromeDriver.findElement(By.id("image"));

            element.sendKeys(del + IMG);
            element = chromeDriver.findElement(By.id("name"));
            element.sendKeys(del + NAME);
            element = chromeDriver.findElement(By.id("phone"));
            element.sendKeys(del + PHONE);
            element = chromeDriver.findElement(By.id("description"));
            element.sendKeys(del + DESCRIPTION);
            element = chromeDriver.findElement(By.id("add_contact"));
            element.click();

            try {
                Thread.sleep(1000L);
            } catch (Exception e) {}

            chromeDriver.switchTo().alert().accept();
         }
    }

    @Test
    public void checkEditContact() {
        chromeDriver.get("localhost:8080/contacts");

        //Checking standart page elements
        Assert.assertEquals(chromeDriver.getTitle(), DetermineLocl.getString("titleList"));
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("titleList")));
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("searchByName")));
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("searchAll")));
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("addContact")));
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("contactName")));
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("contactDescription")));
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("contactPhone")));
        Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("search")));

        //Checking page elements available when contact exists
        if (chromeDriver.findElement(By.id("searchByName")).isEnabled()) {
            WebElement element = chromeDriver.findElement(By.id("search"));

            //Search by NAME and check web-elements of contact
            element.sendKeys(NAME);
            element = chromeDriver.findElement(By.id("searchByName"));
            element.click();
            Assert.assertTrue(chromeDriver.getPageSource().contains(NAME));
            Assert.assertTrue(chromeDriver.getPageSource().contains(IMG));
            Assert.assertTrue(chromeDriver.getPageSource().contains(PHONE));
            Assert.assertTrue(chromeDriver.getPageSource().contains(DESCRIPTION));
            Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("delete")));
            Assert.assertTrue(chromeDriver.findElement(By.id("delete")).isEnabled());
            Assert.assertTrue(chromeDriver.getPageSource().contains(DetermineLocl.getString("edit")));

            //Edit contact
            if (chromeDriver.findElement(By.id("edit")).isEnabled()) {
                String del = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE;

                element = chromeDriver.findElement(By.id("edit"));
                element.click();
                element = chromeDriver.findElement(By.id("image"));
                element.sendKeys(del + IMG_EDITED);
                element = chromeDriver.findElement(By.id("name"));
                element.sendKeys(del + NAME_EDITED);
                element = chromeDriver.findElement(By.id("phone"));
                element.sendKeys(del + PHONE_EDITED);
                element = chromeDriver.findElement(By.id("description"));
                element.sendKeys(del + DESC_EDITED);
                element = chromeDriver.findElement(By.id("add_contact"));
                element.click();

                //try {
                //    Thread.sleep(1000L);
                //} catch (Exception e) {}
                //
                //chromeDriver.switchTo().alert().accept();
            }
        }
    }

    @Test
    public void checkInvalidInfo() {
        String del = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE;
        chromeDriver.get("localhost:8080/contacts");

        WebElement element = chromeDriver.findElement(By.id("search"));
        element.sendKeys(NAME_EDITED);
        element = chromeDriver.findElement(By.id("searchByName"));
        element.click();
        element = chromeDriver.findElement(By.id("edit"));
        element.click();

        WebElement editContact = chromeDriver.findElement(By.id("add_contact"));
        WebElement updatePage = chromeDriver.findElement(By.id("reset"));

        //Incorrect name
        element = chromeDriver.findElement(By.id("name"));
        element.sendKeys(del + "123456789012345678901");
        editContact.click();

        try {
            Thread.sleep(1000L);
        } catch (Exception e) {}

        chromeDriver.switchTo().alert().accept();
        element.sendKeys(del + "");
        editContact.click();

        try {
            Thread.sleep(1000L);
        } catch (Exception e) {}

        chromeDriver.switchTo().alert().accept();
        updatePage.click();

        //Incorrect phone
        element = chromeDriver.findElement(By.id("phone"));
        element.sendKeys(del + "1");
        editContact.click();

        try {
            Thread.sleep(1000L);
        } catch (Exception e) {}

        chromeDriver.switchTo().alert().accept();
        element.sendKeys(del + "123456");
        editContact.click();

        try {
            Thread.sleep(1000L);
        } catch (Exception e) {}

        chromeDriver.switchTo().alert().accept();
        element.sendKeys(del + "123-s21");
        editContact.click();

        try {
            Thread.sleep(1000L);
        } catch (Exception e) {}

        chromeDriver.switchTo().alert().accept();
        element.sendKeys(del + "");
        editContact.click();

        try {
            Thread.sleep(1000L);
        } catch (Exception e) {}

        chromeDriver.switchTo().alert().accept();
        updatePage.click();

        //Incorrect description
        element = chromeDriver.findElement(By.id("description"));
        element.sendKeys(del + "this is the incorrect description, because it has many symbols for this field");
        editContact.click();

        try {
            Thread.sleep(1000L);
        } catch (Exception e) {}

        chromeDriver.switchTo().alert().accept();
    }

    @Test
    public void deleteContact() {
        chromeDriver.get("localhost:8080/contacts");

        WebElement element = chromeDriver.findElement(By.id("search"));
        element.sendKeys(NAME_EDITED);
        element = chromeDriver.findElement(By.id("searchByName"));
        element.click();
        Assert.assertTrue(chromeDriver.getPageSource().contains(NAME_EDITED));
        Assert.assertTrue(chromeDriver.getPageSource().contains(IMG_EDITED));
        Assert.assertTrue(chromeDriver.getPageSource().contains(PHONE_EDITED));
        Assert.assertTrue(chromeDriver.getPageSource().contains(DESC_EDITED));
        element = chromeDriver.findElement(By.id("delete"));
        element.click();
    }
}