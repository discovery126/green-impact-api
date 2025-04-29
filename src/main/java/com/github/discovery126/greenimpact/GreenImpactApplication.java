package com.github.discovery126.greenimpact;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GreenImpactApplication {

	public static void main(String[] args) {
		Dotenv.configure()
				.systemProperties()
				.load();
		SpringApplication.run(GreenImpactApplication.class, args);
	}

}
