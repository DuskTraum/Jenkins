package com.epam.yuri_karpov.selenium.ui.pageobject;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class GooglePage {
	private WebDriver driver;
	private static final Logger LOG = Logger.getLogger(GooglePage.class);
	
	@FindBy (xpath = "//*[@href='https://mail.google.com/mail/?tab=wm' and contains(text(),'Gjxrf')]")
	private WebElement mailBtn;
	
	public GooglePage(WebDriver driver){
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}
	
	public LoginPage clickMail(){
		LOG.info("start 'clickMail'");
		mailBtn.click();
		LOG.info("finish 'clickMail'");
		return new LoginPage(this.driver);
	}
}
