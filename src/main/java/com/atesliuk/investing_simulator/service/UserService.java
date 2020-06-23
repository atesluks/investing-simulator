package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.domain.User;

/**
 * Interface for User Service
 */
public interface UserService {

    Iterable<User> getAllUsers();

    User getUser(Long id);

    User saveUser(User user);

    void deleteUser(User user);

    Boolean matchPassword(User user, String testedPassword);

    User login(String email, String password);

}
