package com.github.eostermueller.perfSandbox.model;

import java.util.ArrayList;
import java.util.List;

public class Account {
	public long accountId = 0;
	public int branchId = 0;
	public long balance = 0;
	public String filler = null;
	public List<Transaction> transactions = new ArrayList<Transaction>();
}
