package fr.alexanj.testapp.service.mq;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import fr.alexanj.testapp.service.model.MqMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Profile("prod")
public class MqReader implements IMqReader {
	private Connection connection;
	private long mqTimeout;
	private String mqName;
	
	@Autowired
	public MqReader(
			@Value("${mq.host:}") String mqHost,
			@Value("${mq.port:0}") int mqPort,
			@Value("${mq.channel:DEFAULT}") String mqChannel,
			@Value("${mq.manage:}") String mqManager,
			@Value("${mq.application:}") String mqApp,
			@Value("${mq.name:}") String mqName,
			@Value("${mq.timeout:10}") long mqTimeout
			) {
		JmsFactoryFactory ff;
		this.mqTimeout = mqTimeout;
		this.mqName = mqName;
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
	}

	public MqReader(Connection connection) {
		this.connection = connection;
	}

	/**
	 * read up to maxReads messages from the mq pile
	 * 
	 * @throws JMSException
	 */
	public List<MqMessage> readWithJms(int maxReads) {
		List<MqMessage> results = new ArrayList<MqMessage>();
		if (Objects.nonNull(connection)) {
			try {
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				Destination queue = session.createQueue(mqName);
				MessageConsumer consumer = session.createConsumer(queue);

				connection.start();
				int count = 0;
				Message message;
				// Read until no more messages or maxCount is reached
				while (Objects.nonNull(message = consumer.receive(mqTimeout))) {
					count++;
					// we suppose the message is always a text since requirement is not clear about
					// what we expect in the mq pile
					if (message instanceof TextMessage textMsg) {
						LocalDateTime timestamp = LocalDateTime
								.ofInstant(Instant.ofEpochMilli(textMsg.getJMSTimestamp()), ZoneId.systemDefault());
						results.add(new MqMessage(null, timestamp, textMsg.getText()));
					} else {
						// message dropped
					}
					if (count >= maxReads) {
						break;
					}
				}
				log.debug("Message read :" + count);
				consumer.close();
				session.close();
				connection.close();
			} catch (JMSException exc) {
				log.error("failed during connection", exc);
			} catch (Throwable exc) {
				log.error("other error", exc);
			}
		}
		return results;
	}
}