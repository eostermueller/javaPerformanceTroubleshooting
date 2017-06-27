package com.github.eostermueller.perfSandbox.model;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

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
		
		t.tellerId = ThreadLocalRandom.current().nextInt(5000);
		t.historyId = ThreadLocalRandom.current().nextInt(2500000);
		t.branchId = ThreadLocalRandom.current().nextInt(1000);;
		t.accountId = acctId;
		t.delta = ThreadLocalRandom.current().nextInt(200000);
		t.mtime = new Date();
		t.filler = getRandomNineteenDigitString();
		return t;
	}
	public static String getRandomNineteenDigitString() {
		///these #'s approach max long, but not quite there.
		long myLong = ThreadLocalRandom.current().nextLong(1223372036854775807L,9223372036854775807L );
		return Long.toString(myLong);
	}
	
}
