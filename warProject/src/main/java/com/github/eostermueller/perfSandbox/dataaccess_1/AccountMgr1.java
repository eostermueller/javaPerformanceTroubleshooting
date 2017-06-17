package com.github.eostermueller.perfSandbox.dataaccess_1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.eostermueller.perfSandbox.PerfSandboxSingleton;
import com.github.eostermueller.perfSandbox.PerfSandboxException;
import com.github.eostermueller.perfSandbox.dataaccess.BaseManager;
import com.github.eostermueller.perfSandbox.dataaccess.PerfSandboxUtil;
import com.github.eostermueller.perfSandbox.model.Account;
import com.github.eostermueller.perfSandbox.model.Accounts;
import com.github.eostermueller.perfSandbox.model.Transaction;

@Service
public class AccountMgr1 extends BaseManager {
	@Autowired
	public AccountMgr1(PerfSandboxSingleton val) {
		this.pgBench = val;
		this.m_sqlTextMgr1.setLogger(val);
	}
	public static SqlTextMgr1 m_sqlTextMgr1 = new SqlTextMgr1();
	private final PerfSandboxSingleton pgBench;
	public PerfSandboxSingleton getPgBench() {
		return pgBench;
	}

	public Accounts getAllAccountsAndCrashJvm() throws PerfSandboxException   {
		int NUM_CRITERIA = 0; // means do not add WHERE clauses, select all rows in table, crash JVM.
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Accounts accounts = new Accounts();
		try {
			con = getPgBench().getConnection();
			ps = con.prepareStatement( m_sqlTextMgr1.getAccountAndHistorySql(NUM_CRITERIA) );
			ps.setFetchSize(1000);//2.5million rows total, get 1k rows at a time.
			rs = ps.executeQuery();
			
			while(rs.next()) {
				getAccountAndTxData( rs, accounts );
			}
		} catch (SQLException e) {
			throw new PerfSandboxException(e);
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
		return accounts;
	}

	public Accounts getAccounts(List<Long> accountIdsCriteria) throws PerfSandboxException   {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Accounts accounts = new Accounts();
		try {
			con = getPgBench().getConnection();
			ps = con.prepareStatement( m_sqlTextMgr1.getAccountAndHistorySql(accountIdsCriteria.size()) );
			ps.setFetchSize(200);
			for (int i = 1; i <= accountIdsCriteria.size(); i++) {
				ps.setLong(i, accountIdsCriteria.get(i-1).longValue());
			}
			rs = ps.executeQuery();
			short count = 0;
			
			while(rs.next()) {
				getAccountAndTxData( rs, accounts );
			}
		} catch (SQLException e) {
			throw new PerfSandboxException(e);
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
		return accounts;
	}

	private void getAccountAndTxData(ResultSet rs, Accounts accounts) throws SQLException {
		long accountId = rs.getLong(1);
		Account account = accounts.findAccount(accountId);
		if (account==null) {
			account = createAccount(rs);
			accounts.addAccount(account);
		}
		Transaction t = createTransactions(rs);
		account.transactions.add(t);
	}

	private Transaction createTransactions(ResultSet rs) throws SQLException {
		Transaction t = new Transaction();
		t.accountId = rs.getLong(1);
		t.branchId = rs.getInt(2);
		t.tellerId = rs.getInt(5+20);
		t.historyId = rs.getLong(6+20);
		t.delta = rs.getLong(7+20);
		t.mtime = PerfSandboxUtil.getDate(rs, 8+20);
		t.filler = rs.getString(9+20);
		return t;
	}

	private Account createAccount(ResultSet rs) throws SQLException {
		Account a = new Account();
		a.accountId = rs.getLong(1);
		a.branchId = rs.getInt(2);
		a.balance = rs.getLong(3);
		a.filler = rs.getString(4);
		a.filler01 = rs.getString(5);
		a.filler02= rs.getString(6);
		a.filler03 = rs.getString(7);
		a.filler04 = rs.getString(8);
		a.filler05 = rs.getString(9);
		a.filler06 = rs.getString(10);
		a.filler07 = rs.getString(11);
		a.filler08 = rs.getString(12);
		a.filler09 = rs.getString(13);
		a.filler10 = rs.getString(14);
		a.filler11 = rs.getString(15);
		a.filler12 = rs.getString(16);
		a.filler13 = rs.getString(17);
		a.filler14 = rs.getString(18);
		a.filler15 = rs.getString(19);
		a.filler16 = rs.getString(20);
		a.filler17 = rs.getString(21);
		a.filler18 = rs.getString(22);
		a.filler19 = rs.getString(23);
		a.filler20 = rs.getString(24);
		return a;
	}

}
