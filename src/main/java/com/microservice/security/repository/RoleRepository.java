package com.microservice.security.repository;

import com.microservice.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<com.microservice.security.model.Role, Integer> {
    Role findRoleByName(String name);
}
