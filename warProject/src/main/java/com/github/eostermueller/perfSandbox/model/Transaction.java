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
	
}
