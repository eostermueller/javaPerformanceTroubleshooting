package com.github.eostermueller.perfSandbox.dataaccess_4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.eostermueller.perfSandbox.PerfSandboxSingleton;
import com.github.eostermueller.perfSandbox.PerfSandboxException;
import com.github.eostermueller.perfSandbox.dataaccess.PerfSandboxUtil;
import com.github.eostermueller.perfSandbox.model.Account;
import com.github.eostermueller.perfSandbox.model.Accounts;
import com.github.eostermueller.perfSandbox.model.Transaction;


public class AccountMgr4 {
	public AccountMgr4(PerfSandboxSingleton val) {
		this.pgBench = val;
		this.m_sqlTextMgr4.setLogger(val);
		
		this.m_pkInquiry4 = new PkInquiry4(val);
		
	}
	private PerfSandboxSingleton pgBench = null;
	private PkInquiry4 m_pkInquiry4 = null;
//	private ListInquiry m_listInquiry;
		
	public static SqlTextMgr4 m_sqlTextMgr4 = new SqlTextMgr4();
	public Accounts getAccounts(List<Long> randomAccountIds) {
		Accounts accounts = new Accounts();
		for( long accountId : randomAccountIds) {
			try {
				Account a = m_pkInquiry4.getAccount(accountId);
				getAccountHistory(a);
				accounts.addAccount( a );
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (PerfSandboxException e) {
				e.printStackTrace();
			}
		}
		return accounts;
	}
	
	public void getAccountHistory(Account val) throws SQLException, PerfSandboxException {
		val.transactions = getTransactions(val.accountId);
	}
	private List<Transaction> getTransactions(long accountId) throws SQLException, PerfSandboxException  {
		
		Connection con = null; 
		PreparedStatement ps = null; 
		ResultSet rs = null;
		List<Transaction> list = null;
		try {
			con = pgBench.getConnection();
			ps = con.prepareStatement( m_sqlTextMgr4.getHistoryByAccountSql() );
			ps.setLong(1, accountId);
			rs = ps.executeQuery();
			list = new ArrayList<Transaction>();
			while(rs.next()) {
				Transaction t = new Transaction();
				t.tellerId = rs.getInt(1);
				t.historyId = rs.getLong(2);
				t.branchId = rs.getInt(3);
				t.accountId = rs.getInt(4);
				t.delta = rs.getLong(5);
				t.mtime = PerfSandboxUtil.getDate(rs, 6); 
				t.filler = rs.getString(7);
				list.add(t);
			}
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
		return list;
	}

}
