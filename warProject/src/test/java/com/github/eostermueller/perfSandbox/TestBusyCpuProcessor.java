package com.github.eostermueller.perfSandbox;

import static org.junit.Assert.*;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.concurrent.TimeUnit;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.junit.Test;

public class TestBusyCpuProcessor {
	  
		/**
		 * I could not find a reliable JVM way to measure CPU.
		 * So this is a manual test.  Comment in the "@Test", run this method, and watch your CPU montior...make sure about one core is in use for the duration.
		 * @throws MalformedObjectNameException
		 * @throws InstanceNotFoundException
		 * @throws ReflectionException
		 */
		//@Test
		public void cpuTest() throws MalformedObjectNameException, InstanceNotFoundException, ReflectionException {
			BusyCpuProcessor bcp = new BusyCpuProcessor();
			
			try {
				bcp.start();
				TimeUnit.SECONDS.sleep(60);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				bcp.stop();
			}
			

		}
	@Test
	public void simpleTest() {
		
		BusyCpuProcessor bcp = new BusyCpuProcessor();
		
		try {
			assertFalse("Haven't done anything yet -- should be stopped", bcp.isStarted() );
			
			bcp.start();
			
			mySleep();
			
			assertTrue("this test just invoked start, but status is wrong ", bcp.isStarted() );
			
			
		} finally {
			bcp.stop();
			mySleep();
			assertFalse("Just requested a stop -- so it should be stopped", bcp.isStarted() );
		}
		
		
	}
	@Test
	public void stopAndStartMultipleTimes() {
		BusyCpuProcessor bcp = new BusyCpuProcessor();
		
		try {
			bcp.start();
			assertTrue("this test just invoked start, but status is wrong ", bcp.isStarted() );
			bcp.start();
			mySleep();
			bcp.start();
			mySleep();
			bcp.start();
			mySleep();
			bcp.start();
			mySleep();
			bcp.start();
			mySleep();
			bcp.stop();
			mySleep();
			bcp.start();
			mySleep();
			bcp.stop();
			mySleep();
			bcp.stop();
			mySleep();
			bcp.start();
			mySleep();
			bcp.stop();
			mySleep();
			bcp.start();
			mySleep();
			bcp.stop();
			mySleep();
			bcp.start();
			mySleep();
			bcp.stop();
			mySleep();
			bcp.start();
			mySleep();
			bcp.stop();
			mySleep();
			bcp.start();
			mySleep();
			bcp.stop();
			mySleep();
			bcp.start();
			
		} finally {
			mySleep();
			bcp.stop();
			mySleep();
			assertFalse("Just requested a stop -- so it should be stopped", bcp.isStarted() );
			
		}
		
		
	}
	private void mySleep() {
		try {
			Thread.sleep(BusyCpuProcessor.SLEEP_TIME_MS);//value of 10 caused exceptions.
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
