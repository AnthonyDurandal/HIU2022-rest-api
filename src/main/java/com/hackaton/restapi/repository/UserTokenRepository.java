package com.hackaton.restapi.repository;

import java.util.Optional;

import com.hackaton.restapi.entity.UserToken;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface UserTokenRepository extends JpaRepository<UserToken, Long>,
        JpaSpecificationExecutor<UserToken> {
    
    @Query("SELECT ut FROM UserToken ut WHERE ut.token = ?1")
    Optional<UserToken> findByToken(String token);

    @Query("SELECT ut FROM UserToken ut WHERE ut.user.id = ?1")
    Page<UserToken> findByUserId(Long userId, Pageable pageable);
}
