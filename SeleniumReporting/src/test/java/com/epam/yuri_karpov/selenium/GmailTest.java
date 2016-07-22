package com.epam.yuri_karpov.selenium;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.uncommons.reportng.HTMLReporter;

import com.epam.yuri_karpov.selenium.bo.Account;
import com.epam.yuri_karpov.selenium.bo.Letter;
import com.epam.yuri_karpov.selenium.service.MailService;
import com.epam.yuri_karpov.selenium.ui.pageobject.MainPage;
import com.epam.yuri_karpov.selenium.ui.webdriver.Driver;
import com.epam.yuri_karpov.selenium.utils.ScreenShot;
import com.epam.yuri_karpov.selenium.utils.ScreenShotListener;

@Listeners({ HTMLReporter.class, ScreenShotListener.class })
public class GmailTest {

	private static final Logger LOG = Logger.getLogger(GmailTest.class);
	private WebDriver driver;

	private Account account;
	private Object[][] data = new Object[3][3];
	private MailService mailService;

	@BeforeTest
	public void startBrowser() {
		LOG.warn("start 'startBrowser'");
		LOG.warn("startBrowser 'delete old screenshots'");
		ScreenShot.deleteAllScreenShots();
		driver = Driver.getWebDriverInstance();
		mailService = new MailService(driver);
		LOG.warn("finish 'startBrowser'");
	}

	@Test
	public void gmailLogin() {
		LOG.info("start 'gmailLogin'");
		account = new Account("yurii.kaprov@gmail.com", "151611eB0");
		driver.get("https://www.google.ru/");
		mailService.loginToMail(account);
		ScreenShot.make(driver);
		LOG.info("finish 'gmailLogin'");
	}

	@Test(dependsOnMethods = "gmailLogin", dataProvider = "mailData")
	public void gmailDraftsGenerator(String to, String subj, String text) {
		LOG.info("start 'gmailDraftsGenerator'");
		Letter letter = initLetters(to, subj, text);
		mailService.generateDrafts(letter);
		LOG.info("finish 'gmailDraftsGenerator'");
	}

	@Test(dependsOnMethods = "gmailDraftsGenerator", dataProvider = "mailData")
	public void gmailDraftsCheck(String to, String subj, String text) {
		LOG.info("start 'gmailDraftsCheck'");
		Letter letter = initLetters(to, subj, text);
		mailService.checkDrafts(letter);
		LOG.info("finish 'gmailDraftsCheck'");
	}

	@Test(dependsOnMethods = "gmailDraftsCheck")
	public void discardDrafts() {
		LOG.info("start 'discardDrafts'");
		MainPage main = new MainPage(driver);
		main.discardDrafts();
		LOG.info("finish 'discardDrafts'");
	}

	@Test(dependsOnMethods = "discardDrafts", dataProvider = "mailData")
	public void gmailsSendMsgs(String to, String subj, String text) {
		LOG.info("start 'gmailsSendMsgs'");
		Letter letter = initLetters(to, subj, text);
		mailService.sendLetter(letter);
		LOG.info("finish 'gmailsSendMsgs'");
	}

	@Test(dependsOnMethods = "gmailsSendMsgs", dataProvider = "mailData")
	public void checkSentMails(String to, String subj, String text) {
		LOG.info("start 'checkSentMails'");
		Letter letter = initLetters(to, subj, text);
		mailService.checkSentMails(letter);
		LOG.info("finish 'checkSentMails'");
	}

	@Test(dependsOnMethods = "checkSentMails")
	public void discardMails() {
		LOG.info("start 'discardMails'");
		MainPage main = new MainPage(driver);
		main.discardSentMails();
		LOG.info("finish 'discardMails'");
	}

	@Test(dependsOnMethods = "discardMails")
	public void logOut() {
		LOG.info("start 'logOut'");
		mailService.logOut();
		LOG.info("finish 'logOut'");
	}

	@DataProvider(name = "mailData")
	public Object[][] mailData() {
		List<String[]> dataList = new ArrayList<>();
		try (BufferedReader input = new BufferedReader(new FileReader("DataProvider.txt"))) {
			String line;
			while ((line = input.readLine()) != null) {
				dataList.add(line.split(","));
			}
			data = dataList.toArray(new String[][] {});
		} catch (FileNotFoundException fe) {
			System.err.println(fe);
		} catch (IOException e) {
			System.err.println(e);
		}
		return data;
	}

	public Letter initLetters(String to, String subj, String text) {
		Letter letter = new Letter(to, subj, text);
		return letter;
	}
	
	public WebDriver getDriver(){
		return this.driver;
	}

	@AfterTest
	public void closeBrowser() {
		LOG.warn("start 'closeBrowser'");
		driver.quit();
		LOG.warn("finish 'closeBrowser'");
	}
}
