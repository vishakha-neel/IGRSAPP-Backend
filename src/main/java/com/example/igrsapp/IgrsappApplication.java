package com.example.igrsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(scanBasePackages = "com.example.igrsapp")

public class IgrsappApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(IgrsappApplication.class, args);
        System.out.println("Application started Successfully ..........");

        // Generate and print a BCrypt hashed password (for testing/admin setup)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin@4321";
        String hashedPassword = encoder.encode(rawPassword);
        System.out.println("Hashed password: " + hashedPassword);
    }
}
