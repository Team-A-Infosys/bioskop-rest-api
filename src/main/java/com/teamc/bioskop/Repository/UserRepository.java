package com.teamc.bioskop.Repository;

import com.teamc.bioskop.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmailId(String email);
}