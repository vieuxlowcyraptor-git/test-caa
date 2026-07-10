package fr.alexanj.testapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@Configuration
@EnableScheduling
@PropertySource("classpath:mq.properties")
@ComponentScan(basePackages = { 
		"fr.alexanj.testapp.controller", 
		"fr.alexanj.testapp.service",
		"fr.alexanj.testapp.repository" })
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}