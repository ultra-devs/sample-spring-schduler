package com.ultradev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ultradev.util.JavaMailService;

@SpringBootApplication
@EnableScheduling
public class SpringSchedulerApplication implements CommandLineRunner {
	@Autowired
	JavaMailService javaMailService;

	public static void main(String[] args) {
		SpringApplication.run(SpringSchedulerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		/*
		 * javaMailService.
		 * sendHtmlMessage("Success:File Parsing is Completed SuccessFully for :" + new
		 * Date(), "<h1>Batch File Parsing Completed Successfully </h1>",null);
		 */
	}

}
