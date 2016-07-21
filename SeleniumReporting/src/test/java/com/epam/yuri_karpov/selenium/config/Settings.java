package com.epam.yuri_karpov.selenium.config;

import org.kohsuke.args4j.Option;

public class Settings {

	@Option(name = "--testng", usage = "set path to TestNg xml", required = true)
	public String testNgPath;
	
	@Override
	 public String toString() {
	  return "\ntestNgPath: " + testNgPath;
	 }

}
