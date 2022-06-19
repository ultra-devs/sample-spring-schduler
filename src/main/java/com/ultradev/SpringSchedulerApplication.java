package com.ultradev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ultradev.service.SayHelloService;
import com.ultradev.util.JavaMailService;

@SpringBootApplication
@EnableScheduling
public class SpringSchedulerApplication implements CommandLineRunner {
	@Autowired
	SayHelloService helloService;

	public static void main(String[] args) {
		SpringApplication.run(SpringSchedulerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		helloService.sayHello();
	}

}
