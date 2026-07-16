package fr.alexanj.proto.testapp.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.alexanj.testapp.service.db.StorageService;
import fr.alexanj.testapp.service.mq.IMqReader;
import fr.alexanj.testapp.service.mq.MqReader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MockConfig {
	@Bean
	private TextMessage mockTextMsg() {
		return mock(TextMessage.class);
	}

	@Bean
	private StorageService storageService() {
		return mock(StorageService.class);
	}

	@Bean
	private IMqReader mqReader(TextMessage mockTextMsg) throws JMSException {
		Session mockSession = mock(Session.class);
		Connection mockConn = mock(Connection.class);
		MessageConsumer mockConsumer = mock(MessageConsumer.class);
		when(mockConn.createSession(anyBoolean(), anyInt())).thenReturn(mockSession);
		when(mockSession.createConsumer(any())).thenReturn(mockConsumer);
		when(mockConsumer.receive(any(Long.class))).thenReturn(mockTextMsg);
		return new MqReader(mockConn);
	}

}