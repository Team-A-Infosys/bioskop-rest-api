package com.teamc.bioskop.Service;

import java.util.List;
import java.util.Optional;

import com.teamc.bioskop.Model.Seats;
import com.teamc.bioskop.Exception.ResourceNotFoundException;
import com.teamc.bioskop.Model.User;
import com.teamc.bioskop.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImplements implements UserService {
    private final UserRepository userRepository;

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

        return this.userRepository.save(user);
    }

    /***
     * Delete User
     * @param users_Id
     */
    @Override
    public void deleteUserById(Long users_Id) {
        Optional<User> optionalUser = userRepository.findById(users_Id);
        if (optionalUser == null) {
            throw new ResourceNotFoundException("User not exist with id :" + users_Id);
        }
        User user = userRepository.getReferenceById(users_Id);
        this.userRepository.delete(user);
    }

    /***
     * Update User
     * @param user
     * @return
     * @throws Exception
     */
    public User updateUser(User user, Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser == null) {
            throw new ResourceNotFoundException("User not exist with id :" + user.getUserId());
        }
        return this.userRepository.save(user);
    }

    @Override
    public User getReferenceById(Long Id) {
        return this.userRepository.getReferenceById(Id);
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