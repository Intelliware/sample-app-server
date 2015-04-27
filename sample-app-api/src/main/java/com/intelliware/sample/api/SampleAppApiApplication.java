package com.intelliware.sample.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.intelliware.sample.api.dao.CompanyRepository;
import com.intelliware.sample.api.model.Company;

@SpringBootApplication
public class SampleAppApiApplication implements CommandLineRunner {

	@Autowired
	CompanyRepository repo;
	
    public static void main(String[] args) {
        SpringApplication.run(SampleAppApiApplication.class, args);
    }
    
    @Override 
    public void run(String ... args) throws Exception {
//    	repo.save( new Company("RBC"));
//    	for( Company c : repo.findAll()) {
//    		System.out.println(c);
//    	}
    }
}
