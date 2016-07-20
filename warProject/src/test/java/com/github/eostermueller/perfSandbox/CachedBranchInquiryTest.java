package com.github.eostermueller.perfSandbox;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.eostermueller.perfSandbox.cache.CachedBranchInquiryImpl;

public class CachedBranchInquiryTest {
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
		
		branchInquiry = new CachedBranchInquiryImpl();
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
	public void cannotMakePhysicalReadForTwoConsecutiveRequests() throws SQLException {
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

		// asserting that caching is taking place
		assertEquals("Just made a second logical read, and the read unexpectedly went to disk!", 
				1, branchInquiry.getTotalPhysicalCount());
		
	}
}
