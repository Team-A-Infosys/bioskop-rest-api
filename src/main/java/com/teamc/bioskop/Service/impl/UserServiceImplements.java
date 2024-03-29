package com.teamc.bioskop.Service.impl;

import java.util.*;

import com.teamc.bioskop.Model.Role;
import com.teamc.bioskop.Model.Seats;
import com.teamc.bioskop.Exception.ResourceNotFoundException;
import com.teamc.bioskop.Model.User;
import com.teamc.bioskop.Repository.RoleRepository;
import com.teamc.bioskop.Repository.UserRepository;
import com.teamc.bioskop.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImplements implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    Collection<Role> roles = new ArrayList<>();


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername()
                , user.getPassword(), authorities);
    }

    @Override
    public Role createRole(Role role) {
        return this.roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = this.userRepository.findByUsername(username);
        Role role = this.roleRepository.findByName(roleName);

        user.getRoles().add(role);
    }

    @Override
    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    /***
     * Get All User
     * @return
     */
    public List<User> getAll() {
        List<User> user = userRepository.findAll();
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not exist with id :");
        }
        return this.userRepository.findAll();

    }

    /***
     * Get User By ID
     * @param users_Id
     * @return
     */
    public Optional<User> getUserById(Long users_Id) {
        Optional<User> optionalUser = userRepository.findById(users_Id);
        if (optionalUser == null) {
            throw new ResourceNotFoundException("User not exist with id :" + users_Id);
        }
        return this.userRepository.findById(users_Id);
    }

    /***
     * Create User
     * @param user
     * @return
     */
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles().isEmpty()){
            Role role = this.roleRepository.findByName("ROLE_CUSTOMER");
            roles.add(role);
            user.setRoles(roles);
        }
        User checkEmail = this.userRepository.findByEmailId(user.getEmailId());
        User checkUsername = this.userRepository.findByUsername(user.getUsername());

        if (checkEmail != null) {
            throw new ResourceNotFoundException("Email has been taken");
        }

        if(checkUsername != null){
            throw new ResourceNotFoundException("Username has been taken");
        }
            return this.userRepository.save(user);

    }

    /***
     * Delete User
     * @param users_Id
     */
    @Override
    public void deleteUserById(Authentication authentication) {
        if (authentication == null){
            throw new ResourceNotFoundException("Not authenticated, forbidden");
        }
        String userAuth = authentication.getName();
        User user = this.userRepository.findByUsername(userAuth);
        this.userRepository.delete(user);
    }

    /***
     * Update User
     * @param user
     * @return
     * @throws Exception
     */
    public User updateUser(User user,  Authentication authentication) {
        String username = authentication.getName();

        User userAuth = this.userRepository.findByUsername(username);

        user.setUserId(userAuth.getUserId());
        user.setRoles(userAuth.getRoles());
        user.setPassword(userAuth.getPassword());

        if (user.getUsername() == null){
            user.setUsername(userAuth.getUsername());
        }
        else if (user.getEmailId() == null){
            user.setEmailId(userAuth.getEmailId());
        }

        return this.userRepository.save(user);
    }

    @Override
    public User getReferenceById(Long Id) {
        return this.userRepository.getById(Id);
    }

    @Override
    public Optional<User> findbyid(Long id) {

        Optional<User> optionalUsers = userRepository.findById(id);
        if (optionalUsers == null) {
            throw new ResourceNotFoundException(" Seats not Exist with id :" + id);
        }
        return userRepository.findById(id);
    }

    @Override
    public Page<User> findPaginated(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);

        return this.userRepository.findAll(pageable);
    }
}