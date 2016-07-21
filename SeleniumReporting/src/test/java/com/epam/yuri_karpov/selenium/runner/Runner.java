package com.epam.yuri_karpov.selenium.runner;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.testng.TestNG;
import org.testng.xml.Parser;
import org.testng.xml.XmlSuite;

import com.epam.yuri_karpov.selenium.config.Settings;
import com.epam.yuri_karpov.selenium.exceptions.TestNGRunException;

public class Runner {

	protected TestNG testng = new TestNG();
	private String testNgConfig;
	
	public static void main(String[] args){
		new Runner(args).run();
	}
	
	public Runner(String[] args){
		Settings settings = new Settings();
		CmdLineParser parser = new CmdLineParser(settings);
		
		
		try{
			parser.parseArgument(args);
			testNgConfig = settings.testNgPath;
			System.out.println("Setting: " + settings);
		} catch(CmdLineException e){
			System.err.println(e.toString());
			parser.printUsage(System.out);
		}
	}
	
	
	public void run(){
		try{
			XmlSuite xmlSuite = new Parser(testNgConfig).parseToList().get(0);
			this.testng.setCommandLineSuite(xmlSuite);
			this.testng.run();
		} catch (Exception ex){
			throw new TestNGRunException("Error running Test NG suite" + ex.getMessage());
		}
	}
	
}
