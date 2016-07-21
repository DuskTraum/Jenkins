package com.epam.yuri_karpov.selenium.service;

import org.openqa.selenium.WebDriver;
import com.epam.yuri_karpov.selenium.bo.Account;
import com.epam.yuri_karpov.selenium.bo.Letter;
import com.epam.yuri_karpov.selenium.ui.pageobject.GooglePage;
import com.epam.yuri_karpov.selenium.ui.pageobject.MainPage;

public class MailService {

	private WebDriver driver;

	public MailService(WebDriver driver) {
		this.driver = driver;
	}

	public void loginToMail(Account account) {
		GooglePage gPage = new GooglePage(driver);
		gPage.clickMail().setLogin(account).clickNext().setPassword(account).signInBtn();
	}

	public void sendLetter(Letter letter) {
		MainPage main = new MainPage(driver);
		main.composeAndSendMsg(letter);
	}

	public void checkSentMails(Letter letter) {
		MainPage main = new MainPage(driver);
		main.checkSentMail(letter);
	}

	public void checkDrafts(Letter letter) {
		MainPage main = new MainPage(driver);
		main.checkDrafts(letter);
	}

	public void generateDrafts(Letter letter) {
		MainPage main = new MainPage(driver);
		main.composeMsg(letter);

	}

	public void logOut() {
		MainPage main = new MainPage(driver);
		main.logOut();
	}

}
