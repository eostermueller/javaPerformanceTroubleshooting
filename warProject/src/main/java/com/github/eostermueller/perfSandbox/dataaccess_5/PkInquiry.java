package com.github.eostermueller.perfSandbox.dataaccess_5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.eostermueller.perfSandbox.PerfSandboxSingleton;
import com.github.eostermueller.perfSandbox.PerfSandboxException;
import com.github.eostermueller.perfSandbox.dataaccess.PerfSandboxUtil;
import com.github.eostermueller.perfSandbox.model.Account;
import com.github.eostermueller.perfSandbox.model.Accounts;
import com.github.eostermueller.perfSandbox.model.Branch;
import com.github.eostermueller.perfSandbox.model.Transaction;

public class PkInquiry {
	public PkInquiry(PerfSandboxSingleton val) {
		this.pgBench = val;
	}
	private PerfSandboxSingleton pgBench;
	public Account getAccount(long accountId) throws SQLException, PerfSandboxException {
		Account account = new Account();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = pgBench.getConnection();
			ps = con.prepareStatement( AccountMgr5.m_sqlTextMgr5.getAccountPkInquirySql() );
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
	public Branch getBranch(int branchId) throws SQLException, PerfSandboxException {
		Branch branch = new Branch();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = pgBench.getConnection();
			ps = con.prepareStatement( AccountMgr5.m_sqlTextMgr5.getBranchPkInquirySql() );
			ps.setInt(1, branchId);
			rs = ps.executeQuery();
			short count = 0;
			while(rs.next()) {
				if (++count >1)
					throw new PerfSandboxException("Expecting only a single account record, but found at least 2 for accountId [" + branchId + "]");
				branch.bid = rs.getInt(1);
				branch.bbalance = rs.getLong(2);
				branch.filler = rs.getString(3);
			}
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
		return branch;
	}
	public Transaction getTransaction(long transactionId) throws SQLException, PerfSandboxException {
		Transaction transaction = new Transaction();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = pgBench.getConnection();
			ps = con.prepareStatement( AccountMgr5.m_sqlTextMgr5.getHistoryPkInquirySql() );
			ps.setLong(1, transactionId);
			rs = ps.executeQuery();
			short count = 0;
			while(rs.next()) {
				if (++count >1)
					throw new PerfSandboxException("Expecting only a single account record, but found at least 2 for accountId [" + transactionId + "]");
				//		return "SELECT tid, hid, bid, aid, delta, mtime, filler from " + m_tableNames.getHistoryTable() + " WHERE tid = ?";
				transaction.tellerId = rs.getInt(1);
				transaction.historyId = rs.getLong(2);
				transaction.branchId = rs.getInt(3);
				transaction.accountId = rs.getLong(4);
				transaction.delta = rs.getLong(5);
				transaction.mtime = PerfSandboxUtil.getDate(rs, 6);
				transaction.filler = rs.getString(7);
			}
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
			return transaction;
		}
}
