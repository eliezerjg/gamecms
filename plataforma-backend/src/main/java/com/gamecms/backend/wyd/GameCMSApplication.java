package com.gamecms.backend.wyd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class GameCMSApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameCMSApplication.class, args);
	}

}
