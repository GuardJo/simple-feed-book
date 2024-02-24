package com.guardjo.feedbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FeedbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedbookApplication.class, args);
	}

}
