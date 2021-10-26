package com.example.atm.atmemulator.service;

import com.example.atm.atmemulator.entity.Account;
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
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("User with username '" + username + "' already exists.");
        }
        Account account = accountService.createAccount();
        User user = new User();
        user.setUsername(username);
        user.setAccount(account);
        user = userRepository.save(user);
        return user;
    }

    public Optional<User> getUserByUsername(@NonNull @NotBlank String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserById(long userId) {
        return userRepository.findById(userId);
    }
}
