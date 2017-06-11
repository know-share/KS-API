package com.knowshare.api.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.knowshare.enterprise","com.knowshare.api"})
public class Main {
	
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
