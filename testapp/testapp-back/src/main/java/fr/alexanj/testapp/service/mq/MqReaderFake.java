package fr.alexanj.testapp.service.mq;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import fr.alexanj.testapp.service.model.MqMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Profile("!prod")
public class MqReaderFake implements IMqReader{

	public MqReaderFake() {
	}

	/**
	 * fake service
	 * 
	 * @throws JMSException
	 */
	public List<MqMessage> readWithJms(int maxReads) {
		List<MqMessage> results = new ArrayList<MqMessage>();
		int rand = (int) (Math.random() * 1000) + 1;
		String message = StringUtils.join("Mocked message ", StringUtils.leftPad(String.valueOf(rand), 4, "0"));
		results.add(new MqMessage(null, LocalDateTime.now(), message));
		log.debug("Fake Message read");
		return results;
	}
}