package com.github.eostermueller.perfSandbox.dataaccess_2;

import java.util.concurrent.atomic.AtomicLong;

import com.github.eostermueller.perfSandbox.dataaccess.BaseSqlTextMgr;
import com.github.eostermueller.perfSandbox.dataaccess.TableNames;

public class SqlTextMgr2 extends BaseSqlTextMgr {
	public Stats m_stats = new Stats();
	
	public String getMultipleAccountsSql(int numAccountCriteria) {
		this.m_stats.m_accountAndHistorySql.incrementAndGet();
		StringBuilder sb = new StringBuilder();
		sb.append( "SELECT a.aid, a.bid, a.abalance, a.filler FROM "
				+ m_tableNames.getAccountTable() 
				+ " a WHERE aid in (");
		
		for(int i =0; i< numAccountCriteria; i++) {
			if (i > 0) sb.append(",");
			sb.append("?");
		}
		sb.append(")");
		logSql(sb.toString());

		return sb.toString();		
	}
	
	public String getHistorySql(int numAccountCriteria) {
		this.m_stats.m_accountAndHistorySql.incrementAndGet();
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT tid , hid, bid , aid , delta , mtime , filler  FROM "); 
		sb.append(m_tableNames.getHistoryTable());  
		sb.append(" WHERE aid in (");
		
		for(int i =0; i< numAccountCriteria; i++) {
			if (i > 0) sb.append(",");
			sb.append("?");
		}
		sb.append(") ORDER BY aid, hid");
		logSql(sb.toString());

		return sb.toString();		
	}
	
	public static class Stats {
		public AtomicLong m_accountAndHistorySql = new AtomicLong(0);
		public AtomicLong m_historyByAccountSql = new AtomicLong(0);
		public String getXmlStats() {
			StringBuilder sb = new StringBuilder();
			sb.append("<accountAndHistorySql>" + m_accountAndHistorySql  + "</accountAndHistorySql>");
			sb.append("<historyByAccountSql>" + this.m_historyByAccountSql + "</historyByAccountSql>");
			return sb.toString();
		}
	
	}

	
}
