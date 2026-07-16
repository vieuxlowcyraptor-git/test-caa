package fr.alexanj.proto.testapp.service.mq;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.jms.JMSException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import fr.alexanj.testapp.config.ServiceConfig;
import fr.alexanj.testapp.service.model.MqMessage;
import fr.alexanj.testapp.service.mq.IMqReader;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@WebAppConfiguration
/**
 * integration test
 */
public class MqReaderInt {
	@Autowired
	private IMqReader mqReader;
	
	@Test
	public void testReader() throws JMSException {
		List<MqMessage> messages = mqReader.readWithJms(1);
		assertThat(messages).isNotEmpty();

	}
}