package com.github.eostermueller.perfSandbox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

public abstract class AbstractBranchInquiry implements BranchInquiry {
	
	private String tableName = null;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	private DataSource dataSource;
	private int inquiryCount;
	private int maxBranchId;
	private AtomicLong totalPhysicalCount = new AtomicLong(0);

	@Override
	public void resetTotalPhysicalCount() {
		this.totalPhysicalCount.set(0);
	}

	@Override
	public String randomBranchInquiries() throws SQLException {
		
		long randomBranchId = -1;
		String filler = null;
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < this.getInquiryCount(); i++) {
			String branchName = this.randomLogicalBranchInquiry();
			sb.append(branchName).append("\n");
		}
		return sb.toString();
	}

	@Override
	abstract public String logicalBranchNameInquiry(long branchId) throws SQLException;

	@Override
	public 
	abstract void shutdown();
	
	@Override
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	@Override
	public DataSource getDataSource() throws SQLException {
		return this.dataSource;
	}

	@Override
	public void setInquiryCount(int val) {
		this.inquiryCount = val;
	}

	@Override
	public void setMaxBranchId(int maxBranchId) {
		this.maxBranchId = maxBranchId;
	}
	@Override
	public int getMaxBranchId() throws SQLException {
		return this.maxBranchId;
	}
	@Override
	public 	int getInquiryCount() {
		return this.inquiryCount;
	}
	@Override
	public
	long getTotalPhysicalCount() {
		return this.totalPhysicalCount.get();
	}
	@Override
	public
	void incrementTotalPhysicalCount() {
		totalPhysicalCount.incrementAndGet();
	}

	public long getRandomBranchId() throws SQLException {
		
		/**
		 * The two data bases will likely have different max branch ids.
		 * If a table has only 1000 branch ids (1-1000 is how the load script is designed)
		 * then querying for branch 1001 would produce and error we want to avoid.
		 */
		
		return (long)ThreadLocalRandom.current().nextDouble(1, getMaxBranchId() );
	}
	
	
	@Override
	public String randomLogicalBranchInquiry() throws SQLException {
		long randomBranchId = this.getRandomBranchId();
		return logicalBranchNameInquiry(randomBranchId);
	}
	@Override
	public String physicalBranchNameInquiry(long branchId) throws SQLException {
		
		Connection conn = getDataSource().getConnection();
		String rc = "<undefined>";
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT FILLER from " + this.getTableName() + " where bid=?" );
			stmt.setLong(1, branchId);
			rs = stmt.executeQuery();
			rs.next();
			rc = rs.getString(1);
			this.incrementTotalPhysicalCount();
		} finally {
			if (rs!=null) rs.close();
			if (stmt!=null) stmt.close();
			if (conn!=null) conn.close();
		}
		return rc;
	}

}
