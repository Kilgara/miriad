package com.truesoft.miriad.coreservice.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.truesoft.miriad.coreservice.user.domain.Role;
import com.truesoft.miriad.coreservice.user.domain.UserRole;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(UserRole name);
}
