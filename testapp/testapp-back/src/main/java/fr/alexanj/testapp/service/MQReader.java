package fr.alexanj.testapp.service;

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

import fr.alexanj.testapp.repository.model.MqMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQReader {
	private final long mqTimeout;
	private final String mqName;
	private final Connection connection;

	public MQReader(Connection connection, String mqName, Long timeout) {
		this.connection = connection;
		this.mqTimeout = timeout;
		this.mqName = mqName;
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
				// Create consumer
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