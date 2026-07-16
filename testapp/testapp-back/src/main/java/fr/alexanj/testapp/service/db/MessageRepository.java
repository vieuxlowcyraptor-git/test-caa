package fr.alexanj.testapp.service.db;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.alexanj.testapp.service.model.MqMessage;

public interface MessageRepository extends JpaRepository<MqMessage, Long> {
	List<MqMessage> findAllByOrderByTimestampDesc(Pageable pageable);
}