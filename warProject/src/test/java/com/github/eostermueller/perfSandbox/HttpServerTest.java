package com.github.eostermueller.perfSandbox;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.github.eostermueller.perfSandbox.HttpServer;
import com.github.eostermueller.perfSandbox.PerfSandboxException;


public class HttpServerTest {

	/**
	 * Validates that code can launch a live HTTP server, configure a simple REST service, invoke it and get the expected response.
	 */
	@Test
	public void test()  {
		HttpServer myServer = new HttpServer();
		try {
			assertTrue("TCP port [" + HttpServer.DEFAULT_HTTP_LISTEN_PORT + "t must be avilable for this test to run...and it is NOT available",
					HttpServer.available(HttpServer.WIREMOCK_HOST,HttpServer.DEFAULT_HTTP_LISTEN_PORT));
			
			myServer.start(HttpServer.DEFAULT_HTTP_LISTEN_PORT, 1000);

			assertTrue("Server did not start on TCP port [" + HttpServer.DEFAULT_HTTP_LISTEN_PORT + " as expected.",
					!HttpServer.available(HttpServer.WIREMOCK_HOST,HttpServer.DEFAULT_HTTP_LISTEN_PORT));

			String actualHttpResponse = myServer.getHttpResponse(); 

			assertEquals("HttpServer did not return expected response", HttpServer.HTTP_RESPONSE, actualHttpResponse);
			
		} catch (Exception e) {
			fail("Unexpected exception");
		} finally {
			myServer.stop();
			assertTrue("HttpServer was unable to stop TCP port [" + HttpServer.DEFAULT_HTTP_LISTEN_PORT + " as expected.",
					HttpServer.available(HttpServer.WIREMOCK_HOST,HttpServer.DEFAULT_HTTP_LISTEN_PORT));
			
		}
		
		
	}

}
