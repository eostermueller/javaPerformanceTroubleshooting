package com.github.eostermueller.perfSandbox;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.eclipse.emf.ecore.xmi.XMLParserPool;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.github.eostermueller.perfSandbox.cache.UncachedBranchInquiryImpl;

public class UncachedBranchInquiryTest {
	private static final int INQUIRY_COUNT = 5;
	private static final int MAX_BRANCH_COUNT = 100;
	static BranchInquiry branchInquiry = null;
	private static String JDBC_URL ="jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
	private static DataSource dataSource = null;
	private AtomicInteger physicalBranchSelectCount = new AtomicInteger(0);
	public static DataSource getDataSource() {
		return dataSource;
	}	
	
	@BeforeClass 
	public static void setup() throws SQLException {
		
		dbSetup();
		
		branchInquiry = new UncachedBranchInquiryImpl();
		branchInquiry.setDataSource( getDataSource() );
		branchInquiry.setInquiryCount(INQUIRY_COUNT);
		branchInquiry.setTableName("BRANCHES");
		branchInquiry.setMaxBranchId(MAX_BRANCH_COUNT);
		
	}
	@AfterClass 
	public static void dbShutdown() throws SQLException {
		getDataSource().getConnection().createStatement().executeUpdate("DROP TABLE BRANCHES");
		getDataSource().getConnection().close();
	}
	
	private static void dbSetup() throws SQLException {
		 dataSource = JdbcConnectionPool.create(JDBC_URL, "user", "password");
			Connection conn = getDataSource().getConnection();
			 
			 conn.createStatement().executeUpdate("CREATE TABLE BRANCHES ("
		        + "bid int not null,"
		        + "bbalance int,"
		        + "filler char(88));");

			 for (int i = 1; i <= MAX_BRANCH_COUNT; i++) {
				 String sql = "INSERT INTO BRANCHES (BID, BBALANCE, FILLER ) values ("+i+", "+i+",'"+i+"x"+i+"x"+i+"x"+i+"')";
				 System.out.println(sql);
				 conn.createStatement().executeUpdate(sql);
			 }
	}
	@Test
	public void canMakePhysicalReadForTwoConsecutiveRequests() throws SQLException {
		//
		branchInquiry.resetTotalPhysicalCount();
		assertEquals("sanity check failed", 0, branchInquiry.getTotalPhysicalCount());
		
		/**
		 *   F  I  R  S  T      T   I   M   E
		 */
		String branchName = branchInquiry.logicalBranchNameInquiry(95);
		
		assertEquals("Lookup to db did not find correct branchName/filler","95x95x95x95", branchName);
		assertEquals("Just executed a single SQL, but didn't tally the count correctly", 
				1, branchInquiry.getTotalPhysicalCount());

		/**
		 *   S  E  C  O  N  D      T   I   M   E
		 */
		branchName = branchInquiry.logicalBranchNameInquiry(95);
		
		assertEquals("Lookup to db did not find correct branchName/filler","95x95x95x95", branchName);
		// asserting that no caching is taking place here.
		assertEquals("Just executed a second SQL, but didn't tally the count correctly", 
				2, branchInquiry.getTotalPhysicalCount());
		
	}
	
	@Test
	public void canSelectData() throws SQLException {
		
		branchInquiry.resetTotalPhysicalCount();
		branchInquiry.randomBranchInquiries();
		
		assertEquals("Did not make physical call to DB the expected number of times", 
				INQUIRY_COUNT, 
				branchInquiry.getTotalPhysicalCount());
	}
}
