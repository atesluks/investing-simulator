package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.domain.User;
import com.atesliuk.investing_simulator.repository.UserDAO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @Override
    public Iterable<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public User getUser(Long id) {
        Optional<User> result = userDAO.findById(id);
        if (result.isPresent()) return result.get();
        else return null;
    }

    @Override
    public User saveUser(User user) {
        return userDAO.save(user);
    }

    @Override
    public void deleteUser(User user) {

        userDAO.delete(user);
    }
}
