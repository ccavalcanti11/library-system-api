package com.librarysystem;

import org.springframework.boot.SpringApplication;

public class TestLibrarySystemApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(LibrarySystemApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
