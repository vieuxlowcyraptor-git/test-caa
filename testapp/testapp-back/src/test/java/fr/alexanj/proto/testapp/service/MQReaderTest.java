package fr.alexanj.proto.testapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import fr.alexanj.testapp.repository.model.MqMessage;
import fr.alexanj.testapp.service.MQReader;

public class MQReaderTest {

	public TextMessage mockTextMsg = mock(TextMessage.class);
	public Connection mockConn = mock(Connection.class);

	@BeforeEach
	void setUp() throws JMSException {
		Session mockSession = mock(Session.class);
		MessageConsumer mockConsumer = mock(MessageConsumer.class);
		when(mockConn.createSession(anyBoolean(), anyInt())).thenReturn(mockSession);
		when(mockSession.createConsumer(any())).thenReturn(mockConsumer);
		when(mockConsumer.receive(any(Long.class))).thenReturn(mockTextMsg);
		reset(mockTextMsg);
	}

	@Test
	public void testReader() throws JMSException {
		Mockito.when(mockTextMsg.getText()).thenReturn("message1").thenReturn("message2").thenReturn("message3");
		MQReader mqReader = new MQReader(mockConn,"test", 5l);
		List<MqMessage> messages = mqReader.readWithJms(3);
		assertThat(messages).isNotEmpty()
			.first()
			.extracting(MqMessage::getMessage).isEqualTo("message1");

	}
}