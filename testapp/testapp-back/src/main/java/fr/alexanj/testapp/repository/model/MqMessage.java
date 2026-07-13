package fr.alexanj.testapp.repository.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
@Entity
@Table(name = "TA_MESSAGES")
@Data
@AllArgsConstructor
public class MqMessage{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private LocalDateTime timestamp;
    private String message;
}
