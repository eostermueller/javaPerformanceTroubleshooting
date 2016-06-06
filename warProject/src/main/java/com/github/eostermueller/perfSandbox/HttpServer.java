package com.github.eostermueller.perfSandbox;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

/**
 * Provides a live HTTP server that provides
 * <ul>
 * 		<li>a single HTTP GET to /perfSandboxBackend</li>
 * 		<li>that will wait x milliseconds before returning the HTTP response. </li>
 * 		<li>To specify x, see the start() method.</li>
 * 		<li>default hostname is localhost</li>
 * 		<li>The http server listens on TCP port specified on the start() method.</li>
 * 		<li>The connect of the response is fixed -- see the constant HTTP_RESPONSE</li>
 * 		<li>the start() method implements both "start" and "restart" functionality</li> 
 * </ul>
 * @author erikostermueller
 *
 */
public class HttpServer {
	public static final String PERF_SANDBOX_BACKEND_URL = "/perfSandboxBackend";
	public static final String WIREMOCK_HOST = "localhost";
	public static final String HTTP_RESPONSE = "HelloWorldFromPerformanceSandBoxBackendServer";
	private static int DELAY_BEFORE_CONFIGURE = 2000;
	private String host = WIREMOCK_HOST;
	public static int DEFAULT_HTTP_LISTEN_PORT = 8674;

	int intPort = 0;
	int intDelayMs = 0;
	WireMockServer wireMockServer = null;

	public static void main_(String args[]) throws PerfSandboxException, IOException {
		HttpServer myServer = new HttpServer();
		
		if (!available(WIREMOCK_HOST,DEFAULT_HTTP_LISTEN_PORT)) 
			log("Can't run test unless [" + DEFAULT_HTTP_LISTEN_PORT + " is available");
		
		myServer.start(DEFAULT_HTTP_LISTEN_PORT, 1000);
		if (available(WIREMOCK_HOST,DEFAULT_HTTP_LISTEN_PORT)) 
			log("Server did not start on TCP port [" + DEFAULT_HTTP_LISTEN_PORT + " as expected.");

		String actualHttpResponse = myServer.getHttpResponse(); 
		if (!actualHttpResponse.equals(HTTP_RESPONSE))
			log("Found [" + actualHttpResponse + "] instead of [" + HTTP_RESPONSE + "]" );
		
		myServer.stop();
		if (!available(WIREMOCK_HOST,DEFAULT_HTTP_LISTEN_PORT)) 
			log("Server did not stop on TCP port [" + DEFAULT_HTTP_LISTEN_PORT + " as expected.");
	}
	public int getDelayInMs() {
		return this.intDelayMs;
	}
	public boolean isRunning() {
		boolean ynRC = false;
		if (this.wireMockServer!=null) {
			ynRC = this.wireMockServer.isRunning();
		}
		return ynRC;
	}
	public boolean stop() {
		boolean ynRC = true;
		if (this.wireMockServer!=null) {
			this.wireMockServer.stop();
			ynRC = !this.wireMockServer.isRunning();
		}
		return ynRC;
	}
	public String getHttpResponse() throws IOException {
		return getHttpResponse(getUrl());
	}
	public String getUrl() {
		return "http://localhost:" + this.intPort + PERF_SANDBOX_BACKEND_URL;
	}
	public String getHttpResponse(String url) throws IOException {
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();		
	}
	/**
	 * If the server is down, it will be started.
	 * If the server is up, it will be restarted.
	 * @param intPort
	 * @param intDelayMs
	 * @return
	 * @throws PerfSandboxException
	 */
	public boolean start(int intPort, int intDelayMs) throws PerfSandboxException {
		if (intPort <1 || intPort > 65535) {
			throw new PerfSandboxException("The /startBackend 'port' query parameter is out of bounds.  Must be a valid TCP port number, aka b/t 1 and 65535");
		} else {
			this.intPort = intPort;
		}
		
		if (this.wireMockServer != null && this.wireMockServer.isRunning())
			this.wireMockServer.stop();

		if (available(host,intPort)) {
    		this.wireMockServer = new WireMockServer(wireMockConfig().port(intPort) );

    		wireMockServer.start();
    		
    		try { Thread.sleep(DELAY_BEFORE_CONFIGURE); } catch (Exception e) {}
    		
    		if (!wireMockServer.isRunning()) {
    			throw new PerfSandboxException("Backend http system is unable to start.  Is some other process listening on port [" + intPort + "]?");
    		}
    		
    		this.intDelayMs = intDelayMs;
    		configure(intDelayMs);
    		return true;
    	} else {
    		throw new PerfSandboxException("Unable to start backend http server because some process listening on port [" + intPort + "].");    		
    	}
		
	}
	private void configure(int intDelayMs) {
		
		WireMock wireMock = new WireMock(host, this.intPort);
		wireMock.register(get(urlEqualTo(PERF_SANDBOX_BACKEND_URL)).willReturn(
                aResponse()
                .withStatus(200)
                .withBody(HTTP_RESPONSE)
                .withFixedDelay(intDelayMs)));
		
	}
	private static void log(String msg) {
		System.out.println("~@: " + msg);
	}
	/**
	 * Test to see if port is available.
	 * @param port
	 * @return
	 */
	public static boolean available(String hst, int port) {
	    try (Socket ignored = new Socket(hst, port)) {
	        return false;
	    } catch (IOException ignored) {
	        return true;
	    }
	}
}
