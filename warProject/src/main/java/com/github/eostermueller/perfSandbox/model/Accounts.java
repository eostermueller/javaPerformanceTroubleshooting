package com.github.eostermueller.perfSandbox.model;

import java.util.ArrayList;
import java.util.List;

public class Accounts {
	private List<Account> m_list = new ArrayList<Account>();
	public void addAccount(Account val) {
		m_list.add(val);
	}
	public List<Account> getAccounts() {
		return m_list;
	}
	public Account findAccount(long accountIdCriteria) {
		for(Account a : m_list) {
			if (a.accountId==accountIdCriteria)
				return a;
		}
		return null;
	}
}
