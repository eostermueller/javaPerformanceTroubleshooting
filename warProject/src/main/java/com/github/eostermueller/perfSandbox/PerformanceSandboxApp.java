package com.github.eostermueller.perfSandbox;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableAutoConfiguration
////@ComponentScan("com.github.eostermueller.perfSandbox.x07")
//@ComponentScan
@PropertySource("classpath:/application.properties")
public class PerformanceSandboxApp extends SpringBootServletInitializer {

	/**
	 * 
	 * NOT WORKING :-(
	 * Some popular data available in this env.
	 * http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html
	 */
	@Resource
	public Environment environment;
	public static ApplicationContext applicationContext= null;   
    public static void main(String[] args) throws Exception {
    	 
    	applicationContext = SpringApplication.run(PerformanceSandboxApp.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PerformanceSandboxApp.class);
    }
    /**
     * Perhaps try this example to get the context path:
     * https://github.com/bijukunjummen/try-spring-boot-config
     * @return
     */
    private String getContextPath() {
    	String contextPath = this.environment.getProperty("server.context-path");
    	return contextPath;
    }
    private String getBaseUrl() {
    	return "http://" + this.getServerAddress() + ":" + this.getPort() + this.getContextPath();
    }
    private String getServerAddress() {
    	String serverAddress = environment.getProperty("server.context-path");	
    	return serverAddress;
    	
    }
    private int getPort() {
    	String port = environment.getProperty("local.server.port");	
    	return Integer.valueOf(port);
    }
    
}