package com.test.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


public class DbUtil {
	private static SqlSessionFactory ssf;
	private static InputStream is;
	static {
		try {
			String resource = "sqlMapConfig.xml";
			is = Resources.getResourceAsStream(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ssf = new SqlSessionFactoryBuilder().build(is);
	}
	
	public static void insertDB(List<?> rlist) {
		SqlSession session = ssf.openSession();
		for (int i = 0; i < rlist.size(); i++) {
			session.insert("testreport.insert", rlist.get(i));
			session.commit();
		}
		session.close();
	}
	
	@SuppressWarnings("rawtypes")
	public static List<?> selectALL() {
		SqlSession session = ssf.openSession();
		List<?> rlist = new ArrayList();
		rlist = session.selectList("testreport.findAll");
		session.close();
		return rlist;
	}

}
