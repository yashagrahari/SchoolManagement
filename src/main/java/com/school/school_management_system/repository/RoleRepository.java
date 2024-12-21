package com.school.school_management_system.repository;

import java.util.Optional;

import com.school.school_management_system.models.ERole;
import com.school.school_management_system.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}