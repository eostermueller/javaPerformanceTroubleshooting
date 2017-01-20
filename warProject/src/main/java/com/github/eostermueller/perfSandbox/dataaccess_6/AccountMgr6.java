package com.github.eostermueller.perfSandbox.dataaccess_6;

import java.util.List;

import com.github.eostermueller.perfSandbox.PerfSandboxSingleton;
import com.github.eostermueller.perfSandbox.model.Account;
import com.github.eostermueller.perfSandbox.model.Accounts;

public class AccountMgr6 {

	public AccountMgr6(PerfSandboxSingleton perfSandbox) {
		// TODO Auto-generated constructor stub
	}

	public Accounts getAccounts(List<Long> accountIds_criteria) {
		
		Accounts accounts = new Accounts();
		for(Long acctId : accountIds_criteria) {
			accounts.addAccount( Account.createFake(acctId) );
		}
		return accounts;
	}

}
