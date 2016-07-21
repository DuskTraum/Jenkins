package com.epam.yuri_karpov.selenium.ui.pageobject;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.epam.yuri_karpov.selenium.bo.Account;

import org.openqa.selenium.support.ui.FluentWait;

public class LoginPage {
	private static final String MY_GMAIL_ERROR = "My gmail does not exist";

	@FindBy(id = "Email")
	private WebElement loginInput;
	@FindBy(name = "Passwd")
	private WebElement passwordInput;
	@FindBy(id = "next")
	private WebElement nextBtn;
	@FindBy(id = "signIn")
	private WebElement signInBtn;

	private WebDriver driver;
	private static final Logger LOG = Logger.getLogger(LoginPage.class);

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}

	public LoginPage setLogin(Account account) {
		LOG.info("start 'setLogin'");
		LOG.info("Login:" + account.getLogin());
		new Actions(driver).sendKeys(loginInput, account.getLogin()).build().perform();
		LOG.info("finish 'setLogin'");
		return this;
	}

	public LoginPage clickNext() {
		LOG.info("start 'clickNext'");
		new Actions(driver).click(nextBtn).build().perform();
		LOG.info("finish 'clickNext'");
		return this;
	}

	public LoginPage setPassword(Account account) {
		LOG.info("start 'setPassword'");
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(15, TimeUnit.SECONDS)
				.pollingEvery(3, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Passwd")));
		new Actions(driver).sendKeys(passwordInput, account.getPassword()).build().perform();
		LOG.info("finish 'setPassword'");
		return this;
	}

	public MainPage signInBtn() {
		LOG.info("start 'signInBtn'");
		new Actions(driver).click(signInBtn).build().perform();
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(15, TimeUnit.SECONDS)
				.pollingEvery(3, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement myMail = wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//a[contains(@title, 'yurii.kaprov@gmail.com')]")));
		Assert.assertTrue(myMail.isDisplayed(), MY_GMAIL_ERROR);
		LOG.info("finish 'signInBtn'");
		return new MainPage(this.driver);
	}

}
