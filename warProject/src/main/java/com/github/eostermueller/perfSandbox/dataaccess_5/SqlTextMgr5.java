package com.github.eostermueller.perfSandbox.dataaccess_5;

import java.util.concurrent.atomic.AtomicLong;

import com.github.eostermueller.perfSandbox.dataaccess.BaseSqlTextMgr;
import com.github.eostermueller.perfSandbox.dataaccess.TableNames;

public class SqlTextMgr5 extends BaseSqlTextMgr {
	public Stats m_stats = new Stats();
	
	public String getAccountPkInquirySql() {
		this.m_stats.m_accountPkInquirySql.incrementAndGet();
		String sql = "SELECT aid, bid, abalance, filler from " + m_tableNames.getAccountTable() + " WHERE aid = ?";
		logSql(sql);
		return sql;
	}
	public String getHistoryListSql() {
		this.m_stats.m_historyListSql.incrementAndGet();
		String sql =  "SELECT hid from " + m_tableNames.getHistoryTable() + " WHERE aid = ?";
		logSql(sql);
		return sql;
	}
	public String getHistoryPkInquirySql() {
		this.m_stats.m_historyPkInquirySql.incrementAndGet();
		String sql =  "SELECT tid, hid, bid, aid, delta, mtime, filler from " + m_tableNames.getHistoryTable() + " WHERE hid = ?";
		logSql(sql);
		return sql;
	}
	public String getBranchPkInquirySql() {
		this.m_stats.m_branchPkInquirySql.incrementAndGet();
		String sql =  "SELECT  bid, bbalance, filler from " + m_tableNames.getBranchTable() + " WHERE bid = ?";
		logSql(sql);
		return sql;
	}
	
	public String getTellerPkInquiry() {
		this.m_stats.m_tellerPkInquirySql.incrementAndGet();
		String sql = "SELECT  tid, bid, tbalance, filler from " + m_tableNames.getTellerTable() + " WHERE tid = ?";
		logSql(sql);
		return sql;
	}
	
	public static class Stats {
		public AtomicLong m_accountPkInquirySql = new AtomicLong();
		public AtomicLong m_historyListSql = new AtomicLong();
		public AtomicLong m_historyPkInquirySql= new AtomicLong();
		public AtomicLong m_branchPkInquirySql = new AtomicLong();
		public AtomicLong m_tellerPkInquirySql = new AtomicLong();
		public String getXmlStats() {
			StringBuilder sb = new StringBuilder();
			sb.append("<accountPkInquirySql>" + m_accountPkInquirySql  + "</accountPkInquirySql>");
			sb.append("<historyListSql>" + m_historyListSql       + "</historyListSql>");
			sb.append("<historyPkInquirySql>" + m_historyPkInquirySql  + "</historyPkInquirySql>");
			sb.append("<branchPkInquirySql>" + m_branchPkInquirySql   + "</branchPkInquirySql>");
			sb.append("<tellerPkInquirySql>" + m_tellerPkInquirySql   + "</tellerPkInquirySql>"); 
			return sb.toString();
		}
	}
	
}
