package com.hackaton.restapi;

import java.sql.Timestamp;

import com.hackaton.restapi.service.PersonneService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestapiApplicationTests {
	@Autowired
	private PersonneService userService;

	@Test
	void contextLoads() {
		// getPersonne(String sort, Integer page, Integer size,
        //     String id, String expressionNom,
        //     String expressionPrenom, String expressionDateNaissance, String expressionCIN,String expressionMail,String expressionIdUser)
		userService.getPersonne( null,  1,  3,
            null,  "like:nom",
             "prenom",  "lt:2022-02-02 00:00:00",  "lt:2022-02-02 00:00:00", "0020110241254125415", "102");
	}

}
