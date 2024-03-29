package com.teamc.bioskop.Service;

import com.teamc.bioskop.Model.Role;
import com.teamc.bioskop.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAll();

    User createUser(User user);

    Role createRole(Role role);

    void addRoleToUser(String username, String roleName);

    User getUserByUsername(String username);

    Optional<User> getUserById(Long users_Id);

    void deleteUserById(Authentication authentication);

    User updateUser(User user, Authentication authentication);

    User getReferenceById(Long Id);

    Optional<User> findbyid(Long id);

    Page<User> findPaginated(int pageNumber, int pageSize);
}