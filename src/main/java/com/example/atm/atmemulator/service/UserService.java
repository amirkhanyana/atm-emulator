package com.example.atm.atmemulator.service;

import com.example.atm.atmemulator.entity.User;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotBlank;

@Service
@Validated
public class UserService {

    public User createUser(@NonNull @NotBlank String username) {
        User user = new User();
        user.setUsername(username);
        return user;
    }
}
