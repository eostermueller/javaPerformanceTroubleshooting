package com.github.eostermueller.perfSandbox.model;

import java.util.ArrayList;
import java.util.List;

public class Account {
	public long accountId = 0;
	public int branchId = 0;
	public long balance = 0;
	public String filler = null;
	public String filler01 = null;
	public String filler02 = null;
	public String filler03 = null;
	public String filler04 = null;
	public String filler05 = null;
	public String filler06 = null;
	public String filler07 = null;
	public String filler08 = null;
	public String filler09 = null;
	public String filler10 = null;
	public String filler11 = null;
	public String filler12 = null;
	public String filler13 = null;
	public String filler14 = null;
	public String filler15 = null;
	public String filler16 = null;
	public String filler17 = null;
	public String filler18 = null;
	public String filler19 = null;
	public String filler20 = null;
	public List<Transaction> transactions = new ArrayList<Transaction>();
	
	public Account(Account template) {
		this.accountId = template.accountId;
		this.branchId = template.branchId;
		this.balance = template.balance;
		this.filler = new String(template.filler);
		this.filler01 = new String(template.filler01);
		this.filler02 = new String(template.filler02);
		this.filler03 = new String(template.filler03);
		this.filler04 = new String(template.filler04);
		this.filler05 = new String(template.filler05);
		this.filler06 = new String(template.filler06);
		this.filler07 = new String(template.filler07);
		this.filler08 = new String(template.filler08);
		this.filler09 = new String(template.filler09);
		this.filler10 = new String(template.filler10);
		this.filler11 = new String(template.filler11);
		this.filler12 = new String(template.filler12);
		this.filler13 = new String(template.filler13);
		this.filler14 = new String(template.filler14);
		this.filler15 = new String(template.filler15);
		this.filler16 = new String(template.filler16);
		this.filler17 = new String(template.filler17);
		this.filler18 = new String(template.filler18);
		this.filler19 = new String(template.filler19);
		this.filler20 = new String(template.filler20);
		
		for(Transaction t : template.transactions) {
			this.transactions.add( new Transaction(t) );
		}
		
	}

	public Account() {
		// TODO Auto-generated constructor stub
	}

	public static Account createFake(Long acctId) {
		Account a = new Account();
		a.accountId = acctId;
		a.branchId = 1;
		a.balance = 8675309;
		a.filler = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler01 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler02 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler03 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler04 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler05 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler06 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler07 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler08 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler09 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler10 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler11 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler12 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler13 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler14 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler15 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler16 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler17 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler18 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler19 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		a.filler20 = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		for(int i = 0; i < 50; i++)
			a.transactions.add( Transaction.createFake( acctId ) );
		
		return a;
	}
}
