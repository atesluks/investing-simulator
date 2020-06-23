package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.domain.User;
import com.atesliuk.investing_simulator.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of User Service. Handles connection
 * between User Controller and User DAO
 */
@Service
public class UserServiceImpl implements UserService{

    /**
     * An autowired UserDAO object that will be used further
     * for communication with the database
     */
    @Autowired
    private UserDAO userDAO;

    /**
     * The method returns q list of all existing Users
     * in the database
     * @return an Iteratable of Users
     */
    @Override
    public Iterable<User> getAllUsers() {
        return userDAO.findAll();
    }

    /**
     * The method returns a particular User object
     * @param id - an ID of a searched user
     * @return a User object that matches the id
     */
    @Override
    public User getUser(Long id) {
        Optional<User> result = userDAO.findById(id);
        if (result.isPresent()) return result.get();
        else return null;
    }

    /**
     * The method saves new or updates an existing user
     * @param user - a User object that has to be saved or updated
     * @return - a saved or updated User object
     */
    @Override
    public User saveUser(User user) {
        return userDAO.save(user);
    }

    /**
     * The method deletes a user from database
     * @param user - a User object that has to be deleted
     */
    @Override
    public void deleteUser(User user) {
        userDAO.delete(user);
    }

    /**
     * The method tells if a provided password matches the user's real password.
     * This method is used for making changes in the account.
     * @param user - a User object for which we want to test the password
     * @param testedPassword - a String object, the password that has to be compared with the real one
     * @return - true if the provided password matches the user's real password, false otherwise
     */
    @Override
    public Boolean matchPassword(User user, String testedPassword) {
        return user.getPassword().equals(testedPassword);
    }

    /**
     * This method returns a User password from the provided email and password.
     * The method is used for loggin into account.
     * @param email - a String object, user's email
     * @param password - a String object, user's password
     * @return - a User object that matches the email and the password.
     *           If no users with such email and password, null is returned
     */
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
