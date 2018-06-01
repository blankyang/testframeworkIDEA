package com.test.framework.report;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.test.framework.utils.FileUtil;
import com.test.framework.utils.MailUtil;

public abstract class ExtentBase {
    protected ExtentReports extent;
    protected static ExtentTest test;
    protected String fileName = "接口测试.html";
    final String filePath = System.getProperty("user.dir") 
    		+ "/report/" 
    		+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())
    		+ "/" + new SimpleDateFormat("HH-mm-ss").format(new Date())
    		+ "/" + fileName;
 
    @AfterMethod
    protected void afterMethod(ITestResult result) {
//        if (result.getStatus() == ITestResult.FAILURE) {
//            test.log(LogStatus.FAIL, result.getThrowable().toString());
//        } else if (result.getStatus() == ITestResult.SKIP) {
//            test.log(LogStatus.SKIP, "Test skipped " + result.getThrowable().toString());
//        } else {
//            test.log(LogStatus.PASS, "Test passed");
//        }
        if (result.getStatus() == ITestResult.SKIP) {
            test.log(LogStatus.SKIP, result.getThrowable().toString());
        }
        extent.endTest(test);
        extent.flush();
    	
   }
   
    @BeforeSuite
    public void beforeSuite() throws Exception {
        extent = ExtentManager.getReporter(filePath);
    }
    
    @AfterSuite
    protected void afterSuite() throws Exception {
    	extent.flush();
//      FileOutputStream fos1 = new FileOutputStream(new File("report/test.zip"));
//		FileUtil.toZip("report/2018-04-23", fos1, true);
//		List<File> attachments = new ArrayList<File>();
//		attachments.add(new File("report/test.zip"));
//		MailUtil.sendFilesEmail("yangyangtbs@163.com", "yangyangtbs@163.com","测试报告", "接口测试", attachments);
//		FileUtil.deleteFile("report/test.zip");
    }
    
    
}
