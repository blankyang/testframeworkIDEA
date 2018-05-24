package cn.test.framework.tests;
//package cn.yxb.maven.maventest;
//
//import java.io.InputStream;
//import java.util.List;
//
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//import cn.yxb.maven.maventest.domain.TestReport;
//
//public class MybatisTest {
//	
//	private SqlSessionFactory ssf;
//	
//	@BeforeClass
//	public void initFactory() throws Exception{
//		String resource = "sqlMapConfig.xml";
//		InputStream is = Resources.getResourceAsStream(resource);
//		ssf = new SqlSessionFactoryBuilder().build(is);
//	}
//	
//	@Test
//	public void testFindAll(){
//		SqlSession session = ssf.openSession();
//		List<TestReport> rlist = session.selectList("testreport.findAll");
//		System.out.println(rlist.size());
//		
//	}
//	@Test
//	public void testInsert(){
//		SqlSession session = ssf.openSession();
//		TestReport tr = new TestReport();
//		tr.setName("666");
//		tr.setTotal("666");
//		tr.setSkipped("666");
//		int i = session.insert("testreport.insert",tr);//不要写错namespace
//		session.commit();
//		System.out.println(i);
//	}
//}
