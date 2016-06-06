package com.github.eostermueller.perfSandbox.dataaccess_4;

import java.util.concurrent.atomic.AtomicLong;

import com.github.eostermueller.perfSandbox.dataaccess.BaseSqlTextMgr;
import com.github.eostermueller.perfSandbox.dataaccess.TableNames;

public class SqlTextMgr4 extends BaseSqlTextMgr {
	public Stats m_stats = new Stats();
	
	public String getHistoryByAccountSql() {
		this.m_stats.m_historyByAccountSql.incrementAndGet();
		String sql ="SELECT tid, hid, bid, aid, delta, mtime, filler from " + m_tableNames.getHistoryTable() + " WHERE aid = ?";
		logSql(sql);
		return sql;
	}
	public String getAccountPkInquirySql() {
		this.m_stats.m_accountPkInquirySql.incrementAndGet();
		String sql = "SELECT aid, bid, abalance, filler from " + m_tableNames.getAccountTable() + " WHERE aid = ?";
		logSql(sql);
		return sql;
	}
	
	public static class Stats {
		public AtomicLong m_accountPkInquirySql = new AtomicLong();
		public AtomicLong m_historyByAccountSql = new AtomicLong();
		public String getXmlStats() {
			StringBuilder sb = new StringBuilder();
			sb.append("<historyByAccountSql>" + m_historyByAccountSql  + "</historyByAccountSql>");
			sb.append("<accountPkInqSql>" + m_accountPkInquirySql  + "</accountPkInqSql>");
			return sb.toString();
		}
	
	}
	
}
