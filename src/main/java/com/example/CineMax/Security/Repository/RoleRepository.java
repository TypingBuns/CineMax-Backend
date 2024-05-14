package com.example.CineMax.Security.Repository;

import com.example.CineMax.Security.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
