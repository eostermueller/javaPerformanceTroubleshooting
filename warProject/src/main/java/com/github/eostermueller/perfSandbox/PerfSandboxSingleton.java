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
		} catch (PerfSandboxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	/**
	 * I've set this default of 100 because in the JMeter loadDb.jmx, we have:
	 * BRANCH_COUNT_PER_THREAD=5
	 * THREAD_COUNT=20
	 */
	public AtomicInteger m_branchCount = new AtomicInteger(100);
	
	private AtomicLong m_maxAccountId_01 = new AtomicLong(20);
	private AtomicLong m_maxAccountId_02 = new AtomicLong(20);

	private PkInquiry m_pkInquiry_3 = null;

	private ListInquiry m_listInquiry_3 = null;

	private AtomicInteger m_numScenario = new AtomicInteger(1);
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
		return this.m_maxAccountId_02.longValue();
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
	public int getBranchCount() {
		return this.m_branchCount.get();
	}
	public int getNumScenario() {
		return this.m_numScenario.get();
	}
	public void setLogSql(boolean val) {
		this.m_logSql.set(val);
	}
	public boolean getLogSql() {
		return this.m_logSql.get();
	}
	public void setBranchCount(int val) {
		this.m_branchCount.set(val);
		log("Set branchCount to [" + val + "]");
	}
	
	public void setMaxAccountId_01(long val) {
		this.m_maxAccountId_01.set(val);
		log("Set maxAccounts to [" + val + "]");
	}
	public void setMaxAccountId_02(long val) {
		this.m_maxAccountId_02.set(val);
		log("Set maxAccounts to [" + val + "]");
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
}
