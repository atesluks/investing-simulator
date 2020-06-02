package com.atesliuk.investing_simulator.controller;

import com.atesliuk.investing_simulator.controller.exceptions.EntityNotFoundException;
import com.atesliuk.investing_simulator.domain.User;
import com.atesliuk.investing_simulator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public Iterable<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable Long userId){
        User theUser = userService.getUser(userId);
        if (theUser==null) throw new EntityNotFoundException("User id not found - "+userId);
        return theUser;
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User theUser){
        theUser.setId(0L);
        return userService.saveUser(theUser);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User theUser){
        return userService.saveUser(theUser);
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable Long userId){
        User theUser = userService.getUser(userId);
        if (theUser == null){
            //throw new CustomerNotFoundException("Customer id not found - "+customerId);
            return "Error. User with id "+userId+" was not found.";
        }
        userService.deleteUser(theUser);
        return "Deleted user id - " + userId;
    }

}
