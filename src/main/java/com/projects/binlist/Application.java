package com.projects.binlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.projects.binlist"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        
    }
    
}
