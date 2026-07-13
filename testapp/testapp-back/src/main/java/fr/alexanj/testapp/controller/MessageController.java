package fr.alexanj.testapp.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.jms.JMSException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.alexanj.testapp.repository.model.MqMessage;
import fr.alexanj.testapp.service.MQReader;
import fr.alexanj.testapp.service.StorageService;

@RestController
@RequestMapping(value = "/mq", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

	private MQReader reader;
	private StorageService storage;

	public MessageController(MQReader reader, StorageService storage) {
		super();
		this.reader = reader;
		this.storage = storage;
	}

	/**
	 * @return last messages stored in DB
	 */
	@GetMapping("/last")
	public List<MqMessage> last(@RequestParam int nb) {
		return storage.getLatest(nb);
	}

	/**
	 * 
	 * @param min
	 * @param max
	 * @return message issued between two date
	 */
	@GetMapping("/period")
	public List<MqMessage> period(@RequestParam LocalDate min, @RequestParam LocalDate max) {
		return null;
	}

	/**
	 * every 30s we attempt to read the mq file up to ten time to avoid writing too
	 * fast in the DB
	 * 
	 * @throws JMSException
	 */
	@Scheduled(fixedDelay = 30000)
	public void readMqFile() {
		List<MqMessage> messages = reader.readWithJms(10);
		if (CollectionUtils.isEmpty(messages)) {
			int rand = (int) (Math.random() * 1000) + 1;
			String message = StringUtils.join("Mocked message ", StringUtils.leftPad(String.valueOf(rand), 4, "0"));
			messages.add(new MqMessage(null, LocalDateTime.now(), message));
		}
		storage.writeMessages(messages);
	}
}
