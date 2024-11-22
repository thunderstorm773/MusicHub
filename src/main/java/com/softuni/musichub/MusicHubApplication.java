package com.softuni.musichub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MusicHubApplication {

	private static final String STARTING_URL = "http://localhost:8080";

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MusicHubApplication.class, args);

		Runtime.getRuntime().exec("cmd /c start chrome " + STARTING_URL);
	}
}
