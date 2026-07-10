package fr.alexanj.testapp.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.alexanj.testapp.repository.model.MqMessage;

public interface MessageRepository extends JpaRepository<MqMessage, Long> {
	List<MqMessage> findAllByOrderByTimestampDesc(Pageable pageable);
}