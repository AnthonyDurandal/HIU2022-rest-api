package com.hackaton.restapi.repository;

import java.util.List;
import java.util.Optional;

import com.hackaton.restapi.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {
    
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.username = ?1 and u.role.nom = ?2")
    Optional<User> findByUsernameAndRole(String username, String role);

    @Query("SELECT u FROM User u WHERE u.role.id = ?1")
    List<User> findByRole(Long roleId);
}
