package fr.alexanj.proto.testapp.service.mq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import fr.alexanj.proto.testapp.config.MockConfig;
import fr.alexanj.testapp.config.ControllerConfig;
import fr.alexanj.testapp.config.PropertyConfig;
import fr.alexanj.testapp.service.model.MqMessage;
import fr.alexanj.testapp.service.mq.IMqReader;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ControllerConfig.class, PropertyConfig.class, MockConfig.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@WebAppConfiguration
public class MqReaderTest {
	@Autowired
	private IMqReader mqReader;
	@Autowired
	public TextMessage mockTextMsg;

	@BeforeEach
	void setUp() throws JMSException {
		reset(mockTextMsg);
	}

	@Test
	public void testReader() throws JMSException {
		Mockito.when(mockTextMsg.getText()).thenReturn("message1").thenReturn("message2").thenReturn("message3");
		List<MqMessage> messages = mqReader.readWithJms(1);
		assertThat(messages).isNotEmpty().first().extracting(MqMessage::getMessage).isEqualTo("message1");
		messages = mqReader.readWithJms(1);
		assertThat(messages).isNotEmpty().first().extracting(MqMessage::getMessage).isEqualTo("message2");
		messages = mqReader.readWithJms(1);
		assertThat(messages).isNotEmpty().first().extracting(MqMessage::getMessage).isEqualTo("message3");
	}
}