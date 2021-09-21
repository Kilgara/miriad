package com.truesoft.miriad.coreservice.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.truesoft.miriad.coreservice.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUuid(String uuid);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);
}
