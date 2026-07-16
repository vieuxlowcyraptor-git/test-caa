package fr.alexanj.testapp.service.db;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import fr.alexanj.testapp.service.model.MqMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StorageService {

	private MessageRepository messageRepo;

	public StorageService(MessageRepository messageRepo) {
		super();
		this.messageRepo = messageRepo;
	}


	public List<MqMessage> getLatest(int limit) {
		Pageable pageable = PageRequest.of(0, limit, Sort.by("timestamp").descending());
		return messageRepo.findAllByOrderByTimestampDesc(pageable);
	}

	public void writeMessages(List<MqMessage> mqMessages) {
		messageRepo.saveAllAndFlush(mqMessages);
	}

}
