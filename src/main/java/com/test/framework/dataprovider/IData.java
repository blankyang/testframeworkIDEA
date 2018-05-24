package com.test.framework.dataprovider;

public interface IData {
	   
    public Object[][] getData(String caseName, String dataFile);
   
    public Object[][] getData(String caseName, String dataFile,int colNum) ;
   
    public Object[][] getData(String caseName, String dataFile,int beginNum,int endNum) ;
   
}
