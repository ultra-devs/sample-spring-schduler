package com.ultradev.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ultradev.service.SayHelloService;
import com.ultradev.util.JavaMailService;

@RestController

public class RunSchedulerController {
	@Autowired
	SayHelloService sayHelloService;

	@Autowired
	JavaMailService javaMailService;
	@GetMapping("/batch/run")
	public Response runBatchJob() {
		
		sayHelloService.processHello();
		javaMailService.sendHtmlMessage("Success :File Parsing Completed : " + new Date(),
				"<h5>SuccessFully Though Manual Run</h5>");
		Response response=new Response();
		response.setStatus("Success :File Parsing Completed Successfully ");
		return response ;
	}
	
	
	
	static class Response{
		
		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		String status;
	}

}
