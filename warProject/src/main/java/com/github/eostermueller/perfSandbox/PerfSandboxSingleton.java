package com.github.eostermueller.perfSandbox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import com.github.eostermueller.perfSandbox.cache.CachedBranchInquiryImpl;
import com.github.eostermueller.perfSandbox.cache.UncachedBranchInquiryImpl;
import com.github.eostermueller.perfSandbox.dataaccess.BaseSqlTextMgr;
import com.github.eostermueller.perfSandbox.dataaccess.PerfSandboxUtil;
import com.github.eostermueller.perfSandbox.dataaccess_5.ListInquiry;
import com.github.eostermueller.perfSandbox.dataaccess_5.PkInquiry;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariConfig;

@Component
@Scope("prototype")
public class PerfSandboxSingleton implements ApplicationListener<ContextRefreshedEvent>, Logger {
	private int db = 1;
	private HttpServer httpServer = new HttpServer();
	public HttpServer getHttpServer() {
		return this.httpServer;
	}
	private BranchInquiry branchInquiry = null;
	public AtomicBoolean backendStarted = new AtomicBoolean(false);
	public PerfSandboxSingleton() {
		
		try {
			init();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		m_pkInquiry_3 = new PkInquiry(this);
		m_listInquiry_3 = new ListInquiry(this); 
	}
	@PostConstruct
	private void init() throws SQLException {
		this.initDataSources();
		
		try {
			//Execute 1 time at startup
			long maxAccountId_01 = queryForMaxAccountId(this.dataSource01.getConnection());
			long maxAccountId_02 = queryForMaxAccountId(this.dataSource02.getConnection());
			this.setMaxAccountId_01( maxAccountId_01 );
			this.setMaxAccountId_02( maxAccountId_02 );
			long maxBranchId_01 = queryForMaxBranchId(this.dataSource01.getConnection());
			long maxBranchId_02 = queryForMaxBranchId(this.dataSource02.getConnection());
			this.setMaxBranchId_01( maxBranchId_01 );
			this.setMaxBranchId_02( maxBranchId_02 );
		} catch (PerfSandboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private long queryForMaxBranchId(Connection con) throws PerfSandboxException {
		BaseSqlTextMgr bstm = new BaseSqlTextMgr();
		PreparedStatement ps = null;
		ResultSet rs = null;
		long countOfBranches = -1L;
		try {
			ps = con.prepareStatement( bstm.getMaxBranchId() );
			rs = ps.executeQuery();
			rs.next();
			countOfBranches = rs.getLong(1);
			
		} catch (SQLException e) {
			throw new PerfSandboxException(e);
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
		return countOfBranches;		
	}
	/**
	 * 
	 * @return
	 * @throws PerfSandboxException
	 */
	public long queryForMaxAccountId(Connection con) throws PerfSandboxException {
		BaseSqlTextMgr bstm = new BaseSqlTextMgr();
		PreparedStatement ps = null;
		ResultSet rs = null;
		long countOfAccounts = -1L;
		try {
			ps = con.prepareStatement( bstm.getMaxAccountId() );
			rs = ps.executeQuery();
			rs.next();
			countOfAccounts = rs.getLong(1);
			
		} catch (SQLException e) {
			throw new PerfSandboxException(e);
		} finally {
			PerfSandboxUtil.closeQuietly(rs);
			PerfSandboxUtil.closeQuietly(ps);
			PerfSandboxUtil.closeQuietly(con);
		}
		return countOfAccounts;		
	}
	
	
	private AtomicLong m_maxAccountId_01 = new AtomicLong(20);
	private AtomicLong m_maxAccountId_02 = new AtomicLong(20);
	private AtomicLong m_maxBranchId_01 = new AtomicLong(20);
	private AtomicLong m_maxBranchId_02 = new AtomicLong(20);

	private PkInquiry m_pkInquiry_3 = null;

	private ListInquiry m_listInquiry_3 = null;

	private AtomicInteger m_numScenario = new AtomicInteger(1);
	private AtomicInteger m_numBranchScenario = new AtomicInteger(0);
	private AtomicBoolean m_logSql = new AtomicBoolean(false);
	
	private void initDataSources() {
	    HikariConfig config = new HikariConfig("/hikari01.properties");
 	    this.dataSource01 = new HikariDataSource(config);
	    log("Hikari JdbcUrl 01 [" + config.getJdbcUrl() + "]");

	    config = new HikariConfig("/hikari02.properties");
 	    this.dataSource02 = new HikariDataSource(config);
	    log("Hikari JdbcUrl 02 [" + config.getJdbcUrl() + "]");
	}
 	@Bean(destroyMethod = "close")
	public DataSource getDataSource() throws SQLException {

	    DataSource rc = null;
	    if (this.getDb()==1)
	    	rc = this.dataSource01;
	    else if (this.getDb()==2)
	    	rc = this.dataSource02;
	    
	    return rc;
	}
	public int getMaxBranchId() throws SQLException {
		int rc = -1;
	    if (this.getDb()==1)
	    	rc = this.getMaxBranchId_01();
	    else if (this.getDb()==2)
	    	rc = this.getMaxBranchId_02();
	    
	    return rc;
	}
 	DataSource dataSource01 = null;
 	DataSource dataSource02 = null;
	private Integer backendPort = 8674;


	public Connection getConnection() throws SQLException, PerfSandboxException {
		Connection c = null;
		if (this.db==1)
			c = this.dataSource01.getConnection();
		else if (this.db==2)
			c = this.dataSource02.getConnection();
		else throw new PerfSandboxException("A database number [" + this.db + "] is not supported.  Only types 1 or 2.");
		
		return c;	
	}
	public long getMaxAccountId_01() {
		return this.m_maxAccountId_01.longValue();
	}
	public long getMaxAccountId_02() {
		return this.m_maxAccountId_01.longValue();
	}
	public int getMaxBranchId_01() {
		return this.m_maxBranchId_01.intValue();
	}
	public int getMaxBranchId_02() {
		return this.m_maxBranchId_01.intValue();
	}
	/*
	 * @param numAccounts - number of random accounts to create.
	 */
	public List<Long> getRandomAccountIds(long numAccounts) {
		List<Long> accountIds = new ArrayList<Long>();
		
		for(long i = 0; i < numAccounts; i++) {
			long randomAccountId = getRandomAccountId();
			if (this.getLogSql())
				System.out.println("Of total [" + this.getMaxAccountId()  + "] accounts, just generated [" + randomAccountId  + "]");
			accountIds.add( randomAccountId );
		}
			
		return accountIds;
	}
	public long getRandomAccountId() {
		
		/**
		 * The two data bases will likely have different max account ids.
		 * If a table has only 1000 account ids (1-1000 is how the load script is designed)
		 * then querying for account 1001 would produce and error we want to avoid.
		 */
		
		return (long)ThreadLocalRandom.current().nextDouble(1, getMaxAccountId() );
	}
	private long getMaxAccountId() {
		long max = -1;
		if (this.getDb()==1)
			max = this.getMaxAccountId_01() ;
		else if (this.getDb()==2)
			max = this.getMaxAccountId_02();
		return max;
	}
	public PkInquiry getPkInquiry() {
		return m_pkInquiry_3;
	}
	public ListInquiry getListInquiry() {
		return m_listInquiry_3;
	}
	public void log(String msg) {
		if (this.getLogSql())
			System.out.println(msg);
	}
	public int getNumScenario() {
		return this.m_numScenario.get();
	}
	public int getBranchScenarioNum() {
		return this.m_numBranchScenario.get();
	}
	public void setLogSql(boolean val) {
		this.m_logSql.set(val);
	}
	public boolean getLogSql() {
		return this.m_logSql.get();
	}
	
	public void setMaxAccountId_01(long val) {
		this.m_maxAccountId_01.set(val);
		log("Set maxAccounts to [" + val + "]");
	}
	public void setMaxAccountId_02(long val) {
		this.m_maxAccountId_02.set(val);
		log("Set maxAccounts to [" + val + "]");
	}
	public void setMaxBranchId_01(long val) {
		this.m_maxBranchId_01.set(val);
		log("Set maxBranch to [" + val + "]");
	}
	public void setMaxBranchId_02(long val) {
		this.m_maxBranchId_02.set(val);
		log("Set maxBranch to [" + val + "]");
	}
	
	public void setNumScenario(int val) {
		this.m_numScenario.set(val);
		log("Set Scenario Num to [" + val + "]");
	}
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		System.out.println("My startup method");
		
	}
	public void setBackendPort(Integer intPort) {
		this.backendPort = intPort;
	}
	public Integer getBackendPort() {
		return this.backendPort;
	}
	public void setDb(int intValue) throws PerfSandboxException {
		if (intValue <1 || intValue >2)
			throw new PerfSandboxException("only 1 and 2 are valid options for the db.");
		this.db = intValue;
	}
	public int getDb() {
		return this.db;
	}
	public BranchInquiry getBranchInquiry() {
		return this.branchInquiry;
	}
	public void setBranchScenarioNum(int intBranchScenarioNum, int numBranchInquiriesPerRoundTrip) {
		if (branchInquiry!=null)
			this.branchInquiry.shutdown();
		
		switch (intBranchScenarioNum) {
		case 0:
			this.m_numBranchScenario.set(intBranchScenarioNum);
			this.branchInquiry = null;
			break;
		case 1:
			this.m_numBranchScenario.set(intBranchScenarioNum);
			this.branchInquiry = new UncachedBranchInquiryImpl() {
				public DataSource getDataSource() throws SQLException {
					return PerfSandboxSingleton.this.getDataSource();
				}
				public int getMaxBranchId() throws SQLException {
					return PerfSandboxSingleton.this.getMaxBranchId();
				}
			};
			break;
		case 2:
			this.m_numBranchScenario.set(intBranchScenarioNum);
			this.branchInquiry = new CachedBranchInquiryImpl(){
				public DataSource getDataSource() throws SQLException {
					return PerfSandboxSingleton.this.getDataSource();
				}
				public int getMaxBranchId() throws SQLException {
					return PerfSandboxSingleton.this.getMaxBranchId();
				}
			};
			break;
		default:
			this.m_numBranchScenario.set(-1);
			throw new RuntimeException("branchScenarios [" + intBranchScenarioNum + "] was received. 0, 1 and 2 are supported");
		}
		
		if (this.branchInquiry !=null) {
			BaseSqlTextMgr bstm = new BaseSqlTextMgr();
			this.branchInquiry.setTableName(bstm.getTableNames().getBranchTable());
			branchInquiry.setInquiryCount(numBranchInquiriesPerRoundTrip);
		}
		
	}
	public String formatStats() {
		StringBuilder sb = new StringBuilder();
		sb.append("<PgBenchStats>");
		//sb.append( this.formatStats(sb) );
		 this.formatStats(sb);
		sb.append("</PgBenchStats>");
		return sb.toString();
	}
	public String formatStats(String stats, long duration) {
		StringBuilder sb = new StringBuilder();
		sb.append("<PgBenchStats>");
		sb.append( stats );
		sb.append("<Duration>").append(duration).append("</Duration>");
		//sb.append( this.formatStats(sb));
		this.formatStats(sb);
		sb.append("</PgBenchStats>");
		return sb.toString();
	}
	public String formatStats(StringBuilder sb) {
		sb.append("<Scenario>").append(Integer.toString(getNumScenario())).append("</Scenario>");
		sb.append("<LogSql>").append(getLogSql()).append("</LogSql>");
		sb.append("<Db>").append(getDb()).append("</Db>");
		int branchScenario = this.getBranchScenarioNum();
		sb.append("<BranchScenario>").append( branchScenario ).append("</BranchScenario>");
		int branchInquiryPerRoundTrip = -1;
		long totalPhysicalBranchInq = -1;
		
		if (branchScenario > 0 && this.branchInquiry !=null) {
			branchInquiryPerRoundTrip = this.branchInquiry.getInquiryCount();
			totalPhysicalBranchInq  = this.branchInquiry.getTotalPhysicalCount();
		}
		
		sb.append("<BranchInquiryPerRoundTrip>").append( branchInquiryPerRoundTrip ).append("</BranchInquiryPerRoundTrip>");
		sb.append("<BranchInquiryPhysicalCount>").append( totalPhysicalBranchInq ).append("</BranchInquiryPhysicalCount>");
		
		return sb.toString();
	}
	
}
