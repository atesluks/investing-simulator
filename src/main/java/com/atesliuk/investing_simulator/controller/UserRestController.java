package com.atesliuk.investing_simulator.controller;

import com.atesliuk.investing_simulator.controller.exceptions.UserNotFoundException;
import com.atesliuk.investing_simulator.domain.User;
import com.atesliuk.investing_simulator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/users")
    public Iterable<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping(value = "/users/{userId}")
    public User getUser(@PathVariable Long userId){
        User theUser = userService.getUser(userId);
        if (theUser==null) throw new UserNotFoundException("User id not found - "+userId);
        return theUser;
    }

    @PostMapping(value = "/users")
    public User addUser(@RequestBody User user){
        user.setId(0L);
        return userService.saveUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @DeleteMapping(value = "/users/{userId}")
    public String deleteUser(@PathVariable Long userId){
        User tempUser = userService.getUser(userId);
        if (tempUser == null){
            //throw new CustomerNotFoundException("Customer id not found - "+customerId);
            return "Error. User with id "+userId+" was not found.";
        }
        userService.deleteUser(tempUser);
        return "Deleted user id - " + userId;
    }

}
