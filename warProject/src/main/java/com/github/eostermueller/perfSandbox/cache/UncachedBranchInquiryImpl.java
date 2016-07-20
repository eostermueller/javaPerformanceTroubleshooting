package com.github.eostermueller.perfSandbox.cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.eostermueller.perfSandbox.AbstractBranchInquiry;
import com.github.eostermueller.perfSandbox.BranchInquiry;

public class UncachedBranchInquiryImpl extends AbstractBranchInquiry implements
		BranchInquiry {

	/**
	 * With this uncached implementation, physical lookup is the best we can do for a logical lookup.
	 * @throws SQLException 
	 */
	@Override
	public String logicalBranchNameInquiry(long branchId) throws SQLException {
		
		return this.physicalBranchNameInquiry(branchId);
	}

	@Override
	public void shutdown() {
		//intentional no-op, no caching manager to shutdown.
	}



}
