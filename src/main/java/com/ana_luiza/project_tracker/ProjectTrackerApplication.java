package com.ana_luiza.project_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ProjectTrackerApplication {
	
    static {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
        System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
        System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
    }

	public static void main(String[] args) {

		SpringApplication.run(ProjectTrackerApplication.class, args);
	}

}
