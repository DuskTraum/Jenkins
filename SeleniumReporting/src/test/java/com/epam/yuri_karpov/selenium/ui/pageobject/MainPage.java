package com.epam.yuri_karpov.selenium.ui.pageobject;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.testng.Assert;

import com.epam.yuri_karpov.selenium.bo.Letter;

public class MainPage {

	private static final Logger LOG = Logger.getLogger(MainPage.class);
	private static final String SAVED = "Saved";
	private static final String NEW_MESSAGE_ERROR = "New message box does not exist!";
	private static final String VALUE = "value";
	private static final String DRAFTS_HAVE_BEEN_DELETED_MSG = "//span[contains(text(), 'Drafts have been deleted')]";

	@FindBy(xpath = "//div[contains(text(), 'COMPOSE')]")
	private WebElement composeBtn;

	@FindBy(xpath = "//textarea[@name='to']")
	private WebElement composeTo;

	@FindBy(xpath = "//input[@name = 'subjectbox']")
	private WebElement composeSubject;

	@FindBy(xpath = "//input[@type = 'hidden' and @name = 'subject']")
	private WebElement hiddenSubject;

	@FindBy(xpath = "//div[@aria-label = 'Message Body']")
	private WebElement textField;

	@FindBy(xpath = "//img[@alt = 'Close']")
	private WebElement closeComposeBtn;

	@FindBy(xpath = "//div[contains(text(), 'New Message')]")
	private WebElement newMsgBox;

	@FindBy(xpath = "//a[contains(text(), 'Drafts')]")
	private WebElement draftsBtn;

	@FindBy(xpath = "//div[@gh]//span[@role='checkbox' and @dir = 'ltr']")
	private WebElement selectMails;

	@FindBy(xpath = "//div[contains(text(), 'Discard drafts')]")
	private WebElement discardDrafts;

	@FindBy(xpath = "//a[contains(@title, 'yurii.kaprov@gmail.com')]")
	private WebElement myMailBtn;

	@FindBy(xpath = "//a[contains(text(), 'Sign out')]")
	private WebElement signOutBtn;

	@FindBy(xpath = "//div[contains(@data-tooltip,'Send(Ctrl-Enter)‬')]")
	private WebElement sendMsg;

	@FindBy(xpath = "//span[contains(text(), 'View message')]")
	private WebElement sendMsgconfirm;

	@FindBy(xpath = "//div[@aria-label = 'Back to Sent Mail']")
	private WebElement backToSentMailBtn;

	@FindBy(xpath = "//a[contains(text(),'Sent Mail')]")
	private WebElement sentMail;

	@FindBy(xpath = "//img[@data-tooltip='Show details']")
	private WebElement msgInfo;

	@FindBy(xpath = "//div[@data-tooltip='Delete']")
	private WebElement deleteSentMails;

	@FindBy(xpath = "//button[@name='ok']")
	private WebElement okButton;

	@FindBy(xpath = "//div[@gh='tm']//div[@title='Refresh']/div/div | //div[@gh='tm']//div[@aria-label='Refresh']/div/div")
	private WebElement refreshButton;

	private WebDriver driver;

	public MainPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}

	public void composeMsg(Letter letter) {
		LOG.info("start 'composeMsg'");
		LOG.info(letter.toString());
		composeBtn.click();
		Assert.assertTrue(newMsgBox.isDisplayed(), NEW_MESSAGE_ERROR);
		composeTo.sendKeys(letter.getTo());
		composeSubject.sendKeys(letter.getSubj());
		textField.sendKeys(letter.getText());
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(15, TimeUnit.SECONDS)
				.pollingEvery(3, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement draftSave = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'Saved')]")));
		Assert.assertEquals(draftSave.getText(), SAVED);

		closeComposeBtn.click();
		LOG.info("finish 'composeMsg'");
	}

	public void composeAndSendMsg(Letter letter) {
		LOG.info("start 'composeAndSendMsg'");
		composeBtn.click();
		Assert.assertTrue(newMsgBox.isDisplayed(), NEW_MESSAGE_ERROR);
		LOG.info(letter.toString());
		composeTo.sendKeys(letter.getTo());
		composeSubject.sendKeys(letter.getSubj());
		textField.sendKeys(letter.getText());
		sendMsg.click();
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(15, TimeUnit.SECONDS)
				.pollingEvery(3, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		WebElement msgSent = wait.until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'View message')]")));
		Assert.assertEquals(msgSent.getText(), "View message");
		LOG.info("finish 'composeAndSendMsg'");
	}

	public void checkDrafts(Letter letter) {
		LOG.info("start 'checkDrafts'");
		draftsBtn.click();
		WebElement temp = driver.findElement(By.xpath("//span[contains(text(),'" + letter.getSubj() + "')]"));
		temp.click();
		WebElement tmpComposeTo = driver.findElement(By.xpath(
				"//span[contains(@email, '" + letter.getTo() + "') and contains(text(), '" + letter.getTo() + "')]"));
		Assert.assertEquals(letter.getTo(), tmpComposeTo.getText());
		Assert.assertEquals(letter.getSubj(), composeSubject.getAttribute(VALUE));
		Assert.assertEquals(letter.getText(), textField.getText());
		closeComposeBtn.click();
		LOG.info("finish 'checkDrafts'");
	}

	public void refreshPage() {
		refreshButton.click();
	}

	public void checkSentMail(Letter letter) {
		LOG.info("start 'checkSentMail'");
		new Actions(driver).click(sentMail).build().perform();
		WebElement secondTemp = driver.findElement(By.xpath("//span[contains(text(),'" + letter.getSubj() + "')]"));
		secondTemp.click();
		WebElement sentMailComposeSubj = driver
				.findElement(By.xpath("//h2[contains(text(), '" + letter.getSubj() + "')]"));
		WebElement sentMailText = driver.findElement(By.xpath("//div[contains(text(), '" + letter.getText() + "')]"));
		Assert.assertEquals(letter.getSubj(), sentMailComposeSubj.getText());
		Assert.assertEquals(letter.getText(), sentMailText.getText());
		LOG.info("finish 'checkSentMail'");
	}

	public void discardDrafts() {
		LOG.info("start 'discardDrafts'");
		selectMails.click();
		discardDrafts.click();
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(15, TimeUnit.SECONDS)
				.pollingEvery(3, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(DRAFTS_HAVE_BEEN_DELETED_MSG)));
		LOG.info("finish 'discardDrafts'");
	}

	public void discardSentMails() {
		LOG.info("start 'discardSentMails'");
		sentMail.click();
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(15, TimeUnit.SECONDS)
				.pollingEvery(3, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);

		WebElement tempSelectMails = wait.until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//div[@gh]//span[@role='checkbox' and @dir = 'ltr']")));
		tempSelectMails.click();
		deleteSentMails.click();

		WebElement tempOkBtn = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@name='ok']")));
		tempOkBtn.click();
		LOG.info("finish 'discardSentMails'");
	}

	public void logOut() {
		LOG.info("start 'logOut'");
		JavascriptExecutor ex = (JavascriptExecutor) driver;
		ex.executeScript("arguments[0].click();", myMailBtn);
		signOutBtn.click();
		LOG.info("finish 'logOut'");
	}

}
