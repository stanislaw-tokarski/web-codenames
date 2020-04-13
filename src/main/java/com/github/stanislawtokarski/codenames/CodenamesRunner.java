package com.github.stanislawtokarski.codenames;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "com.github.stanislawtokarski.codenames")
@EnableVaadin(value = "com.github.stanislawtokarski.codenames.ui")
public class CodenamesRunner extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CodenamesRunner.class, args);
    }

}
