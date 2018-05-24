package com.test.framework.report;

import java.io.File;
import java.net.InetAddress;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.NetworkMode;

public class ExtentManager {
    private static ExtentReports extent;
    
    public synchronized static ExtentReports getReporter(String filePath) throws Exception {
        if (extent == null) {
            extent = new ExtentReports(filePath, false,NetworkMode.OFFLINE);
            extent.loadConfig(new File("extent-config.xml"));
            extent
                .addSystemInfo("Host Name", InetAddress.getLocalHost().getHostName())
                .addSystemInfo("Environment", "QA");
        }
        return extent;
    }
}