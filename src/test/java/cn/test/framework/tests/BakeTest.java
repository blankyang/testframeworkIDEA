package cn.test.framework.tests;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.relevantcodes.extentreports.LogStatus;
import com.test.framework.report.ExtentBase;
import com.test.framework.utils.AssertUtil;
import com.test.framework.utils.HttpClientUtil;
import com.test.framework.utils.HttpUtil;
import com.test.framework.utils.MD5Util;

public class BakeTest extends ExtentBase {

	public static final String MD5_KEY = "f652724b231d0cc23122a6b3e1646036";

	public static final String MD5_KEY_TH = "f652724b231d0cc23122a6b33e1646033";

	public static final String DEV_repaymentPlanUpload = "http://dev-pim-api.baketechfin.com/api/repaymentPlanUpload";

	public static final String DEV_contractUploadd = "http://dev-pim-api.baketechfin.com/api/contractUpload";

	@Test
	public void repaymentPlanUpload() throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		List<Map<String, String>> maps = HttpClientUtil.getExcelData("getPlan");
		for (int i = 0; i < maps.size(); i++) {
			params = maps.get(i);
			params.put("projectId", "1");
			params.put("contractNo", params.get("contractNo"));
			params.put("planJson", params.get("planJson"));
			params = getSignParams2(params);
			String result = HttpClientUtil.sendPost4File(
					DEV_repaymentPlanUpload,null,params,null,null);
			System.out.println("上传还款计划表:" + result);
		}

	}

	@Test
	public void contractUpload() throws Exception {
		test = extent.startTest(this.getClass().getSimpleName() + "_"
				+ "contractUpload");
		List<Map<String, String>> maps = HttpClientUtil
				.getExcelData("getContract");
		File[] fileArray = { new File("C:\\Users\\admin\\Desktop\\test.png"),
				new File("C:\\Users\\admin\\Desktop\\test.png") };
		Map<String, String> params = new HashMap<String, String>();
		Map<String, File[]> files = new HashMap<String, File[]>();
		files.put("contractFile", fileArray);
		files.put("invoiceFile", fileArray);
		for (int i = 0; i < maps.size(); i++) {
			params = maps.get(i);
			params.put("projectId", "1");
			params.put("contractNo", params.get("contractNo"));
			params.put("dealerName", params.get("dealerName"));
			params.put("dealerAccount", params.get("dealerAccount"));
			params.put("dealerArea", params.get("dealerArea"));
			params.put("lenderAge", params.get("lenderAge"));
			params.put("lenderAccount", params.get("lenderAccount"));
			params.put("loanDate", params.get("loanDate"));
			params.put("invoicePrice", params.get("invoicePrice"));
			params.put("contractPrice", params.get("contractPrice"));
			params.put("firstPayment", params.get("firstPayment"));
			params.put("loanRate", params.get("loanRate"));
			params.put("loanType", params.get("loanType"));
			params.put("loanPeriods", params.get("loanPeriods"));
			params.put("repaymentDate", params.get("repaymentDate"));
			params.put("totalInterest", params.get("totalInterest"));
			params.put("totalPrincipalAndInterest", params.get("totalPrincipalAndInterest"));
			params.put("monthRepayment", params.get("monthRepayment"));
			params.put("carType", params.get("carType"));
			params.put("isNewCar", params.get("isNewCar"));
			params = getSignParams2(params);
			String result = HttpClientUtil.sendPost4FileArray(
					DEV_contractUploadd, files, params,null,null);
			System.out.println("合同上传:" + result);
			test.log(LogStatus.PASS, "验证通过");
			AssertUtil.verifyPass();
		}
	}

	@Test
	public void saveStrategy() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "sdfdsfsdf");
		String result = HttpClientUtil.sendPost4Map(
				"http://dev-pim-admin.baketechfin.com/alarmStrategy/save",params,null,null);
		System.out.println("获取预警策略详情:" + result);
	}

	@Test
	public void login() {
		// 登录接口
		String loginParams = "password=t6%2FhQYro0GPgRD4okeeEclKQPCYn%2BkUEF60qStO7VI3q2ToagWz0DEQ3fRiwlH6iOHakVplbdlCYUuN7N8G2V7HcYWBktbFFyvTfsyjIRHaQOPBm0UEn8wwq9Z2C%2B5rjL3C4ad0Wbw2bm%2F4crevx9mQnqwmG6fTJ4%2B5UKRrGq2c&t=1513662038940&username=13800138000&apiSign=aea0786219fdf2d5838d99e0b4169516";
		String loginResult = HttpUtil.byPost(
				"http://test-api.bakejinfu.com/user/login", null, loginParams);
		JSONObject jo = (JSONObject) JSONObject.parse(loginResult);
		JSONObject data = (JSONObject) JSONObject.parse(jo.getString("data"));
		System.out.println("登录客户经理：" + data.getString("name"));
		AssertUtil.verifyEquals(data.getString("name"), "张鹏");
	}

	@Test
	public void addCustomer() {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, File> files = new HashMap<String, File>();
		params.put("userId", "f4361b8446cd43ed9d27a14a512d1fab");
		params.put("customerTypeId", "f3cf07fa0e61428db4776590092d63e5");
		params.put(
				"data",
				"{\"base_info_company_addr\":\"{\\\"position_doorplate\\\":\\\"\\\",\\\"position_latitude\\\":31.298510605498247,\\\"position_longitude\\\":121.49328052997592,\\\"position_name\\\":\\\"上海市杨浦区五角场街道纪念路8号\\\"}\",\"base_info_company_name\":\"乐百氏\",\"base_info_gender\":\"男\",\"base_info_idcard\":\"120104198901019079\",\"base_info_idcard_photo\":[\"idcard_1.jpg\",\"idcard_2.jpg\"],\"base_info_name\":\"客户2\"}");
		params.put("memo", "测试并发");
		params = getSignParams2(params);
		File file = new File("C:\\Users\\admin\\Desktop\\test.png");
		files.put("left_test.png", file);
		files.put("right_test.png", file);
		String addCustomerRusult = HttpClientUtil.sendPost4File(
				"http://test-api.bakejinfu.com/customer", files, params,null,null);
		System.out.println("添加客户：" + addCustomerRusult);
	}

	@Test
	public void getCustomer() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", "f4361b8446cd43ed9d27a14a512d1fab");
		params.put("customerId", "6ef113f42b8146209637688edf38c151");
		String apiSign = getSignParams(params);
		String getCustomer = HttpUtil
				.byGet("http://test-api.bakejinfu.com/customer?t="
						+ System.currentTimeMillis()
						+ "&apiSign="
						+ apiSign
						+ "&userId=f4361b8446cd43ed9d27a14a512d1fab&customerId=6ef113f42b8146209637688edf38c151",
						null);
		System.out.println("获取客户：" + getCustomer);
	}

	@Test
	public void getPersonBaseInfo() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", "f4361b8446cd43ed9d27a14a512d1fab");
		params.put("personId", "b3595792ff9c4777bdcb3b4238730496");
		String apiSign = getSignParams(params);
		String baseInfo = HttpUtil
				.byGet("http://test-api.bakejinfu.com/customer/person?t="
						+ params.get("t")
						+ "&apiSign="
						+ apiSign
						+ "&personId=b3595792ff9c4777bdcb3b4238730496&userId=f4361b8446cd43ed9d27a14a512d1fab",
						null);
		System.out.println("获取人员基本信息：" + baseInfo);
	}

	@Test
	public void updateCustomerInfo() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", "f4361b8446cd43ed9d27a14a512d1fab");
		params.put("customerId", "6ef113f42b8146209637688edf38c151");
		params.put(
				"data",
				"{\"code\":200,\"data\":{\"idLocked\":\"0\",\"updateDate\":1513667347000,\"star\":\"0\",\"customerTypeCode\":\"5\",\"loanCount\":0,\"permission\":15,\"portrait\":\"[\"group1/M00/00/17/Ch3Fylo4uxKANnBLAAOqXpPf0P8728.jpg\",\"group1/M00/00/17/Ch3Fylo4uxKAIt2oAAOqXpPf0P8162.jpg\"]\",\"title\":\"马超\",\"currentAfterLoan\":false,\"personInfo\":\"{\"base_info_tel\": \"13412341234\", \"base_info_name\": \"马超\", \"base_info_gender\": \"男\", \"base_info_idcard\": \"230404199201017273\", \"base_info_shop_addr\": \"{\"position_doorplate\":\"\",\"position_latitude\":31.298517481118196,\"position_longitude\":121.49324834346768,\"position_name\":\"上海市杨浦区五角场街道纪念路8号\\\"}\", \"base_info_shop_name\": \"西凉军\", \"base_info_shop_photo\": [\"group1/M00/00/17/Ch3Fylo4uxKANnBLAAOqXpPf0P8728.jpg\", \"group1/M00/00/17/Ch3Fylo4uxKAIt2oAAOqXpPf0P8162.jpg\"]}\",\"activityCount\":1,\"riskNotifyCount\":0,\"customerTypeId\":\"1eb28a19c4394da7b056b825bbea72b2\",\"customerTypeName\":\"生产型\",\"createBy\":\"f4361b8446cd43ed9d27a14a512d1fab\",\"updateBy\":\"f4361b8446cd43ed9d27a14a512d1fab\",\"afterLoanCount\":0,\"subtitle\":\"西凉军\",\"personId\":\"b3595792ff9c4777bdcb3b4238730496\",\"id\":\"6ef113f42b8146209637688edf38c151\",\"createDate\":1513667347000},\"success\":true}");
		String apiSign = getSignParams(params);
		String param = "userId=f4361b8446cd43ed9d27a14a512d1fab&t="
				+ params.get("t") + "&apiSign=" + apiSign
				+ "&data={'idLocked':'1'}";
		String baseInfo = HttpUtil.byPost(
				"http://test-api.bakejinfu.com/customer/put", null, param);
		System.out.println("修改客户信息：" + baseInfo);
	}

	private String getSignParams(Map<String, String> params) {
		StringBuffer source = new StringBuffer();
		if (params == null || params.isEmpty()) {
			params = new HashMap<String, String>();
		}
		List<String> invalidValue = new ArrayList<>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (entry.getValue().isEmpty()) {
				invalidValue.add(entry.getKey());
			}
		}
		for (String key : invalidValue) {
			params.remove(key);
		}
		params.put("t", System.currentTimeMillis() + "");
		params.put("apiKey", MD5_KEY);
		Set<String> keys = params.keySet();
		TreeSet<String> set = new TreeSet<>(keys);
		Iterator<String> it = set.iterator();
		String key = it.next();
		source.append(key);
		source.append("=");
		source.append(params.get(key));
		while (it.hasNext()) {
			source.append(",");
			key = it.next();
			source.append(key);
			source.append("=");
			source.append(params.get(key));
		}
		String md5 = MD5Util.getMD5(source.toString());

		return md5;
	}

	private Map<String, String> getSignParams2(Map<String, String> params) {
		StringBuffer source = new StringBuffer();
		if (params == null || params.isEmpty()) {
			params = new HashMap<String, String>();
		}
		List<String> invalidValue = new ArrayList<>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (entry.getValue().isEmpty()) {
				invalidValue.add(entry.getKey());
			}
		}
		for (String key : invalidValue) {
			params.remove(key);
		}
		params.put("t", System.currentTimeMillis() + "");
		params.put("apiKey", MD5_KEY_TH);
		Set<String> keys = params.keySet();
		TreeSet<String> set = new TreeSet<>(keys);
		Iterator<String> it = set.iterator();
		String key = it.next();
		source.append(key);
		source.append("=");
		source.append(params.get(key));
		while (it.hasNext()) {
			source.append(",");
			key = it.next();
			source.append(key);
			source.append("=");
			source.append(params.get(key));
		}
		String md5 = MD5Util.getMD5(source.toString());
		params.put("apiSign", md5);
		params.remove("apiKey");
		params.put("apiSign", md5);
		params.remove("apiKey");
		return params;
	}

}
