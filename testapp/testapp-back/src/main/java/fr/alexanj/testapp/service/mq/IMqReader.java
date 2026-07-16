package fr.alexanj.testapp.service.mq;

import java.util.List;

import javax.jms.JMSException;

import fr.alexanj.testapp.service.model.MqMessage;

public interface IMqReader {
	/**
	 * read up to maxReads messages from the mq pile
	 * 
	 * @throws JMSException
	 */
	public List<MqMessage> readWithJms(int maxReads);
}