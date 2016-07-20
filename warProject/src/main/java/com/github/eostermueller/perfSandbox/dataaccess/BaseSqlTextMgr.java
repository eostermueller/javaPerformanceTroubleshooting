package com.github.eostermueller.perfSandbox.dataaccess;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.eostermueller.perfSandbox.Logger;
import com.github.eostermueller.perfSandbox.PerfSandboxSingleton;

public class BaseSqlTextMgr {
	private Logger m_logger;
	public void setLogger(Logger logger) {
		this.m_logger = logger;
	}
	public TableNames getTableNames() {
		return this.m_tableNames;
	}
	protected TableNames m_tableNames = new TableNames();
	@Autowired
	private PerfSandboxSingleton pgBench = null;

       public String getMaxAccountId() {
                return "select max(aid) from " + m_tableNames.getAccountTable();
        }
       public String getMaxBranchId() {
           return "select max(bid) from " + m_tableNames.getBranchTable();
   }


	public String getBranchCount() {
		return "select count(*) from " + m_tableNames.getBranchTable();
	}
	public void logSql(String sql) {
		if (this.m_logger!=null)
			this.m_logger.log(sql);
	}
}
