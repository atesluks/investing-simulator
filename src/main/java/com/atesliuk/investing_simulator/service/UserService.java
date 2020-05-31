package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.domain.User;

public interface UserService {

    Iterable<User> getAllUsers();

    User getUser(Long id);

    User saveUser(User user);

    void deleteUser(User user);

}
