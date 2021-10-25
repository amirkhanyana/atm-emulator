package com.example.atm.atmemulator.service;

import com.example.atm.atmemulator.entity.User;
import com.example.atm.atmemulator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Service
@Validated
public class UserService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;

    public User createUser(@NonNull @NotBlank String username) {
        User user = new User();
        user.setUsername(username);
        user = userRepository.save(user);
        accountService.createAccountForUser(user.getId());
        return user;
    }

    public Optional<User> getUserById(long userId) {
        return userRepository.findById(userId);
    }
}
