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
				account.filler01 = rs.getString(5);
				account.filler02 = rs.getString(6);
				account.filler03 = rs.getString(7);
				account.filler04 = rs.getString(8);
				account.filler05 = rs.getString(9);
				account.filler06 = rs.getString(10);
				account.filler07 = rs.getString(11);
				account.filler08 = rs.getString(12);
				account.filler09 = rs.getString(13);
				account.filler10 = rs.getString(14);
				account.filler11 = rs.getString(15);
				account.filler12 = rs.getString(16);
				account.filler13 = rs.getString(17);
				account.filler14 = rs.getString(18);
				account.filler15 = rs.getString(19);
				account.filler16 = rs.getString(20);
				account.filler17 = rs.getString(21);
				account.filler18 = rs.getString(22);
				account.filler19 = rs.getString(23);
				account.filler20 = rs.getString(24);
			}
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
		return account;
	}
}
