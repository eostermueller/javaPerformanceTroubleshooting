package com.github.eostermueller.perfSandbox;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;


import com.github.eostermueller.perfSandbox.dataaccess_1.AccountMgr1;
import com.github.eostermueller.perfSandbox.dataaccess_2.AccountMgr2;
import com.github.eostermueller.perfSandbox.dataaccess_2.AllAccountMgr2;
import com.github.eostermueller.perfSandbox.dataaccess_3.AccountMgr3;
import com.github.eostermueller.perfSandbox.dataaccess_4.AccountMgr4;
import com.github.eostermueller.perfSandbox.dataaccess_5.AccountMgr5;
import com.github.eostermueller.perfSandbox.dataaccess_6.AccountMgr6;
import com.github.eostermueller.perfSandbox.filesystem.FileSystemReader;
import com.github.eostermueller.perfSandbox.model.Accounts;
import com.github.eostermueller.perfSandbox.model.SerializationUtil;
import com.github.eostermueller.perfSandbox.parse.ApacheCommonsPooledSaxParserWorker;
import com.github.eostermueller.perfSandbox.parse.EclipseEmfPooledSaxParserWorker;
import com.github.eostermueller.perfSandbox.parse.UnpooledSaxParserWorker;


@RestController
public class Controller implements ApplicationListener<ServletWebServerInitializedEvent>  {
	private static final int DEFAULT_BRANCH_INQ_PER_ROUND_TRIP = 5;
        private int port = -1;

	@Autowired
	public Controller(PerfSandboxSingleton val) throws PerfSandboxException {
		if (val==null) {
			throw new PerfSandboxException("Unable to create PgBench");
		}
		this.perfSandbox = val;
	}
	
	PerfSandboxSingleton perfSandbox = null;
    @RequestMapping("/")
    String homePage() {
        return "Home page of the Performance Sandbox.";
    }
    @RequestMapping(value="/backendStop", method=RequestMethod.GET, produces = { "application/xml", "text/xml" })
    String backendStop()  {
    	
    	this.perfSandbox.getHttpServer().stop();
    	this.perfSandbox.backendStarted.set(false);

		StringBuilder sb = new StringBuilder();
		sb.append("<Config>")
			.append("<msg>Backend stopped.</msg>")
			.append( getBackendStatus() )
			.append("</Config>");
		return sb.toString();
    }
    private ServletContext getServletContext() {
    	ApplicationContext ac = PerformanceSandboxApp.applicationContext;
    	ServletContext sc = null;
		if (ac instanceof WebApplicationContext) {
			WebApplicationContext wac = (WebApplicationContext)ac;
			sc = wac.getServletContext();
		}
    	return sc;
    }
    private String getServletContextPath() {
    	return this.getServletContext().getContextPath();
    }
	@Override
	public void onApplicationEvent(ServletWebServerInitializedEvent event) {
		this.port = event.getSource().getPort();
	}
    
    private int getPort() {
    	return port;
     }
    private String getBaseUrl() {
    	return "http://" + this.getServerAddress() + ":" + this.getPort() + this.getServletContextPath();
    }
    private String getServerAddress() {
		return "localhost"; //TODO:  get this from jetty instead of hard coding it.
	}
	private String getBackendStatus() {
    	    	
		StringBuilder sb = new StringBuilder();
    		sb.append("<isRunning>")
    		.append(this.perfSandbox.getHttpServer().isRunning())
    		.append("</isRunning>")
    		.append("<delayInMs>")
    		.append(this.perfSandbox.getHttpServer().getDelayInMs())
    		.append("</delayInMs>");
    		
    		
    		if (this.perfSandbox.getHttpServer().isRunning()) {
        		sb.append("<url>")
        		.append(this.perfSandbox.getHttpServer().getUrl())
        		.append("</url>");
    			sb.append("<stopInstructions>")
				.append("To stop the backend server, execute this url:  " + this.getBaseUrl() + "/backendStop")
				.append("</stopInstructions>");
    		} else {
    			sb.append("<startInstructions>")
    				.append("To start the backend server, execute this url:  " + this.getBaseUrl() + "/backendStart?port=" + this.getPort() + "&delayMs1000")
    				.append("</startInstructions>");
    		}
    		
    	return sb.toString();
    }
    @RequestMapping(value="/backendStatus", method=RequestMethod.GET, produces = { "application/xml", "text/xml" })
    String backendStatus()  {
			StringBuilder sb = new StringBuilder();
			sb.append("<Config>")
				.append( getBackendStatus() )
				.append("</Config>");

			return sb.toString();
    }
    @RequestMapping(value="/backendStart", method=RequestMethod.GET, produces = { "application/xml", "text/xml" })
    String backendStart(
    			@RequestParam(value="port", required=true) Integer intPort,
    			@RequestParam(value="delayMs", required=false, defaultValue = "0") Integer intDelayMs,
    			@RequestParam(value="memoryLeak", defaultValue = "false", required=false) Boolean ynMemoryLeak
    			) throws IOException, PerfSandboxException {

    	boolean ynStarted = this.perfSandbox.getHttpServer().start(intPort, intDelayMs, !ynMemoryLeak.booleanValue() );
    	perfSandbox.backendStarted.set(ynStarted);
    	
		StringBuilder sb = new StringBuilder();
		sb.append("<Config>")
			.append("<msg>Backend started</msg>")
			.append( getBackendStatus() )
			.append("</Config>");
		return sb.toString();
		
    }
    @RequestMapping(value="/startCpuBusy", method=RequestMethod.GET, produces = { "application/xml", "text/xml" })
    String startCpuBusy(
			@RequestParam(value="id", required=true) Integer id
    		)  {
    			this.perfSandbox.getBusyCpuProcessor(id).start();
    		try {
				TimeUnit.MILLISECONDS.sleep(BusyCpuProcessor.SLEEP_TIME_MS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		StringBuilder sb = new StringBuilder();
    		sb.append("<Config>")
    			.append("<msg>Busy CPU thread started.</msg>")
    			.append("<id>").append(id).append("</id>")
    			.append("<started>").append( this.perfSandbox.getBusyCpuProcessor(id).isStarted() ).append("</started>")
    			.append("</Config>");
    		return sb.toString();
    }
    @RequestMapping(value="/stopCpuBusy", method=RequestMethod.GET, produces = { "application/xml", "text/xml" })
    String stopCpuBusy(
			@RequestParam(value="id", required=true) Integer id
    		)  {
    		this.perfSandbox.getBusyCpuProcessor(id).stop();
    		try {
				TimeUnit.MILLISECONDS.sleep(BusyCpuProcessor.SLEEP_TIME_MS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    		
    		StringBuilder sb = new StringBuilder();
    		sb.append("<Config>")
    			.append("<msg>Busy CPU thread stopped.</msg>")
    			.append("<id>").append(id).append("</id>")
    			.append("<stopped>").append( !this.perfSandbox.getBusyCpuProcessor(id).isStarted() ).append("</stopped>")
    			.append("</Config>");
    		return sb.toString();
    }
    @RequestMapping(value="/crashJvmWithMonsterResultSet", method=RequestMethod.GET, produces = { "application/xml", "text/xml" })
    String crashJvmWithMonsterResultSet() throws PerfSandboxException {
		
		long start = System.currentTimeMillis();
		Accounts accounts = null;
		String stats = null;

		AllAccountMgr2 acctMgr2 = new AllAccountMgr2(perfSandbox);
		stats = acctMgr2.m_sqlTextMgr2.m_stats.getXmlStats();
		
		long end = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		sb.append("<Root>");
		SerializationUtil as = null;
		try {
			accounts = acctMgr2.getAllAccountsAndCrashJvm();		
			as = new SerializationUtil();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			as.setOutputStream(baos);
			as.serialize(accounts);
			sb.append( perfSandbox.formatStats( stats, (end-start)) );
			sb.append( as.getOutputStream().toString() );
			sb.append("</Root>");
		} catch (ParserConfigurationException e) {
			throw new PerfSandboxException(e);
		} catch (TransformerException e) {
			throw new PerfSandboxException(e);
		}
		return sb.toString();
		
    	
    }
    		
    @RequestMapping(value="/config", method=RequestMethod.GET, produces = { "application/xml", "text/xml" })
    String config(
    			@RequestParam(value="db", required=false) Integer intDb,
    			@RequestParam(value="branchScenarioNum", required=false) Integer intBranchScenarioNum,
    			@RequestParam(value="branchInqPerRoundTrip", required=false) Integer intBranchInqPerRoundTrip,
    			@RequestParam(value="scenarioNum", required=false) Integer intScenarioNum,
    			@RequestParam(value="fileSystemReadCount", required=false) Integer intFileSystemReadCount,
    			@RequestParam(value="eclipseEmfPooledSaxParseCount", required=false) Integer intEclipseEmfPooledSaxParseCount,
    			@RequestParam(value="apacheCommonsPooledSaxParseCount", required=false) Integer intApacheCommonsPooledSaxParseCount,
    			@RequestParam(value="unpooledSaxParseCount", required=false) Integer intUnpooledSaxParseCount,
    			@RequestParam(value="accountCloneCount", required=false) Integer intAccountCloneCount,
    			@RequestParam(value="logSql", required=false) Boolean ynLogSql
    			) throws IOException, PerfSandboxException {
		
		if (intEclipseEmfPooledSaxParseCount!=null)
			perfSandbox.setEclipseEmfPooledSaxParseCount(intEclipseEmfPooledSaxParseCount.intValue());
   
		if (intUnpooledSaxParseCount!=null)
			perfSandbox.setUnpooledSaxParseCount(intUnpooledSaxParseCount.intValue());
		
		if (intApacheCommonsPooledSaxParseCount!=null)
			perfSandbox.setApacheCommonsPooledSaxParseCount(intApacheCommonsPooledSaxParseCount.intValue());
		
    	if (intFileSystemReadCount!=null)
    		perfSandbox.setFileSystemReadCount(intFileSystemReadCount.intValue() );
    	
    	if (intAccountCloneCount!=null)
    		perfSandbox.setAccountCloneCount(intAccountCloneCount.intValue() );

    	if (intScenarioNum!=null)
    		perfSandbox.setNumScenario(intScenarioNum.intValue());
    	
    	if (ynLogSql!=null)
    		perfSandbox.setLogSql(ynLogSql.booleanValue());
    	
    	if (intDb!=null)
    		perfSandbox.setDb(intDb.intValue());
    	
    	if (intBranchScenarioNum!=null) {
    		int branchInqPerRoundTrip = DEFAULT_BRANCH_INQ_PER_ROUND_TRIP;
    		if (intBranchInqPerRoundTrip!=null)
    			branchInqPerRoundTrip = intBranchInqPerRoundTrip.intValue();
			perfSandbox.setBranchScenarioNum(intBranchScenarioNum.intValue(), branchInqPerRoundTrip);
    	}
    	
		StringBuilder sb = new StringBuilder();
		sb.append("<Config>");
		sb.append( perfSandbox.formatStats() );
		sb.append("</Config>");
		return sb.toString();
		
    }
    
    @RequestMapping(value="/randomInquiry", method=RequestMethod.GET, produces = { "application/xml", "text/xml" })
    String inquiry(
    			@RequestParam(value="numAccounts" ) int intNumAccounts
    			) throws IOException, PerfSandboxException {
    	
    	if (perfSandbox.getLogSql())
    		System.out.println("Received  rq for [" + intNumAccounts  + "] accounts");
		List<Long> accountIds_criteria = perfSandbox.getRandomAccountIds(intNumAccounts);
		
		return this.internalInquiry(accountIds_criteria);
    }
    
    @RequestMapping(value="/inquiry", method=RequestMethod.GET, produces = { "application/xml", "text/xml" })
    String inquiry(
    			@RequestParam("accountIds") String accountIds
    			) throws IOException, PerfSandboxException {
    	
    	List<Long> accountIds_criteria = this.getAccountIds(accountIds);
		return this.internalInquiry(accountIds_criteria);
    }
    
    public String getStats(int scenario) {
		String stats = null;
		switch(scenario) {
		case SCENARIO_0_NO_DB_ACCESS:
			break;
		case SCENARIO_1_SINGLE_QUERY:
			AccountMgr1 acctMgr1 = new AccountMgr1(perfSandbox);
			stats = acctMgr1.m_sqlTextMgr1.m_stats.getXmlStats();
			break;
		case SCENARIO_2_TWO_BULK_QUERIES:
			AccountMgr2 acctMgr2 = new AccountMgr2(perfSandbox);
			stats = acctMgr2.m_sqlTextMgr2.m_stats.getXmlStats();
			break;
		case SCENARIO_3_BULK_ACCOUNT_PIECEMEAL_HISTORY:
			AccountMgr3 acctMgr3 = new AccountMgr3(perfSandbox);
			stats = acctMgr3.m_sqlTextMgr3.m_stats.getXmlStats();
			break;
		case SCENARIO_4_SEPARATE_ACCOUNT_QUERIES:
			AccountMgr4 acctMgr4 = new AccountMgr4(perfSandbox);
			stats = acctMgr4.m_sqlTextMgr4.m_stats.getXmlStats();
			break;
		case SCENARIO_5_PK_LOOKUP_ONLY:
			AccountMgr5 acctMgr5 = new AccountMgr5(perfSandbox); 
			stats = acctMgr5.m_sqlTextMgr5.m_stats.getXmlStats();
			break;
		case SCENARIO_6_FROM_MEMORY:
			return "";
		default:
				throw new RuntimeException("Found URL Parameter [" + PARAM_SCENARIO_NUM + "=" + scenario + "].  Was expecting a value of 0-5." );
		}
    	return stats;
    }
    public Accounts internalInquiry2(List<Long> accountIds_criteria, int scenario) throws PerfSandboxException {
		Accounts accounts = null;

		for(int i = 0; i < this.perfSandbox.getFileSystemReadCount(); i++ ) {
			FileSystemReader fsr = new FileSystemReader();
			fsr.readConfig();
		}

		parse();
		
		if (this.perfSandbox.getBranchInquiry() != null)
			try {
				this.perfSandbox.getBranchInquiry().randomBranchInquiries();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new PerfSandboxException(e1);
			}

		switch(scenario) {
		case SCENARIO_0_NO_DB_ACCESS:
			break;
		case SCENARIO_1_SINGLE_QUERY:
			AccountMgr1 acctMgr1 = new AccountMgr1(this.perfSandbox);
			accounts = acctMgr1.getAccounts(accountIds_criteria);
			break;
		case SCENARIO_2_TWO_BULK_QUERIES:
			AccountMgr2 acctMgr2 = new AccountMgr2(perfSandbox);
			accounts = acctMgr2.getAccounts(accountIds_criteria);
			break;
		case SCENARIO_3_BULK_ACCOUNT_PIECEMEAL_HISTORY:
			AccountMgr3 acctMgr3 = new AccountMgr3(perfSandbox);
			accounts = acctMgr3.getAccounts(accountIds_criteria);
			break;
		case SCENARIO_4_SEPARATE_ACCOUNT_QUERIES:
			AccountMgr4 acctMgr4 = new AccountMgr4(perfSandbox);
			accounts = acctMgr4.getAccounts(accountIds_criteria);
			break;
		case SCENARIO_5_PK_LOOKUP_ONLY:
			AccountMgr5 acctMgr5 = new AccountMgr5(perfSandbox); 
			accounts = acctMgr5.getAccounts(accountIds_criteria);
			break;
		case SCENARIO_6_FROM_MEMORY:
			AccountMgr6 acctMgr6 = new AccountMgr6(perfSandbox); 
			accounts = acctMgr6.getAccounts(accountIds_criteria);
			break;
		default:
			throw new RuntimeException("Found URL Parameter [" + PARAM_SCENARIO_NUM + "=" + perfSandbox.getNumScenario() + "].  Was expecting a value of 1-5." );
		}
		
		List<Accounts> setsOfAccounts = new ArrayList<Accounts>(); //keep in scope until end of method.
		//Create extra memory for Young Gen to expire.
		for(int i = 0; i < this.perfSandbox.getAccountCloneCount(); i++) {
			setsOfAccounts.add( new Accounts(accounts) );
		}
		
		try {
			if (this.perfSandbox.backendStarted.get()) {
				this.perfSandbox.getHttpServer().getHttpResponse();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error making HTTP request to backend [" + this.perfSandbox.getHttpServer().getUrl());
			throw new PerfSandboxException(e);
		}
		return accounts;
    }
    /**
     * Parse an XML file.
     * Yes, parsing the same file repeatedly is not terribly realistic, but it does provide a nice test bed for comparison of pooled versus unpooled parsing.
     */
    private void parse() {
		UnpooledSaxParserWorker unpooledWorker = new UnpooledSaxParserWorker();
		for(int i = 0; i < this.perfSandbox.getUnpooledSaxParseCount(); i++ ) {
			if (i==0) 		
				unpooledWorker.setup();

			unpooledWorker.parse();
		}
		ApacheCommonsPooledSaxParserWorker acPooledWorker = new ApacheCommonsPooledSaxParserWorker();
		for(int i = 0; i < this.perfSandbox.getApacheCommonsPooledSaxParseCount(); i++ ) {
			if (i==0) 		
				acPooledWorker.setup();

			acPooledWorker.parse();
		}
		EclipseEmfPooledSaxParserWorker pooledWorker = new EclipseEmfPooledSaxParserWorker();
		for(int i = 0; i < this.perfSandbox.getEclipseEmfPooledSaxParseCount(); i++ ) {
			if (i==0) 		
				pooledWorker.setup();

			pooledWorker.parse();
		}
		
	}
	public String internalInquiry(List<Long> accountIds_criteria) throws PerfSandboxException, IOException {
		long start = System.currentTimeMillis();
		Accounts accounts = null;
		String stats = null;

		int scenario = perfSandbox.getNumScenario();
		boolean ynLogSql = perfSandbox.getLogSql();
		accounts = this.internalInquiry2(accountIds_criteria,scenario);
		
		
		stats = this.getStats(scenario);
		
		long end = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		sb.append("<Root>");
		//SerializationUtil as;
		SerializationUtil as = null;
		try {
			as = new SerializationUtil();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			as.setOutputStream(baos);
			as.serialize(accounts);
			sb.append( perfSandbox.formatStats( stats, (end-start)) );
			sb.append( as.getOutputStream().toString() );
			sb.append("</Root>");
		} catch (ParserConfigurationException e) {
			throw new PerfSandboxException(e);
		} catch (TransformerException e) {
			throw new PerfSandboxException(e);
		}
		//System.out.println("Here is the xml [" + sb.toString() + "]");
		return sb.toString();
    	
    }


	private static final long serialVersionUID = 681821029539761430L;
	
	private static final String PARAM_SCENARIO_NUM = "pgbenchScenarioNum";
	private static final String PARAM_ACCOUNT_IDS = "pgbenchAccountIds";
	private static final int SCENARIO_0_NO_DB_ACCESS = 0;
	private static final int SCENARIO_1_SINGLE_QUERY = 1;
	private static final int SCENARIO_2_TWO_BULK_QUERIES = 2;
	private static final int SCENARIO_3_BULK_ACCOUNT_PIECEMEAL_HISTORY = 3;
	private static final int SCENARIO_4_SEPARATE_ACCOUNT_QUERIES = 4;
	private static final int SCENARIO_5_PK_LOOKUP_ONLY = 5;
	private static final int SCENARIO_6_FROM_MEMORY = 6;

	
	private String formatStats(String xmlStats, long duration, int scenario, boolean ynLogSql, int db) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		sb.append("<PgBenchStats>");
		sb.append("<Scenario>").append(Integer.toString(scenario)).append("</Scenario>");
		sb.append("<LogSql>").append(ynLogSql).append("</LogSql>");
		sb.append( xmlStats );
		sb.append("<Duration>").append(duration).append("</Duration>");
		sb.append("<Db>").append(db).append("</Db>");
		sb.append("</PgBenchStats>");
		return sb.toString();
	}
	List<Long> getAccountIds(String strAccountIds) throws NumberFormatException {
		List<Long> accountIds = null;
		try {
			String[] aryAccountIds = strAccountIds.split(",");
			accountIds = new ArrayList<Long>();
			for(String oneAccountId : aryAccountIds)
				accountIds.add( new Long(Long.parseLong(oneAccountId)));
			
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Invalid HTTP Parameter value [" + strAccountIds + "] for variable ["  + PARAM_ACCOUNT_IDS + "]. Expecting a comma-delimited list of accountIds.");
		}
		return accountIds;

	}


}
