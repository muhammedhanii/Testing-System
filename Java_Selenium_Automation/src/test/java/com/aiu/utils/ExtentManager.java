package com.aiu.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

/**
 * ExtentManager - Manages ExtentReports configuration
 */
public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports createInstance() {
        if (extent == null) {
            String reportPath = ConfigReader.getReportPath();
            String reportName = ConfigReader.getExtentReportName();
            
            // Create directory if it doesn't exist
            File directory = new File(reportPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fullPath = reportPath + reportName;
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(fullPath);

            // Configure report
            sparkReporter.config().setDocumentTitle("AIU Trips & Events - Automation Test Report");
            sparkReporter.config().setReportName("Selenium Automation Test Results");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setEncoding("UTF-8");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            
            // System information
            extent.setSystemInfo("Application", "AIU Trips & Events System");
            extent.setSystemInfo("Environment", "Test");
            extent.setSystemInfo("Browser", ConfigReader.getBrowser());
            extent.setSystemInfo("Base URL", ConfigReader.getBaseUrl());
            extent.setSystemInfo("Tester", "QA Automation Team");
        }

        return extent;
    }

    public static ExtentReports getInstance() {
        if (extent == null) {
            return createInstance();
        }
        return extent;
    }
}
