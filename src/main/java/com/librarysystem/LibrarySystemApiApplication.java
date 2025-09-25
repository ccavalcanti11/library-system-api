package com.librarysystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LibrarySystemApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrarySystemApiApplication.class, args);
	}

}
