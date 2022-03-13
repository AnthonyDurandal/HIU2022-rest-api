package com.hackaton.restapi;

import java.sql.Timestamp;

import com.hackaton.restapi.service.CentreService;
import com.hackaton.restapi.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestapiApplicationTests {
	@Autowired
	private CentreService userService;

	@Test
	void contextLoads() {
		// getCentre(String sort, Integer page, Integer size,
        //     String id, String nom,String expressionLongitude, String expressionLatitude,expressionOuverture, String expressionFermeture,String nombrePersonnel,String idUser)
		userService.getCentre( null,  null,  null, "1",  "monNom", "lt:18.8888",  "gt:-26.2346786",  "lt:2022-02-02 00:00:00","gt:2022-12-02 00:00:00", "12", "1");
	}

}
