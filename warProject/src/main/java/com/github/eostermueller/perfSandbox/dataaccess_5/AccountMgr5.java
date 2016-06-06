package com.github.eostermueller.perfSandbox.dataaccess_5;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.eostermueller.perfSandbox.PerfSandboxSingleton;
import com.github.eostermueller.perfSandbox.PerfSandboxException;
import com.github.eostermueller.perfSandbox.model.Account;
import com.github.eostermueller.perfSandbox.model.Accounts;

public class AccountMgr5 {
	public AccountMgr5(PerfSandboxSingleton val) {
		this.pgBench = val;
		this.m_sqlTextMgr5.setLogger(val);
		m_pkInquiry = pgBench.getPkInquiry();
		m_listInquiry = pgBench.getListInquiry();
	}
	private PerfSandboxSingleton pgBench = null;
	private PkInquiry m_pkInquiry;
	private ListInquiry m_listInquiry;
	public static SqlTextMgr5 m_sqlTextMgr5 = new SqlTextMgr5();
	public Accounts getAccounts(List<Long> randomAccountIds) {
		Accounts accounts = new Accounts();
		for( long accountId : randomAccountIds) {
			try {
				Account a = m_pkInquiry.getAccount(accountId);
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
		List<Long> historyIds = m_listInquiry.getTransactions(val.accountId);
		for( long historyId : historyIds) {
			val.transactions.add(m_pkInquiry.getTransaction(historyId));
		}
	}

}
