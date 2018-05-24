package com.test.framework.utils;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpUtil.class})
public class MockDemo {
	
	@Test
	public void test() {
		PowerMockito.mockStatic(HttpUtil.class);
		PowerMockito.when(HttpUtil.byGet("http://www.baidu.com", null)).thenReturn("{'name':'test'}");
		String str = HttpUtil.byGet("http://www.baidu.com", null);
		str = (String) JSONObject.fromObject(str).get("name");
		Assert.assertEquals(str, "test");
		
	}
	
}
