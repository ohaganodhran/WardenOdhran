package com.warden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("file:/var/WardenOdhran/local.properties")
public class WardenOdhranApplication {

	public static void main(String[] args) {
		SpringApplication.run(WardenOdhranApplication.class, args);
	}

}
