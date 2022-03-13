package com.hackaton.restapi.config;

import java.sql.Timestamp;
import java.time.LocalTime;

import com.hackaton.restapi.entity.Centre;
import com.hackaton.restapi.entity.Maladie;
import com.hackaton.restapi.entity.Personne;
import com.hackaton.restapi.entity.Role;
import com.hackaton.restapi.entity.User;
import com.hackaton.restapi.entity.Vaccin;
import com.hackaton.restapi.repository.CentreRepository;
import com.hackaton.restapi.repository.MaladieRepository;
import com.hackaton.restapi.repository.PersonneRepository;
import com.hackaton.restapi.repository.RoleRepository;
import com.hackaton.restapi.repository.UserRepository;
import com.hackaton.restapi.repository.VaccinRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InsertData {

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository, MaladieRepository maladieRepository, 
    PersonneRepository personneRepository, VaccinRepository vaccinRepository, CentreRepository centreRepository) {
        return args -> {
            User tmp = null;
            User tmp1 = null;
            if (userRepository.count() == 0) {
                Role role1 = new Role("admin");
                Role role2 = new Role("centreVaccination");
                Role role3 = new Role("personne");
                roleRepository.save(role1);
                roleRepository.save(role2);
                roleRepository.save(role3);

                User user1 = new User(
                    "admin1@test.com", 
                    passwordEncoder.encode("123456789"), 
                    new Timestamp(System.currentTimeMillis()), 
                    role1
                );
                
                User user2 = new User(
                    "usersimple@test.com", 
                    passwordEncoder.encode("123456789"), 
                    new Timestamp(System.currentTimeMillis()), 
                    role2
                );

                User user3 = new User(
                    "personne@test.com", 
                    passwordEncoder.encode("123456789"), 
                    new Timestamp(System.currentTimeMillis()), 
                    role3
                );
                userRepository.save(user1);
                tmp1 = userRepository.save(user2);
                tmp = userRepository.save(user3);
            }

            if(maladieRepository.count() == 0) {
                Maladie maladie1 = new Maladie("Diabète");
                Maladie maladie2 = new Maladie("Asthme");
                Maladie maladie3 = new Maladie("Cardiopathie");

                maladieRepository.save(maladie1);
                maladieRepository.save(maladie2);
                maladieRepository.save(maladie3);
            }

            if(personneRepository.count() == 0) {
                Personne personne = new Personne("Personne", "Test", new Timestamp(System.currentTimeMillis()), "101231174281", "personne@test.com", tmp);
                personneRepository.save(personne);
            }

            if(vaccinRepository.count() == 0) {
                Vaccin vaccin1 = new Vaccin("Pfizer", 3, 3);
                Vaccin vaccin2 = new Vaccin("Astrazeneca", 3, 5);
                vaccinRepository.save(vaccin1);
                vaccinRepository.save(vaccin2);
            }

            if(centreRepository.count() == 0) {
                Centre centre1 = new Centre("CCO Ivato", 18.434, -25.34, true, LocalTime.of(7, 30), LocalTime.of(18, 30), 5, tmp1);
                centreRepository.save(centre1);
            }
        };
    }
}