package com.github.eostermueller.perfSandbox.dataaccess_5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.eostermueller.perfSandbox.PerfSandboxSingleton;
import com.github.eostermueller.perfSandbox.PerfSandboxException;
import com.github.eostermueller.perfSandbox.dataaccess.PerfSandboxUtil;
import com.github.eostermueller.perfSandbox.model.Transaction;

public class ListInquiry {

	public ListInquiry(PerfSandboxSingleton val) {
		this.pgBench = val;
	}
	PerfSandboxSingleton pgBench = null;
	public List<Long> getTransactions(long accountId) throws SQLException, PerfSandboxException  {
		
		Connection con = null; 
		PreparedStatement ps = null; 
		ResultSet rs = null;
		List<Long> list = null;
		try {
			con = pgBench.getConnection();
			ps = con.prepareStatement( AccountMgr5.m_sqlTextMgr5.getHistoryListSql() );
			ps.setLong(1, accountId);
			rs = ps.executeQuery();
			list = new ArrayList<Long>();
			while(rs.next()) {
				list.add ( rs.getLong(1));
			}
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
		return list;
	}
		
}
