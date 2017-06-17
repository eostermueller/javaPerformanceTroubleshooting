package com.github.eostermueller.perfSandbox.dataaccess_1;

import java.util.concurrent.atomic.AtomicLong;

import com.github.eostermueller.perfSandbox.dataaccess.BaseSqlTextMgr;
import com.github.eostermueller.perfSandbox.dataaccess.TableNames;

public class SqlTextMgr1 extends BaseSqlTextMgr {
	public Stats m_stats = new Stats();
	
	public String getAccountAndHistorySql(int numAccountCriteria) {
		this.m_stats.m_accountAndHistorySql.incrementAndGet();
		StringBuilder sb = new StringBuilder();
//		sb.append( "SELECT a.aid, a.bid, a.abalance, a.filler, "
//				+ "h.tid, h.hid, h.delta, h.mtime, h.filler FROM " 
//				+ m_tableNames.getAccountTable() + " a, "
//				+ m_tableNames.getHistoryTable() + " h " 
//				+ "WHERE a.aid = h.aid AND a.aid in (");
		
		sb.append( "SELECT A.AID, A.BID, A.ABALANCE, A.FILLER, a.FILLER02, A.FILLER02, A.FILLER03,A.FILLER04,A.FILLER05,A.FILLER06,A.FILLER07,A.FILLER08,A.FILLER09,A.FILLER10,A.FILLER11,A.FILLER12,A.FILLER13,A.FILLER14,A.FILLER15,A.FILLER16,A.FILLER17,A.FILLER18,A.FILLER19,A.FILLER20,"
		+ "h.tid, h.hid, h.delta, h.mtime, h.filler FROM " 
		+ m_tableNames.getAccountTable() + " A LEFT OUTER JOIN "
		+ m_tableNames.getHistoryTable() + " h ON a.aid = h.aid ");
		
		if (numAccountCriteria > 0 ) {
			sb.append("WHERE a.aid in (");
			for(int i =0; i< numAccountCriteria; i++) {
				if (i > 0) sb.append(",");
				sb.append("?");
			}
			sb.append(") ");			
		}
		sb.append("ORDER BY a.aid, h.mtime desc");
			
		logSql(sb.toString());
		return sb.toString();		
	}
	
	public static class Stats {
		public AtomicLong m_accountAndHistorySql = new AtomicLong(0);
		public String getXmlStats() {
			StringBuilder sb = new StringBuilder();
			sb.append("<accountAndHistorySql>" + m_accountAndHistorySql  + "</accountAndHistorySql>");
			return sb.toString();
		}
	
	}
	
}
