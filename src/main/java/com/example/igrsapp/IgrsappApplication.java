package com.example.igrsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class IgrsappApplication  extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(IgrsappApplication.class, args);
		System.out.println("Application started Successfully ..........");
	}

}
