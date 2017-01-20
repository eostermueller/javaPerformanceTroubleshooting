package com.github.eostermueller.perfSandbox.model;

import java.util.Date;

import com.github.eostermueller.perfSandbox.dataaccess.PerfSandboxUtil;

public class Transaction {
//tid  | bid  | aid | delta |           mtime            | filler
	public long historyId = PerfSandboxUtil.UN_INIT;
	public int tellerId = PerfSandboxUtil.UN_INIT;
	public int branchId = PerfSandboxUtil.UN_INIT;
	public long accountId  = PerfSandboxUtil.UN_INIT;
	public long delta  = PerfSandboxUtil.UN_INIT;
	public Date mtime = null;
	public String filler = null;
	
	public Transaction(Transaction template) {
		this.historyId = template.historyId;
		this.tellerId = template.tellerId;
		this.branchId = template.branchId;
		this.accountId = template.accountId;
		this.delta = template.delta;
		this.mtime = new Date(template.mtime.getTime());
		this.filler = new String(template.filler);
		
	}

	public Transaction() {
	}

	public static Transaction createFake(Long acctId) {
		Transaction t = new Transaction();
		
		t.tellerId = 1;
		t.historyId = 2;
		t.branchId = 3;
		t.accountId = acctId;
		t.delta = 8675309;
		t.mtime = new Date();
		t.filler = new String("0123456789012345678912");
		return t;
	}
}
