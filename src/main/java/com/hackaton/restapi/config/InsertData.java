package com.hackaton.restapi.config;

import java.sql.Timestamp;

import com.hackaton.restapi.entity.Maladie;
import com.hackaton.restapi.entity.Personne;
import com.hackaton.restapi.entity.Role;
import com.hackaton.restapi.entity.User;
import com.hackaton.restapi.repository.MaladieRepository;
import com.hackaton.restapi.repository.PersonneRepository;
import com.hackaton.restapi.repository.RoleRepository;
import com.hackaton.restapi.repository.UserRepository;

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
    CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository, MaladieRepository maladieRepository, PersonneRepository personneRepository) {
        return args -> {
            User tmp = null;
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
                userRepository.save(user2);
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
        };
    }
}