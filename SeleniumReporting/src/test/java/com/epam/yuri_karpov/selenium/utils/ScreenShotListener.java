package com.epam.yuri_karpov.selenium.utils;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.epam.yuri_karpov.selenium.GmailTest;

public class ScreenShotListener implements ITestListener {

	private void screenMake(ITestResult result) {
		Object inst = result.getInstance();
		
		if(inst == null){
			return;
		}
		if (!(inst instanceof GmailTest)){
			return;
		}
		GmailTest gmailTest = (GmailTest) inst;
		WebDriver driver  = gmailTest.getDriver();
		
		if (driver!=null){
			ScreenShot.make(driver, "AutoScreenShot");
		}
	}

	@Override
	public void onTestStart(ITestResult result) {

	}

	@Override
	public void onTestSuccess(ITestResult result) {

	}

	@Override
	public void onTestFailure(ITestResult result) {
		screenMake(result);
	}

	@Override
	public void onTestSkipped(ITestResult result) {

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	@Override
	public void onStart(ITestContext context) {

	}

	@Override
	public void onFinish(ITestContext context) {

	}

}
