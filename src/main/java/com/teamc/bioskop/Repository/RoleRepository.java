package com.teamc.bioskop.Repository;

import com.teamc.bioskop.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
