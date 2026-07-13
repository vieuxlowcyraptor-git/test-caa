package fr.alexanj.testapp;

import javax.jms.Connection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import fr.alexanj.testapp.service.MQReader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
@PropertySource("classpath:mq.properties")
@ComponentScan(basePackages = { "fr.alexanj.testapp.controller", "fr.alexanj.testapp.service", "fr.alexanj.testapp.repository" })
public class AppConfig {


	@Value("${mq.host:}")
	private String mqHost;
	@Value("${mq.port:0}")
	private int mqPort;
	@Value("${mq.channel:C}")
	private String mqChannel;
	@Value("${mq.manage:}")
	private String mqManager;
	@Value("${mq.application:}")
	private String mqApp;
	@Value("${mq.name:}")
	private String mqName;
	@Value("${mq.timeout:10}")
	private long mqTimeout;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public MQReader mqReader() {		
		Connection connection = null;
		JmsFactoryFactory ff;
		try {
			ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
			JmsConnectionFactory cf = ff.createConnectionFactory();
	
			// Configure connection
			cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, mqHost);
			cf.setIntProperty(WMQConstants.WMQ_PORT, mqPort);
			cf.setStringProperty(WMQConstants.WMQ_CHANNEL, mqChannel);
			cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, mqApp);
			cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, mqManager);
			// Create connection and session
			connection = cf.createConnection();
	
		} catch (Throwable exc) {
			log.error("failed to init mqreader connection", exc);
		}
		return new MQReader(connection, mqName, mqTimeout);
	}
}