package com.github.eostermueller.perfSandbox.dataaccess_4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.github.eostermueller.perfSandbox.PerfSandboxSingleton;
import com.github.eostermueller.perfSandbox.PerfSandboxException;
import com.github.eostermueller.perfSandbox.dataaccess.PerfSandboxUtil;
import com.github.eostermueller.perfSandbox.model.Account;
import com.github.eostermueller.perfSandbox.model.Branch;
import com.github.eostermueller.perfSandbox.model.Transaction;


public class PkInquiry4 {
	public PkInquiry4(PerfSandboxSingleton val) {
		this.pgBench = val;
	}
	private PerfSandboxSingleton pgBench = null;
	public Account getAccount(long accountId) throws SQLException, PerfSandboxException {
		Account account = new Account();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = pgBench.getConnection();
			ps = con.prepareStatement( AccountMgr4.m_sqlTextMgr4.getAccountPkInquirySql() );
			ps.setLong(1, accountId);
			
			rs = ps.executeQuery();
			short count = 0;
			while(rs.next()) {
				if (++count >1)
					throw new PerfSandboxException("Expecting only a single account record, but found at least 2 for accountId [" + accountId + "]");
				account.accountId = rs.getLong(1);
				account.branchId = rs.getInt(2);
				account.balance = rs.getLong(3);
				account.filler = rs.getString(4);
			}
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
		return account;
	}
}
