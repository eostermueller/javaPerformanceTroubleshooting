package com.github.eostermueller.perfSandbox.dataaccess_3;

import java.util.concurrent.atomic.AtomicLong;

import com.github.eostermueller.perfSandbox.dataaccess.BaseSqlTextMgr;
import com.github.eostermueller.perfSandbox.dataaccess.TableNames;

public class SqlTextMgr3 extends BaseSqlTextMgr {
	public Stats m_stats = new Stats();
	
	public String getMultipleAccountsSql(int numAccountCriteria) {
		this.m_stats.m_accountAndHistorySql.incrementAndGet();
		StringBuilder sb = new StringBuilder();
		sb.append( "SELECT a.aid, a.bid, a.abalance, a.filler, a.filler01, a.filler02, a.filler03, a.filler04, a.filler05, a.filler06, a.filler07, a.filler08, a.filler09, a.filler10, a.filler11, a.filler12, a.filler13, a.filler14, a.filler15, a.filler16, a.filler17, a.filler18, a.filler19, a.filler20 FROM "
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
	
	public String getHistoryByAccountSql() {
		 
		this.m_stats.m_historyByAccountSql.incrementAndGet();
		String sql = "SELECT tid, hid, bid, aid, delta, mtime, filler from " + m_tableNames.getHistoryTable() + " WHERE aid = ?";
		logSql(sql);

		return sql;
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
