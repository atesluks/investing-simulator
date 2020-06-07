package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.domain.User;
import com.atesliuk.investing_simulator.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDAO userDAO;

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

    @Override
    public Boolean matchPassword(User user, String testedPassword) {
        return user.getPassword().equals(testedPassword);
    }

    @Override
    public User login(String email, String password) {
        List<User> result = userDAO.findByEmailAndPassword(email,password);
        //System.out.println("UserServiceImpl.loging() results: "+result);
        if (result.size()>1){
            throw new RuntimeException("Something went wrong! More than one User with the same email and password!");
        }else if (result.size()==0){
            return null;
        }else{
            return result.get(0);
        }
    }
}
