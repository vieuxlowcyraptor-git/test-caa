package fr.alexanj.testapp.controller;

import java.time.LocalDate;
import java.util.List;

import javax.jms.JMSException;

import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.alexanj.testapp.service.db.StorageService;
import fr.alexanj.testapp.service.model.MqMessage;
import fr.alexanj.testapp.service.mq.IMqReader;

@RestController
@RequestMapping(value = "/mq", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

	private IMqReader reader;
	private StorageService storage;

	public MessageController(IMqReader reader, StorageService storage) {
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
		storage.writeMessages(messages);
	}
}
