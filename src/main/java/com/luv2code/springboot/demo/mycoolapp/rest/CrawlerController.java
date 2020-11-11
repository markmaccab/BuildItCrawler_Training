package com.luv2code.springboot.demo.mycoolapp.rest;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrawlerController {

	String root_url, origin, destination;
	
	@RequestMapping("/")
    public String handleEmployeeRequestByDept (@RequestParam("ROOT_URL") String root_url
                                          	) {
		this.root_url=root_url;
		String test;
		boolean junit=false;
		FunRestModel1 funRestModel = new FunRestModel1();
		System.out.println("ROOT_URL:  " + root_url );
		if (junit==true)
			System.out.println("All segments available:");		      
		test=funRestModel.testFunRestModel(root_url,junit);
		return test;
		}
		
	}	

	
	
