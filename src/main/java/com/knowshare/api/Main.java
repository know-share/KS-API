package com.knowshare.api;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Clase principal donde el servidor tomcat embebido
 * inicia la ejecución
 * @author Miguel Montañez
 *
 */
@SpringBootApplication
@EnableMongoRepositories(basePackages={"com.knowshare.enterprise.repository"})
@ComponentScan(basePackages={"com.knowshare.enterprise.bean","com.knowshare.api.controller",
		"com.knowshare.api.interceptor"})
public class Main {
	
	public static void main(String[] args) {
		RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
		System.out.println("---------------------------------------------------");
		System.out.println("Running app with next arguments: "+runtimeMxBean.getInputArguments());
		System.out.println("---------------------------------------------------");
		SpringApplication.run(Main.class, args);
	}
}
