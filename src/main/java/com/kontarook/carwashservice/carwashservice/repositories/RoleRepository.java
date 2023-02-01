package com.kontarook.carwashservice.carwashservice.repositories;

import com.kontarook.carwashservice.carwashservice.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(String roleUser);
}
