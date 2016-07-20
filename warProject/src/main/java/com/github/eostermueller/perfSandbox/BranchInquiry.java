package com.github.eostermueller.perfSandbox;

import java.sql.SQLException;

import javax.sql.DataSource;


public interface BranchInquiry {
	String randomBranchInquiries() throws SQLException;
	void setDataSource(DataSource dataSource);
	DataSource getDataSource() throws SQLException;
	String randomLogicalBranchInquiry() throws SQLException;
	void setMaxBranchId(int maxBranchId);
	int getMaxBranchId() throws SQLException;
	void setInquiryCount(int val);
	int getInquiryCount();
	String getTableName();
	void setTableName(String tableName);
	long getTotalPhysicalCount();
	void resetTotalPhysicalCount();
	void incrementTotalPhysicalCount();
	String physicalBranchNameInquiry(long branchId) throws SQLException;
	String logicalBranchNameInquiry(long branchId) throws SQLException;
	void shutdown();
}
