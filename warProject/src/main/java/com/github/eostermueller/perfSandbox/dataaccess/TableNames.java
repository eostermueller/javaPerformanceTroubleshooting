package com.github.eostermueller.perfSandbox.dataaccess;

public class TableNames {
	public String getAccountTable() {
		return getTable("accounts");
	}
	public String getBranchTable() {
		return getTable("branches");
	}
	public String getHistoryTable() {
		return getTable("history");
	}
	public String getTellerTable() {
		return getTable("tellers");
	}
	private String m_schema = null;
	public void setSchema(String val) {
		m_schema = val;
	}

	private String getTable(String tableName) {
		if (getSchema()==null)
			return tableName;
		else
			return getSchema() + "." + tableName;
	}
	private String getSchema() {
		return m_schema;
	}
}
